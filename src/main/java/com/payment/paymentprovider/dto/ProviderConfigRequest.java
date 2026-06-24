package com.payment.paymentprovider.dto;

import lombok.Data;

@Data
public class ProviderConfigRequest {

  /** Độ trễ giả lập (ms) trước khi provider trả kết quả qua webhook. */
  private Integer delayMs;

  /** Tỷ lệ fail ngẫu nhiên, 0-100 (%). */
  private Integer failRate;
}
