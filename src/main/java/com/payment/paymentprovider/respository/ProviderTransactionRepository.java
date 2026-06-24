package com.payment.paymentprovider.respository;

import com.payment.paymentprovider.entity.ProviderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderTransactionRepository extends JpaRepository<ProviderTransaction, String> {

}
