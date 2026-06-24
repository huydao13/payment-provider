package com.payment.paymentprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChargeResponse {
  private String transactionId;
  private String status;
  private String message;

}
