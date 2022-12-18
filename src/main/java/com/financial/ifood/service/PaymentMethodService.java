package com.financial.ifood.service;

import java.util.List;

import com.financial.ifood.domain.model.PaymentMethod;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentMethodService {
	
	PaymentMethod save(PaymentMethod paymentMethod);
	PaymentMethod update(Long id, PaymentMethod paymentMethod);
	List<PaymentMethod> findAll();
	PaymentMethod findById(Long id);
    void deleteById(Long id);
}
