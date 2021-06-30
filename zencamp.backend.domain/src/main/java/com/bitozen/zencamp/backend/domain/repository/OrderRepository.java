/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain.repository;


import com.bitozen.zencamp.backend.domain.OrderCustomer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dumayangsari
 */
@Repository
@Transactional
public interface OrderRepository extends PagingAndSortingRepository<OrderCustomer, Long>{
    Optional<OrderCustomer> findOneByCustomerID(String customerID);
    Page<OrderCustomer> findAll(Pageable pageable);
}
