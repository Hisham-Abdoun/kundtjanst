package org.example.kundtjanst.repository;

import org.example.kundtjanst.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
}
