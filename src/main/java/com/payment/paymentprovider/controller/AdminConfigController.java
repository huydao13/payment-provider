package com.payment.paymentprovider.controller;

import com.payment.paymentprovider.config.ProviderConfig.GlobalProviderSettings;
import com.payment.paymentprovider.dto.ProviderConfigRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider/config")
@RequiredArgsConstructor
@Tag(name = "Provider Admin Config", description = "Cấu hình hành vi giả lập của provider — delay/fail rate")
public class AdminConfigController {
  private final GlobalProviderSettings settings;

  @GetMapping
  @Operation(summary = "Xem config hiện tại")
  public ResponseEntity<GlobalProviderSettings> getConfig() {
    return ResponseEntity.ok(settings);
  }

  @PostMapping
  @Operation(summary = "Cập nhật config global — áp dụng cho mọi giao dịch sau đó")
  public ResponseEntity<GlobalProviderSettings> updateConfig(@RequestBody ProviderConfigRequest request) {
    if (request.getDelayMs() != null) {
      settings.setDelayMs(Math.max(0, request.getDelayMs()));
    }
    if (request.getFailRate() != null) {
      settings.setFailRate(Math.max(0, Math.min(100, request.getFailRate())));
    }
    return ResponseEntity.ok(settings);
  }
}
