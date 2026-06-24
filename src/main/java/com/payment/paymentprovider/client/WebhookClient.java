package com.payment.paymentprovider.client;

import com.payment.paymentprovider.dto.WebhookPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookClient {

  private final RestTemplate restTemplate;

  @Value("${payment-service.webhook-url}")
  private String webhookUrl;

  public void sendWebhook(WebhookPayload payload) {
    try {
      log.info("[Provider] Bắn webhook → {} | transactionId={}, status={}",
          webhookUrl, payload.getTransactionId(), payload.getStatus());
      restTemplate.postForObject(webhookUrl, payload, Void.class);
    } catch (Exception e) {
      // Webhook fail (Payment Service không nhận được) — trong hệ
      // thống thật, đây là lúc cần retry-with-backoff hoặc dead
      // letter queue. Demo chỉ log lỗi, không retry — Payment
      // Service có SagaRecoveryJob phía Orchestrator để tự phát
      // hiện giao dịch bị "treo" và xử lý sau.
      log.error("[Provider] Gửi webhook thất bại: {}", e.getMessage());
    }
  }
}
