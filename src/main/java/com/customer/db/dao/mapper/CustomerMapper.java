package com.customer.db.dao.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.openapi.model.Customer;
import com.customer.db.model.CustomerEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

/**
 * @author davidjmartin
 */
@Slf4j
@Component
public class CustomerMapper {

    @Autowired
    private ModelMapper modelMapper;

    private CustomerMapper() {}

    public Customer toDto(CustomerEntity customerEntity) {
        log.info("mapping customer entity with id: {} to dto.", customerEntity.getId());
        return modelMapper.map(customerEntity, Customer.class);
    }

    public CustomerEntity toEntity(Customer customer) {
        log.info("mapping customer dto with to entity.");
        return modelMapper.map(customer, CustomerEntity.class);
    }

    public List<Customer> toDtos(List<CustomerEntity> customers) {
        log.info("mapping customer entities to dtos.");
        return customers.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

}