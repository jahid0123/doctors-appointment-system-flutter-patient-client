package com.jmjbrothers.doctorsappointmentsystem.service;

import net.sf.jasperreports.engine.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class JasperReportService {

    private final DataSource dataSource;

    public JasperReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generatePrescriptionReport(Long prescriptionId, String outputFilePath) throws Exception {
        // Load and compile the JRXML template
        InputStream reportStream = getClass().getResourceAsStream("/reports/final_prescription_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Parameters map
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PRESCRIPTION_ID", prescriptionId); // Assuming your JRXML uses this param

        // Database connection
        try (Connection conn = dataSource.getConnection()) {
            // Fill the report using SQL query inside the jrxml
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            // Export to PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilePath);
        }
    }
}
