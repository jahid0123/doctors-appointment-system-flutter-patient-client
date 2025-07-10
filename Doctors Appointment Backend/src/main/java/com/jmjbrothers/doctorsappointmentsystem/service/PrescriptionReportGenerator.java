package com.jmjbrothers.doctorsappointmentsystem.service;

import com.jmjbrothers.doctorsappointmentsystem.model.Prescription;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PrescriptionReportGenerator {

    public void generateFromJavaBean(Prescription prescription, String outputPath) throws Exception {
        // Load the Jasper template (from resources)
        InputStream reportStream = getClass().getResourceAsStream("/reports/prescriptions.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Map for fields used directly in $F{} (not part of collection)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SYMPTOMS", prescription.getSymptoms());
        parameters.put("DIAGNOSIS", prescription.getDiagnosis());
        parameters.put("DATE_ISSUED", java.sql.Date.valueOf(prescription.getDateIssued()));
        parameters.put("PATIENT_NAME", prescription.getPatient().getName());
        parameters.put("PATIENT_DATE_OF_BIRTH", java.sql.Date.valueOf(prescription.getPatient().getDob()));
        parameters.put("DOCTOR_NAME", prescription.getDoctor().getName());
        parameters.put("QUALIFICATION", prescription.getDoctor().getQualification());
        parameters.put("SPECIALIZATION", prescription.getDoctor().getSpecialization());
        parameters.put("HOSPITAL_NAME", prescription.getDoctor().getHospitalName());
        parameters.put("PHONE", prescription.getDoctor().getPhone());
        parameters.put("EXPERIENCE", prescription.getDoctor().getExperience());

        // For table (or list of medicines), use a JRBeanCollectionDataSource
        JRBeanCollectionDataSource medicineDataSource = new JRBeanCollectionDataSource(prescription.getMedicines());

        // This assumes you're using a subdataset or table with a dataset
        parameters.put("medicine", medicineDataSource); // Match dataset name in JRXML

        // Generate the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        // Export to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
    }
}
