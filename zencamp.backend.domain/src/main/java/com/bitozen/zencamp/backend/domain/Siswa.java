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

import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;

@Entity
@Table(name = "zencamp_siswa")
public class Siswa implements Serializable{

	@Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id")
    private Long id;
	
	private String siswaID;
	private String siswaName;
	@Temporal(javax.persistence.TemporalType.DATE)
    private Date siswaDateOfBirth;
	
	@Embedded
    private CreationalSpecification siswaCreational;

	public Siswa() {
		
	}
	
	public Siswa(String siswaID, String siswaName, Date siswaDateOfBirth, CreationalSpecification siswaCreational) {
		this.siswaID = siswaID;
		this.siswaName = siswaName;
		this.siswaDateOfBirth = siswaDateOfBirth;
		this.siswaCreational = siswaCreational;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiswaID() {
		return siswaID;
	}

	public void setSiswaID(String siswaID) {
		this.siswaID = siswaID;
	}

	public String getSiswaName() {
		return siswaName;
	}

	public void setSiswaName(String siswaName) {
		this.siswaName = siswaName;
	}

	public Date getSiswaDateOfBirth() {
		return siswaDateOfBirth;
	}

	public void setSiswaDateOfBirth(Date siswaDateOfBirth) {
		this.siswaDateOfBirth = siswaDateOfBirth;
	}

	public CreationalSpecification getSiswaCreational() {
		return siswaCreational;
	}

	public void setSiswaCreational(CreationalSpecification siswaCreational) {
		this.siswaCreational = siswaCreational;
	}
}
