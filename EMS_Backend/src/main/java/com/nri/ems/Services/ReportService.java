package com.nri.ems.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.nri.ems.DTO.Reports.ReportResponse;
import com.nri.ems.Entities.Department;
import com.nri.ems.Entities.Employee;
import com.nri.ems.Entities.PayGrade;
import com.nri.ems.Entities.Project;
import com.nri.ems.Repositories.DepartmentRepository;
import com.nri.ems.Repositories.EmployeeRepository;
import com.nri.ems.Repositories.ProjectRepository;
import com.nri.ems.Utils.Enums.EmployementStatus;
import com.nri.ems.Utils.Enums.Gender;
import com.nri.ems.Utils.Enums.ProjectStatus;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ReportService {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PayGradeService payGradeService;

    @Autowired
    ProjectService projectService;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public ResponseEntity<ReportResponse> exportBasicReport(String reportFormat, Integer empID)
            throws FileNotFoundException, JRException {

        String userHome = System.getProperty("user.home");

        String downloadsDir;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            downloadsDir = Paths.get(userHome, "Downloads").toString();
        } else {

            downloadsDir = Paths.get(userHome, "Downloads").toString();
        }
        String fileName = "BasicInformationReport." + reportFormat.toLowerCase();
        Path filePath = Paths.get(downloadsDir, fileName);
        Employee user = employeeRepository.findById(empID).get();
        File file = ResourceUtils.getFile("classpath:basic_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        List<Department> departments = departmentService.getAllDepartments();
        List<Project> projects = projectService.getAllProjects();
        List<PayGrade> paygrades = payGradeService.getAllPayGrades();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(departments);
        JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(projects);
        JRBeanCollectionDataSource dataSource3 = new JRBeanCollectionDataSource(paygrades);
        Integer totalemployees = employeeRepository.findTotalEmployees();
        Integer totalprojects = projectRepository.findTotalProjects();
        Integer totaldepartments = employeeService.countDepartment();
        Integer totalpaygrades = employeeService.countPaygrde();
        Integer activeCount = employeeRepository.findCountofActiveEmployees();
        Integer retired_count = employeeRepository.findCountofRetireEmployees();
        Integer layoff_count = employeeRepository.findCountofLayoffEmployees();
        Integer resign_count = employeeRepository.findCountofResignEmployees();
        HashMap<String, Integer> gendermp = employeeService.getGenderData();
        Map<String, Object> params = new HashMap<>();
        params.put("deptSet", dataSource);
        params.put("projectSet", dataSource2);
        params.put("paybandSet", dataSource3);
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("totalWorkforce", totalemployees);
        params.put("male_count", gendermp.get("MALE"));
        params.put("female_count", gendermp.get("FEMALE"));
        params.put("department_count", totaldepartments);
        params.put("project_count", totalprojects);
        params.put("payband_count", totalpaygrades);
        params.put("active_count", activeCount);
        params.put("retired_count", retired_count);
        params.put("resigned_count", resign_count);
        params.put("layoff_count", layoff_count);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint,
                    filePath.toString());
        } else if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint,
                    filePath.toString());

        }

        return ResponseEntity
                .ok(ReportResponse.builder().message("Report Generated in path: " + filePath.toString()).build());

    }

    public ResponseEntity<ReportResponse> exportReport(String reportFormat, Integer empID,
            String statusString, String genderString, String deptName)
            throws FileNotFoundException, JRException {


        List<Employee> employees = null;
        String userHome = System.getProperty("user.home");

        String downloadsDir;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            downloadsDir = Paths.get(userHome, "Downloads").toString();
        } else {

            downloadsDir = Paths.get(userHome, "Downloads").toString();
        }
        String fileName = "EmployeeReport." + reportFormat.toLowerCase();
        Path filePath = Paths.get(downloadsDir, fileName);
        Employee user = employeeRepository.findById(empID).get();
        File file = ResourceUtils.getFile("classpath:employees.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        //

        if (!statusString.isEmpty() && !genderString.isEmpty() && !deptName.isEmpty()) {
            // If status, gender, and department are present, use
            // findByStatusAndGenderAndDepartment
            Department department = departmentRepository.findByName(deptName);
            EmployementStatus status = EmployementStatus.valueOf(statusString);
            Gender gender = Gender.valueOf(genderString);
            employees = employeeRepository.findByStatusAndGenderAndDepartment(status, gender, department);
        }

        else if (!statusString.isEmpty() && !deptName.isEmpty()) {
            Department department = departmentRepository.findByName(deptName);
            EmployementStatus status = EmployementStatus.valueOf(statusString);
            employees = employeeRepository.findByStatusAndDepartment(status, department);
        } else if (!genderString.isEmpty() && !deptName.isEmpty()) {
            Department department = departmentRepository.findByName(deptName);
            Gender gender = Gender.valueOf(genderString);
            employees = employeeRepository.findByGenderAndDepartment(gender, department);
        } else if (!genderString.isEmpty() && !statusString.isEmpty()) {
            Gender gender = Gender.valueOf(genderString);
            EmployementStatus status = EmployementStatus.valueOf(statusString);
            employees = employeeRepository.findByStatusAndGender(status, gender);
        } else if (!statusString.isEmpty()) {
            EmployementStatus status = EmployementStatus.valueOf(statusString);
            employees = employeeRepository.findByStatus(status);
        } else if (!genderString.isEmpty()) {
            Gender gender = Gender.valueOf(genderString);
            employees = employeeRepository.findByGender(gender);
        } else if (!deptName.isEmpty()) {
            Department department = departmentRepository.findByName(deptName);
            employees = employeeRepository.findByDepartment(department);
        } else {
            employees = employeeRepository.findAll();
        }
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        Map<String, Object> params = new HashMap<>();
        params.put("empSet", dataSource);
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint,
                    filePath.toString());
        } else if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint,
                    filePath.toString());

        } else if (reportFormat.equalsIgnoreCase("xlsx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath.toString()));
            exporter.exportReport();
        }

        return ResponseEntity
                .ok(ReportResponse.builder().message("Report Generated in path: " + filePath.toString()).build());

    }

    public ResponseEntity<ReportResponse> exportProReport(String reportFormat, Integer empID,
            String statusString, String deptName)
            throws FileNotFoundException, JRException {
        File file = null;
        List<Project> projects = null;
        Employee user = employeeRepository.findById(empID).get();

        String userHome = System.getProperty("user.home");

        String downloadsDir;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            downloadsDir = Paths.get(userHome, "Downloads").toString();
        } else {

            downloadsDir = Paths.get(userHome, "Downloads").toString();
        }
        String fileName = "ProjectReport." + reportFormat.toLowerCase();
        Path filePath = Paths.get(downloadsDir, fileName);

        file = ResourceUtils.getFile("classpath:projects.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        if (!statusString.isEmpty() && !deptName.isEmpty()) {

            Department department = departmentRepository.findByName(deptName);
            ProjectStatus status = ProjectStatus.valueOf(statusString);
            projects = projectRepository.findByProjectStatusAndDepartment(status, department);
        } else if (!statusString.isEmpty()) {
            ProjectStatus status = ProjectStatus.valueOf(statusString);
            projects = projectRepository.findByProjectStatus(status);
        } else if (!deptName.isEmpty()) {
            Department department = departmentRepository.findByName(deptName);
            projects = projectRepository.findByDepartment(department);
        } else {
            projects = projectRepository.findAll();
        }
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(projects);

        Map<String, Object> params = new HashMap<>();
        params.put("projectSet", dataSource);
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint,
                    filePath.toString());
        } else if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint,
                    filePath.toString());
        } else if (reportFormat.equalsIgnoreCase("xlsx")) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath.toString()));
            exporter.exportReport();
        }

        return ResponseEntity
                .ok(ReportResponse.builder().message("Report Generated in path: " + filePath.toString()).build());
    }
}
