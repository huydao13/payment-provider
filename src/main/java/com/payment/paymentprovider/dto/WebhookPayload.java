package com.payment.paymentprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebhookPayload {
  private String transactionId;
  private String sagaId;
  private String status; // SUCCESS | FAILED
  private String message;
}
