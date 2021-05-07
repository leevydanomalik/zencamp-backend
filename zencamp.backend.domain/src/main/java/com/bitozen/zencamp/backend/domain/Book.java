package com.bitozen.zencamp.backend.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.bitozen.zencamp.backend.common.BookGenre;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;

@Entity
@Table(name = "zencamp_book")
public class Book implements Serializable{

	@Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id")
    private Long id;
	
	private String bookID;
	private String bookTitle;
	private BookGenre bookGenre;
	@Temporal(javax.persistence.TemporalType.DATE)
    private Date releaseDate;
	
	@Type(type = "text")
    private String bookNotes;
	
	@Embedded
    private CreationalSpecification bookCreational;
	
	public Book() {
		
	}

	public Book(String bookID, String bookTitle, BookGenre bookGenre, Date releaseDate, String bookNotes,
			CreationalSpecification bookCreational) {
		this.bookID = bookID;
		this.bookTitle = bookTitle;
		this.bookGenre = bookGenre;
		this.releaseDate = releaseDate;
		this.bookNotes = bookNotes;
		this.bookCreational = bookCreational;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookID() {
		return bookID;
	}

	public void setBookID(String bookID) {
		this.bookID = bookID;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public BookGenre getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(BookGenre bookGenre) {
		this.bookGenre = bookGenre;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getBookNotes() {
		return bookNotes;
	}

	public void setBookNotes(String bookNotes) {
		this.bookNotes = bookNotes;
	}

	public CreationalSpecification getBookCreational() {
		return bookCreational;
	}

	public void setBookCreational(CreationalSpecification bookCreational) {
		this.bookCreational = bookCreational;
	}
}
