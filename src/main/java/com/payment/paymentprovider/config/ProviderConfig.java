package com.payment.paymentprovider.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
public class ProviderConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*");
      }
    };
  }

  /**
   * Global config — set qua POST /api/provider/config, áp dụng cho
   * mọi giao dịch sau đó không override riêng. Lưu in-memory (không
   * cần persist DB — đây là config vận hành tạm thời cho demo, mất
   * khi restart là hợp lý, không phải dữ liệu nghiệp vụ).
   */
  @Bean
  public GlobalProviderSettings globalProviderSettings() {
    return new GlobalProviderSettings();
  }

  @Getter
  @Setter
  public static class GlobalProviderSettings {
    private int delayMs = 2000;
    private int failRate = 0; // % — 0 nghĩa là luôn thành công
  }

}
