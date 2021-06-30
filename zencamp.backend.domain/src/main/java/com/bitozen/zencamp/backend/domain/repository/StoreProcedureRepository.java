/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain.repository;

import com.bitozen.zencamp.backend.domain.Book;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dumayangsari
 */

@Repository
@Transactional
public interface StoreProcedureRepository extends PagingAndSortingRepository<Book, Long>{

    @Query(value = "select * from memoanggaran()", nativeQuery = true)
    //@Query(value = "select * from namastoreprocedure()", nativeQuery = true)
    //List<Object[]> findTrainingHistoryEmployee();
    List<Object[]> findMemoAnggaran();
}
