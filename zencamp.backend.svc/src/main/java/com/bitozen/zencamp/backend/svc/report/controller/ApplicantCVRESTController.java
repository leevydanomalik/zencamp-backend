package com.bitozen.zencamp.backend.svc.report.controller;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bitozen.zencamp.backend.common.GenericException;
import com.bitozen.zencamp.backend.svc.report.service.ReportService;

@RestController
@RequestMapping("applicant")
public class ApplicantCVRESTController {
    
    private final String CV_JASPER = "CurriculumVitae.jasper";    

    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/curriculum.vitae/{appID}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generateCV(@PathVariable("appID") String appID) {
        Map<String, Object> params = new HashMap<>();
        params.put("appID", appID);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        reportService.setRptResourcePrefix("/report/");
        try {
            os = (ByteArrayOutputStream) reportService.showReportJdbcDataSourceExportToPdfTxtCsvXls(null, CV_JASPER, params);
        } catch (GenericException ex) {
            Logger.getLogger(ApplicantCVRESTController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*validate object*/
        Validate.notNull(os);

        /*get the byte*/
        byte[] datastream = os.toByteArray();

        /*validate object*/
        Validate.notNull(datastream);

        return ResponseEntity.status(HttpStatus.OK).body(datastream);
    }
    
}
