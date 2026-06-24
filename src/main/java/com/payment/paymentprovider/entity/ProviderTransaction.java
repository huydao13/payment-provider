package com.payment.paymentprovider.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provider_transactions")
@Data
@NoArgsConstructor
public class ProviderTransaction {

  @Id
  private String transactionId;

  private String sagaId;
  private Long amount;
  /**
   * PENDING -> SUCCESS hoặc FAILED, set bởi thread async sau khi
   * "xử lý" xong (giả lập độ trễ + tỷ lệ fail thật).
   */
  private String status;

  private Integer delayMs;
  private Integer failRate;

  /**
      * Override per-request: "SUCCESS" | "FAIL" | null (random theo failRate).
      * Cho phép demo ép cứng 1 kết quả cụ thể mà không cần chỉnh failRate global.
      */
  private String forcedOutcome;

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  public ProviderTransaction(String transactionId, String sagaId, Long amount,
      Integer delayMs, Integer failRate, String forcedOutcome) {
    this.transactionId = transactionId;
    this.sagaId = sagaId;
    this.amount = amount;
    this.status = "PENDING";
    this.delayMs = delayMs;
    this.failRate = failRate;
    this.forcedOutcome = forcedOutcome;
  }

}
