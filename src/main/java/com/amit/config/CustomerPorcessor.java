package com.amit.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.amit.entity.Customer;

@Component
public class CustomerPorcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}
}
