/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain;

import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import static org.joda.time.format.ISODateTimeFormat.date;

/**
 *
 * @author Dumayangsari
 */

@Entity
@Table (name = "zencamp_customer")
public class OrderCustomer implements Serializable {

    @Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id")
    
    private Long id;
    private String customerID;
    private String customerName;
    private String noTelp;
    private String alamatAsal;
    private String alamatTujuan;
    private String materialKIMAP;
    private String materialName;
    private String materialDesc;
    private String materialType;
    private String materialUoM;
    private int materialPrice;
    private int orderQty;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date orderDate;
    
    @Embedded
    private CreationalSpecification customerCreational;
    
    public OrderCustomer(){
        
    }

    public OrderCustomer(String customerID, String customerName, String noTelp, String alamatAsal, 
            String alamatTujuan, String materialKIMAP, String materialName, String materialDesc, 
            String materialType, String materialUoM, int materialPrice, 
            int orderQty, Date orderDate, CreationalSpecification customerCreational){
        this.customerID = customerID;
        this.customerName = customerName;
        this.noTelp = noTelp;
        this.alamatAsal = alamatAsal;
        this.alamatTujuan = alamatTujuan;
        this.materialKIMAP = materialKIMAP;
        this.materialName = materialName;
        this.materialDesc = materialDesc;
        this.materialType = materialType;
        this.materialUoM = materialUoM;
        this.materialPrice = materialPrice;
        this.orderQty = orderQty;
        this.orderDate = orderDate;
        this.customerCreational = customerCreational;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getAlamatAsal() {
        return alamatAsal;
    }

    public void setAlamatAsal(String alamatAsal) {
        this.alamatAsal = alamatAsal;
    }

    public String getAlamatTujuan() {
        return alamatTujuan;
    }

    public void setAlamatTujuan(String alamatTujuan) {
        this.alamatTujuan = alamatTujuan;
    }

    public String getMaterialKIMAP() {
        return materialKIMAP;
    }

    public void setMaterialKIMAP(String materialKIMAP) {
        this.materialKIMAP = materialKIMAP;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public CreationalSpecification getCustomerCreational() {
        return customerCreational;
    }

    public void setCustomerCreational(CreationalSpecification customerCreational) {
        this.customerCreational = customerCreational;
    }
    
    
}
