package com.payment.paymentprovider.service;

import com.payment.paymentprovider.client.WebhookClient;
import com.payment.paymentprovider.config.ProviderConfig.GlobalProviderSettings;
import com.payment.paymentprovider.dto.ChargeRequest;
import com.payment.paymentprovider.dto.ChargeResponse;
import com.payment.paymentprovider.dto.WebhookPayload;
import com.payment.paymentprovider.entity.ProviderTransaction;
import com.payment.paymentprovider.respository.ProviderTransactionRepository;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderService {
  private final ProviderTransactionRepository transactionRepository;
  private final GlobalProviderSettings globalSettings;
  private final WebhookClient webhookClient;

  /**
   * Khởi tạo giao dịch — trả PENDING NGAY, xử lý thật chạy ở thread
   * khác (processAsync). Đây là điểm khác biệt cốt lõi so với
   * InventoryService/cách cũ: không trả kết quả cuối trong request này.
   */
  @Transactional
  public ChargeResponse charge(ChargeRequest request) {
    String transactionId = UUID.randomUUID().toString();

    int delayMs = globalSettings.getDelayMs();
    int failRate = globalSettings.getFailRate();

    ProviderTransaction tx = new ProviderTransaction(
        transactionId, request.getSagaId(), request.getAmount(),
        delayMs, failRate, request.getForceOutcome());
    transactionRepository.save(tx);

    log.info("[Provider] Tạo giao dịch PENDING transactionId={}, sagaId={}, delayMs={}, failRate={}%, forceOutcome={}",
        transactionId, request.getSagaId(), delayMs, failRate, request.getForceOutcome());

    processAsync(transactionId);

    return new ChargeResponse(transactionId, "PENDING", "Giao dịch đang được xử lý");
  }

  /**
   * Chạy trong thread pool riêng (@Async) — KHÔNG block request charge()
   * gốc. Đợi delayMs (giả lập thời gian xử lý thật của 1 payment gateway),
   * rồi quyết định SUCCESS/FAILED, lưu DB, và tự bắn webhook về Payment
   * Service — đúng như Stripe/VNPay/Momo làm ngoài đời.
   */
  @Async
  public void processAsync(String transactionId) {
    ProviderTransaction tx = transactionRepository.findById(transactionId).orElse(null);
    if (tx == null) {
      log.error("[Provider] processAsync: không tìm thấy transaction {}", transactionId);
      return;
    }

    try {
      Thread.sleep(tx.getDelayMs());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    boolean success = decideOutcome(tx);
    tx.setStatus(success ? "SUCCESS" : "FAILED");
    tx.setUpdatedAt(java.time.LocalDateTime.now());
    transactionRepository.save(tx);

    log.info("[Provider] Xử lý xong transactionId={}, kết quả={}", transactionId, tx.getStatus());

    webhookClient.sendWebhook(new WebhookPayload(
        tx.getTransactionId(),
        tx.getSagaId(),
        tx.getStatus(),
        success ? "Giao dịch thành công" : "Giao dịch bị từ chối (giả lập)"
    ));
  }

  private boolean decideOutcome(ProviderTransaction tx) {
    // Override per-request có quyền cao nhất, dùng để demo ép kết
    // quả cụ thể (vd: luôn show được case fail trong phỏng vấn mà
    // không phải canh tỷ lệ random).
    if ("SUCCESS".equalsIgnoreCase(tx.getForcedOutcome())) return true;
    if ("FAIL".equalsIgnoreCase(tx.getForcedOutcome())) return false;

    int roll = ThreadLocalRandom.current().nextInt(100);
    return roll >= tx.getFailRate();
  }
}
