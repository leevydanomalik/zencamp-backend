/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain.repository;

import com.bitozen.zencamp.backend.domain.Game;
import com.bitozen.zencamp.backend.domain.Material;
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
public interface MaterialRepository extends PagingAndSortingRepository<Material, Long>{

	Optional<Material> findOneByMaterialID(String materialID);
        Page<Material> findAllByMaterialName(String materialName, Pageable pageable);
	
	Page<Material> findAll(Pageable pageable);

}

