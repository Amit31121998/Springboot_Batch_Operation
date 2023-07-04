package com.amit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amit.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer>{

}
