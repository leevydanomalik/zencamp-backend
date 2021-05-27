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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitozen.zencamp.backend.common.GenericException;
import com.bitozen.zencamp.backend.common.type.FileExtention;
import com.bitozen.zencamp.backend.svc.report.service.ReportService;

@RestController
@RequestMapping("applicant")
public class ApplicantCVRESTController {
    
    private final String CV_JASPER = "ZencampBook.jasper";
    private final String CV_REPORT = "zencampreport.jasper";

    @Autowired
    ReportService reportService;

    @ResponseBody
    @RequestMapping(value = "book.csv", method = RequestMethod.GET,
            produces = "text/csv")
    public byte[] generateBookCSV(@RequestParam(value = "reportFormat", required = false) FileExtention reportFormat) throws GenericException {
        Map<String, Object> params = new HashMap<>();
        ByteArrayOutputStream os;
        reportService.setRptResourcePrefix("/report/");
        os = (ByteArrayOutputStream) reportService.showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention.CSV, CV_JASPER, params);

        Validate.notNull(os);

        byte[] datastream = os.toByteArray();

        Validate.notNull(datastream);

        return datastream;
    }
    
    @RequestMapping(value = "/data.loan.offering.letter",
            method = RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<?> generateLoanOfferingLetter( @RequestParam(value = "reportFormat", required = false) FileExtention reportFormat) throws GenericException {
        Map<String, Object> params = new HashMap<>();
     
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        reportService.setRptResourcePrefix("/report/");
        try {
            os = (ByteArrayOutputStream) reportService.showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention.CSV, CV_REPORT, params);
        } catch (GenericException ex) {
            Logger.getLogger(ApplicantCVRESTController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        Validate.notNull(os);

        
        byte[] datastream = os.toByteArray();

        
        Validate.notNull(datastream);

        return ResponseEntity.status(HttpStatus.OK).body(datastream);
    }
}
