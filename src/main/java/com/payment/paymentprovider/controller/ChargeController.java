package com.payment.paymentprovider.controller;

import com.payment.paymentprovider.dto.ChargeRequest;
import com.payment.paymentprovider.dto.ChargeResponse;
import com.payment.paymentprovider.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
@Tag(name = "Provider Charge", description = "Endpoint nhận yêu cầu charge từ Payment Service")
public class ChargeController {
  private final ProviderService providerService;

  @PostMapping("/charge")
  @Operation(
      summary = "Khởi tạo giao dịch charge",
      description = "Trả PENDING ngay, kết quả thật về sau qua webhook gọi ngược về Payment Service"
  )
  public ResponseEntity<ChargeResponse> charge(@RequestBody ChargeRequest request) {
    return ResponseEntity.ok(providerService.charge(request));
  }

}
