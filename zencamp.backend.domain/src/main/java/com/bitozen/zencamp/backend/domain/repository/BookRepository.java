package com.bitozen.zencamp.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bitozen.zencamp.backend.domain.Book;

@Repository
@Transactional
public interface BookRepository extends PagingAndSortingRepository<Book, Long>{

	Optional<Book> findOneByBookID(String bookID);
	
	Page<Book> findAll(Pageable pageable);
}
