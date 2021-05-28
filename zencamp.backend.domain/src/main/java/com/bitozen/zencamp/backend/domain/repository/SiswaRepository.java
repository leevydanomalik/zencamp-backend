package com.bitozen.zencamp.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bitozen.zencamp.backend.domain.Siswa;

@Repository
@Transactional
public interface SiswaRepository extends PagingAndSortingRepository<Siswa, Long>{

	Optional<Siswa> findOneBySiswaID(String siswaID);
}
