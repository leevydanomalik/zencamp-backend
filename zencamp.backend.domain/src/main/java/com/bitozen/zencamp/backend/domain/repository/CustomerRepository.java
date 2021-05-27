/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain.repository;

import com.bitozen.zencamp.backend.domain.Customer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jeremia
 */
@Repository
@Transactional
public interface CustomerRepository extends PagingAndSortingRepository <Customer, Long>{
    
    Optional<Customer> findOneByFullname(String fullname);
    Optional<Customer> findOneByCustomerid(String customerid);
    Page<Customer> findAllByFullname(Pageable pageable, String fullname);
    Page<Customer> findAll(Pageable pageable);
}
