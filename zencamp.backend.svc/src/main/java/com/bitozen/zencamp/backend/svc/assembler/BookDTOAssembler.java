package com.bitozen.zencamp.backend.svc.assembler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitozen.zencamp.backend.domain.Book;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.BookRepository;
import com.bitozen.zencamp.backend.dto.BookDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BookDTOAssembler implements IObjectAssembler<Book, BookDTO>{

	@Autowired
	private BookRepository repository;
	
	@Autowired
    private CreationalSpecificationDTOAssembler creationalAssembler;
	
	@Override
	public BookDTO toDTO(Book domainObject) {
			return new BookDTO(
					domainObject.getBookID(),
					domainObject.getBookTitle(),
					domainObject.getBookGenre(),
					domainObject.getReleaseDate(),
					domainObject.getBookNotes(),
					domainObject.getBookCreational() == null ? new CreationalSpecificationDTO().getInstance() : creationalAssembler.toDTO(domainObject.getBookCreational())
					);
	}

	@Override
	public Book toDomain(BookDTO dtoObject) {
		// TODO Auto-generated method stub
		return new Book(
				dtoObject.getBookID(),
				dtoObject.getBookTitle(),
				dtoObject.getBookGenre(),
				dtoObject.getReleaseDate(),
				dtoObject.getBookNotes(),
				dtoObject.getBookCreationalDTO() == null ? new CreationalSpecification().getInstance() : creationalAssembler.toDomain(dtoObject.getBookCreationalDTO())
				);
	}

	@Override
	public List<BookDTO> toDTOs(Set<Book> domainObjects) {
		List<BookDTO> datas = new ArrayList<>();
        domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	@Override
	public List<BookDTO> toDTOs(List<Book> domainObjects) {
		List<BookDTO> datas = new ArrayList<>();
        domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	@Override
	public List<Book> toDomains(List<BookDTO> dtoObjects) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
