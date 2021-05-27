/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jeremia
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerUpdateDTO implements Serializable {
    private String customerid;
    private String phonenumber;
    private String fullname;
    private String orderto;
    private String orderfrom;
    private String requestBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date requestDate;
}
