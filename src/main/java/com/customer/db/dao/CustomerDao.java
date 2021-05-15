package com.customer.db.dao;

import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.openapi.generated.model.Customer;
import com.customer.db.DbOperation;
import com.customer.db.dao.mapper.CustomerMapper;
import com.customer.db.dao.model.CustomerEntity;
import com.customer.db.dao.repository.CustomerRepository;
import com.customer.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author davidjmartin
 */
@Slf4j
@Component
public class CustomerDao implements DbOperation<Customer> {

    @Autowired
    private CustomerMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        log.info("fetching customers.");
        return customerRepository.findAll()
            .stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Customer findById(long id) {
        log.info("fetching customer with id: {}.", id);
        return customerRepository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(
                () -> new ResourceNotFoundException(format("resource with id: %s not found.", id))
            );
    }

    @Override
    public Customer save(Customer entity) {
        log.info("saving customer with lastName: {}.", entity.getLastName());
        final CustomerEntity customerEntity =
            customerRepository.save(mapper.toEntity(entity));
        return mapper.toDto(customerEntity);
    }

    @Override
    public Customer update(Customer entity) {
        log.info("updating customer with id: {}.", entity.getId());
        final CustomerEntity customerEntity =
            customerRepository.save(mapper.toEntity(entity));
        return mapper.toDto(customerEntity);
    }

    @Override
    public void deleteById(long id) {
        log.info("deleting customer with id: {}.", id);
        customerRepository.deleteById(id);
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return customerRepository.existsByEmail(email);
    }

}
