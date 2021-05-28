package com.bitozen.zencamp.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bitozen.zencamp.backend.domain.Kelas;

@Repository
@Transactional
public interface KelasRepository extends PagingAndSortingRepository<Kelas, Long> {

	Optional<Kelas> findOneByKelasID(String kelasID);
	
	Page<Kelas> findAll(Pageable pageable);
}
