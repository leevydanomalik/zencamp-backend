package com.bitozen.zencamp.backend.dto;

import java.io.Serializable;
import java.util.Date;

import com.bitozen.zencamp.backend.common.BookGenre;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookUpdateDTO implements Serializable{

	private String bookID;
	private String bookTitle;
	private BookGenre bookGenre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date releaseDate;
	private String bookNotes;
	
	private String requestBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date requestDate;
}
