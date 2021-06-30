package com.bitozen.zencamp.backend.svc.assembler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitozen.zencamp.backend.domain.Game;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.GameRepository;
import com.bitozen.zencamp.backend.dto.GameDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GameDTOAssembler implements IObjectAssembler<Game, GameDTO>{

	@Autowired
	private GameRepository repository;
	
	@Autowired
        private CreationalSpecificationDTOAssembler creationalAssembler;
	
	@Override
	public GameDTO toDTO(Game domainObject) {
            return new GameDTO(
		domainObject.getGameID(),
		domainObject.getGenre(),
                domainObject.getQuantity(),
		domainObject.getGameName(),
                domainObject.getPlatform(),
		domainObject.getGameCreational() == null ? new CreationalSpecificationDTO().getInstance() 
                        : creationalAssembler.toDTO(domainObject.getGameCreational())
            );
	}

	@Override
	public Game toDomain(GameDTO dtoObject) {
            return new Game(
		dtoObject.getGameID(),
		dtoObject.getGenre(),
                dtoObject.getQuantity(),
		dtoObject.getGameName(),
                dtoObject.getPlatform(),
		dtoObject.getGameCreationalDTO() == null ? new CreationalSpecification().getInstance() 
                        : creationalAssembler.toDomain(dtoObject.getGameCreationalDTO())
            );
	}

	@Override
	public List<GameDTO> toDTOs(Set<Game> domainObjects) {
		List<GameDTO> datas = new ArrayList<>();
            domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	@Override
	public List<GameDTO> toDTOs(List<Game> domainObjects) {
		List<GameDTO> datas = new ArrayList<>();
            domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	@Override
	public List<Game> toDomains(List<GameDTO> dtoObjects) {
		return null;
	}

    

	
}
