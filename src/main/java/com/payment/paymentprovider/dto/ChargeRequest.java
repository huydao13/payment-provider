package com.payment.paymentprovider.dto;

import lombok.Data;

@Data
public class ChargeRequest {

  private String sagaId;
  private Long amount;

  /**
   * Override riêng cho request này — không set thì dùng global config.
   * "SUCCESS" | "FAIL" | null
   */
  private String forceOutcome;

}
