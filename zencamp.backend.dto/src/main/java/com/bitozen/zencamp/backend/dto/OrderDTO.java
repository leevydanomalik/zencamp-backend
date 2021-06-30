/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.dto;

import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Dumayangsari
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO implements Serializable{
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;
    private CreationalSpecificationDTO customerCreationalDTO;
}
