package com.bitozen.zencamp.backend.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;

@Entity
@Table(name = "zencamp_kelas")
public class Kelas implements Serializable{

	@Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id")
    private Long id;
	
	private String kelasID;
	private String kelasName;
	private String kelasDescription;
	private String kelasYear;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "kelasID", referencedColumnName = "kelasID")
	private Set<Siswa> listSiswa;
	
	@Embedded
    private CreationalSpecification kelasCreational;

	public Kelas() {
		
	}
	
	public Kelas(String kelasID, String kelasName, String kelasDescription, String kelasYear, Set<Siswa> listSiswa,
			CreationalSpecification kelasCreational) {
		this.kelasID = kelasID;
		this.kelasName = kelasName;
		this.kelasDescription = kelasDescription;
		this.kelasYear = kelasYear;
		this.listSiswa = listSiswa;
		this.kelasCreational = kelasCreational;
	}

	public Kelas(String kelasID, String kelasName, String kelasDescription, String kelasYear,
			CreationalSpecification kelasCreational) {
		this.kelasID = kelasID;
		this.kelasName = kelasName;
		this.kelasDescription = kelasDescription;
		this.kelasYear = kelasYear;
		this.kelasCreational = kelasCreational;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKelasID() {
		return kelasID;
	}

	public void setKelasID(String kelasID) {
		this.kelasID = kelasID;
	}

	public String getKelasName() {
		return kelasName;
	}

	public void setKelasName(String kelasName) {
		this.kelasName = kelasName;
	}

	public String getKelasDescription() {
		return kelasDescription;
	}

	public void setKelasDescription(String kelasDescription) {
		this.kelasDescription = kelasDescription;
	}

	public String getKelasYear() {
		return kelasYear;
	}

	public void setKelasYear(String kelasYear) {
		this.kelasYear = kelasYear;
	}

	public Set<Siswa> getListSiswa() {
		return listSiswa;
	}

	public void setListSiswa(Set<Siswa> listSiswa) {
		this.listSiswa = listSiswa;
	}

	public CreationalSpecification getKelasCreational() {
		return kelasCreational;
	}

	public void setKelasCreational(CreationalSpecification kelasCreational) {
		this.kelasCreational = kelasCreational;
	}
	
	
}
