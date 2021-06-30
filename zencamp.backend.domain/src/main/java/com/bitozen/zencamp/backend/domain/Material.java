/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain;

import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Dumayangsari
 */
@Entity
@Table(name = "zencamp_material")
public class Material implements Serializable {
    @Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id") 
    private Long id;
    
    private String materialID;
    private String materialName;
    private String materialKIMAP;
    private String materialDesc;
    private String materialType;
    private String materialUoM;
    private int materialPrice;
    private int materialQty;
    
    @Embedded
    private CreationalSpecification materialCreational;
    
    public Material(){
    
    }
    
    public Material(String materialID, String materialName, String materialKIMAP, String materialDesc, String materialType,
            String materialUoM, int materialPrice, int materialQty, CreationalSpecification materialCreational){
        this.materialID = materialID;
        this.materialName = materialName;
        this.materialKIMAP = materialKIMAP;
        this.materialDesc = materialDesc;
        this.materialType = materialType;
        this.materialUoM = materialUoM;
        this.materialPrice = materialPrice;
        this.materialQty = materialQty;
        this.materialCreational = materialCreational;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialKIMAP() {
        return materialKIMAP;
    }

    public void setMaterialKIMAP(String materialKIMAP) {
        this.materialKIMAP = materialKIMAP;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialUoM() {
        return materialUoM;
    }

    public void setMaterialUoM(String materialUoM) {
        this.materialUoM = materialUoM;
    }

    public int getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(int materialPrice) {
        this.materialPrice = materialPrice;
    }

    public int getMaterialQty() {
        return materialQty;
    }

    public void setMaterialQty(int materialQty) {
        this.materialQty = materialQty;
    }

    public CreationalSpecification getMaterialCreational() {
        return materialCreational;
    }

    public void setMaterialCreational(CreationalSpecification materialCreational) {
        this.materialCreational = materialCreational;
    }
    
    
}
