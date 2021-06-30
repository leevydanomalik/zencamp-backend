package com.bitozen.zencamp.backend.domain;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;

@Entity
@Table(name = "zencamp_game")
public class Game implements Serializable{

    @Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id") 
    private Long id;
	
	private String gameID;
	private String genre;
	private String quantity;
        private String gameName;
        private String platform;
	
	@Embedded
        private CreationalSpecification gameCreational;
	
	public Game() {
		
	}

	public Game(String gameID, String genre, String quantity, String gameName,String platform, 
			CreationalSpecification gameCreational) {
		this.gameID = gameID;
		this.genre = genre;
		this.quantity = quantity;
                this.gameName = gameName;
                this.platform = platform;
		this.gameCreational = gameCreational;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public CreationalSpecification getGameCreational() {
        return gameCreational;
    }

    public void setGameCreational(CreationalSpecification gameCreational) {
        this.gameCreational = gameCreational;
    }

    
}
