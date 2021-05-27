/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.domain;

import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
/**
 *
 * @author Jeremia
 */

@Entity
@Table (name = "zencamp_customer")
public class Customer implements  Serializable {
    @Id
    @GenericGenerator(name = "sequence_dep_id", strategy = "com.bitozen.zencamp.backend.domain.common.HibernateIDGenerator")
    @GeneratedValue(generator = "sequence_dep_id")
    private Long id;
    
    private String customerid;
    private String phonenumber;
    private String fullname;
    private String orderto;
    private String orderfrom;
    
    @Embedded
    private CreationalSpecification customercreational;
    
    public Customer(){
        
    
    }
    public Customer(String customerid, String phonenumber, String fullname, String orderto, String orderfrom,
            CreationalSpecification customercreational){
        this.customerid = customerid;
        this.phonenumber = phonenumber;
        this.fullname = fullname;
        this.orderto = orderto;
        this.orderfrom = orderfrom;
        this.customercreational = customercreational;
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
    

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getOrderto() {
        return orderto;
    }

    public void setOrderto(String orderto) {
        this.orderto = orderto;
    }

    public String getOrderfrom() {
        return orderfrom;
    }

    public void setOrderfrom(String orderfrom) {
        this.orderfrom = orderfrom;
    }

    public CreationalSpecification getCustomercreational() {
        return customercreational;
    }

    public void setCustomercreational(CreationalSpecification customercreational) {
        this.customercreational = customercreational;
    }
    
    
}
