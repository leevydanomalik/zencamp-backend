package com.bitozen.zencamp.backend.svc.report.service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bitozen.zencamp.backend.common.GenericException;
import com.bitozen.zencamp.backend.common.type.FileExtention;

public interface IReportService {

    public abstract OutputStream showReportBeanDataSource(
            String templateFile, Map<String, Object> params,
            Collection<?> collection) throws GenericException;

    public abstract OutputStream showReportJdbcDataSource(
            String jasperFile, Map<String, Object> params, Connection connection)
            throws GenericException;

    public abstract OutputStream showReportJdbcDataSource(String templateFile, Map<String, Object> params)
            throws GenericException;

    public abstract void showReportBeanDataSource(
            String templateFile, Map<String, Object> params,
            Collection<?> collection, OutputStream suppliedStream) throws GenericException;

    public abstract void showReport(
            String templateFile, Object reportParamsBean,
            Collection<?> beansCollectionDS, OutputStream rptOutput) throws GenericException;

    public abstract void showReportJdbcDataSource(
            String templateFile, Map<String, Object> params, Connection connection,
            OutputStream rptOutput)
            throws GenericException;

    public abstract void showReportJdbcDataSource(
            String templateFile, Map<String, Object> params,
            OutputStream suppliedStream)
            throws GenericException;

    public abstract void showReport(
            String templateFile, Object reportParamsBean,
            OutputStream rptOutput)
            throws GenericException;

    public abstract OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params) throws GenericException;
    
    public abstract OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, List<String> jasperFiles, Map<String, Object> params) throws GenericException;

    public abstract OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params, Connection connection) throws GenericException;
    
    public abstract OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, List<String> jasperFiles, Map<String, Object> params, Connection connection) throws GenericException;

    public abstract OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params, JREmptyDataSource jremptyDataSource) throws GenericException;
    
    public abstract OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params, JREmptyDataSource jremptyDataSource, Connection connection) throws GenericException;
    
    void flushTemplateCache();

}
