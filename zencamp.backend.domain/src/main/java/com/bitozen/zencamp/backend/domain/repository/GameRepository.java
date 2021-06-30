package com.bitozen.zencamp.backend.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bitozen.zencamp.backend.domain.Game;

@Repository
@Transactional
public interface GameRepository extends PagingAndSortingRepository<Game, Long>{

	Optional<Game> findOneByGameID(String gameID);
        Page<Game> findAllByGameName(String gameName, Pageable pageable);
	
	Page<Game> findAll(Pageable pageable);

    //public Optional<Game> findByGameName(String gameName, Pageable page);
}
