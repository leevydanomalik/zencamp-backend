package com.bitozen.zencamp.backend.svc.report.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.FileResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bitozen.zencamp.backend.common.GenericException;
import com.bitozen.zencamp.backend.common.MessageCode;
import com.bitozen.zencamp.backend.common.type.FileExtention;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

@Service
public class ReportService extends AbstractReportService<JasperReport> {

    private ClassLoader classLoader = getClass().getClassLoader();
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);
    private String rptResourcePrefix = null;
    @Autowired
    private DataSource reportDataSource;

    protected final FileResolver FILE_RESOLVER = new FileResolver() {
        @Override
        public File resolveFile(String fileName) {
            try {
                return ResourceUtils.getFile(rptResourcePrefix + fileName);
            } catch (FileNotFoundException e) {
                return null;
            }
        }

    };

    @Override
    public OutputStream showReportBeanDataSource(String jasperFile, Map<String, Object> params, Collection<?> collection) throws GenericException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        showReportBeanDataSource(jasperFile, params, collection, out);
        return out;
    }

    @Override
    public OutputStream showReportJdbcDataSource(String jasperFile, Map<String, Object> params, Connection connection) throws GenericException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        showReportJdbcDataSource(jasperFile, params, connection, output);
        return output;
    }

    @Override
    public void showReportBeanDataSource(String jasperFile, Map<String, Object> params, Collection<?> collection, OutputStream suppliedStream) throws GenericException {
        InputStream in = null;
        try {
            in = ResourceUtils.getURL(this.rptResourcePrefix + jasperFile).openStream();
            if (collection != null) {
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put("REPORT_FILE_RESOLVER", this.FILE_RESOLVER);
                JRDataSource dataSource = new JRBeanCollectionDataSource(collection);
                JasperRunManager.runReportToPdfStream(in, suppliedStream, params, dataSource);
            }
        } catch (IOException | JRException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportBeanDataSource", true), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    @Override
    public void showReportJdbcDataSource(String jasperFile, Map<String, Object> params, Connection connection, OutputStream suppliedStream) throws GenericException {
        InputStream in = null;
        try {
            in = ResourceUtils.getURL(this.rptResourcePrefix + jasperFile).openStream();
            if (connection != null) {
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put("REPORT_FILE_RESOLVER", this.FILE_RESOLVER);
                JasperRunManager.runReportToPdfStream(in, suppliedStream, params, connection);
            }
        } catch (IOException | JRException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    @Override
    public OutputStream showReportJdbcDataSource(String jasperFile, Map<String, Object> params) throws GenericException {
        try {
            return showReportJdbcDataSource(jasperFile, params, this.reportDataSource.getConnection());
        } catch (GenericException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        }
    }

    @Override
    public void showReportJdbcDataSource(String jasperFile, Map<String, Object> params, OutputStream suppliedStream) throws GenericException {
        try {
            showReportJdbcDataSource(jasperFile, params, this.reportDataSource.getConnection(), suppliedStream);
        } catch (GenericException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        }
    }

    @Override
    public void showReport(String templateFile, Object reportParamsBean, Collection<?> beansCollectionDS, OutputStream rptOutput) throws GenericException {
        showReportBeanDataSource(templateFile, convertToParamMap(reportParamsBean), beansCollectionDS, rptOutput);
    }

    @Override
    public void showReport(String templateFile, Object reportParamsBean, OutputStream rptOutput) throws GenericException {
        showReportJdbcDataSource(templateFile, convertToParamMap(reportParamsBean), rptOutput);
    }

    @Override
    public OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params) throws GenericException {
        try {
            return showReportJdbcDataSourceExportToPdfTxtCsvXls(format, jasperFile, params, this.reportDataSource.getConnection());
        } catch (GenericException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        }
    }

    @Override
    public OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, List<String> jasperFiles, Map<String, Object> params) throws GenericException {
        try {
            return showReportJdbcDataSourceExportToPdfTxtCsvXls(format, jasperFiles, params, this.reportDataSource.getConnection());
        } catch (GenericException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        }
    }

    @Override
    public OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params, Connection connection) throws GenericException {
        InputStream in = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            /*solve Docker deployement 20-04-2018*/
//            in = classLoader.getResourceAsStream(this.rptResourcePrefix + jasperFile);
            try {
                in = new ClassPathResource(this.rptResourcePrefix + jasperFile).getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put("REPORT_FILE_RESOLVER", this.FILE_RESOLVER);
                try {
                    params.put("SUBREPORT_DIR", new ClassPathResource(rptResourcePrefix).getPath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JRExporter exporter = new JRPdfExporter();
                if (format != null) {
                    switch (format) {
                        case XLS:
                            exporter = new JRXlsExporter();
                            break;
                        case CSV:
                            exporter = new JRCsvExporter();
                            break;
                        case TXT:
                            exporter = new JRTextExporter();
                            exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 80);
                            exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 40);
                            break;
                        default:
                            break;
                    }
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(in, params, connection);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                exporter.exportReport();
            }
        } catch (JRException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return os;
    }

    @Override
    public OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, List<String> jasperFiles, Map<String, Object> params, Connection connection) throws GenericException {
        InputStream in = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            if (connection != null) {
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put("REPORT_FILE_RESOLVER", this.FILE_RESOLVER);

                JRExporter exporter = new JRPdfExporter();
                if (format != null) {
                    if (format.equals(FileExtention.XLS)) {
                        exporter = new JRXlsExporter();
                    } else if (format.equals(FileExtention.CSV)) {
                        exporter = new JRCsvExporter();
                    } else if (format.equals(FileExtention.TXT)) {
                        exporter = new JRTextExporter();
                        exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 80);
                        exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 40);
                    }
                }

                List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

                for (String jasperFile : jasperFiles) {
                    in = ResourceUtils.getURL(this.rptResourcePrefix + jasperFile).openStream();
                    jasperPrintList.add(JasperFillManager.fillReport(in, params, connection));
                }

                exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
                exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                exporter.exportReport();
            }
        } catch (IOException | JRException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return os;
    }

    /*DI*/
    public void setRptResourcePrefix(String rptResourcePrefix) {
        this.rptResourcePrefix = rptResourcePrefix;
    }

    public void setReportDataSource(DataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
    }

	@Override
	public OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params, JREmptyDataSource jremptyDataSource) throws GenericException {
        try {
            return showReportJdbcDataSourceExportToPdfTxtCsvXls(format, jasperFile, params, jremptyDataSource, this.reportDataSource.getConnection());
        } catch (GenericException e) {
            throw e;
        } catch (SQLException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        }
    
		
	}

	@Override
	public OutputStream showReportJdbcDataSourceExportToPdfTxtCsvXls(FileExtention format, String jasperFile, Map<String, Object> params, JREmptyDataSource jremptyDataSource, Connection connection)
			throws GenericException {
        InputStream in = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            /*solve Docker deployement 20-04-2018*/
//            in = classLoader.getResourceAsStream(this.rptResourcePrefix + jasperFile);
            try {
                in = new ClassPathResource(this.rptResourcePrefix + jasperFile).getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put("REPORT_FILE_RESOLVER", this.FILE_RESOLVER);
                params.put("jdbcConnection", connection);
                try {
                    params.put("SUBREPORT_DIR", new ClassPathResource(rptResourcePrefix).getPath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JRExporter exporter = new JRPdfExporter();
                if (format != null) {
                    switch (format) {
                        case XLS:
                            exporter = new JRXlsExporter();
                            break;
                        case CSV:
                            exporter = new JRCsvExporter();
                            break;
                        case TXT:
                            exporter = new JRTextExporter();
                            exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 80);
                            exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 40);
                            break;
                        default:
                            break;
                    }
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(in, params, jremptyDataSource);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                exporter.exportReport();
            }
        } catch (JRException e) {
            logger.error("Failure", e);
            throw new GenericException(MessageCode.get(IReportService.class, "showReportJdbcDataSource", true), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return os;
    }

}