package com.jmjbrothers.doctorsappointmentsystem.service;

import com.jmjbrothers.doctorsappointmentsystem.model.Prescription;
import com.jmjbrothers.doctorsappointmentsystem.repository.PrescriptionRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PrescriptionReportService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public byte[] generatePrescriptionReport(Long prescriptionId) throws IOException {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with ID: " + prescriptionId));

        try {
            // Load and compile JRXML file
            File jrxmlFile = ResourceUtils.getFile("classpath:reports/FinalPrescriptionReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());

            // Prepare data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(prescription));

            // Set report parameters (if any)
            Map<String, Object> parameters = new HashMap<>();

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to PDF bytes
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new IOException("Error generating prescription report", e);
        }
    }
}