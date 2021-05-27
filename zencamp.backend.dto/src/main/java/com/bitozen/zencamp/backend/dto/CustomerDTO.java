/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.dto;

import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import java.io.Serializable;
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
public class CustomerDTO implements Serializable {
    private String customerid;
    private String phonenumber;
    private String fullname;
    private String orderto;
    private String orderfrom;
    private CreationalSpecificationDTO customercreationalDTO;
}
