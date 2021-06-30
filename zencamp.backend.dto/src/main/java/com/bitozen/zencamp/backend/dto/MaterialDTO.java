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
 * @author Dumayangsari
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MaterialDTO implements Serializable {
    private String materialID;
    private String materialName;
    private String materialKIMAP;
    private String materialDesc;
    private String materialType;
    private String materialUoM;
    private int materialPrice;
    private int materialQty;
    private CreationalSpecificationDTO materialCreationalDTO;
}
