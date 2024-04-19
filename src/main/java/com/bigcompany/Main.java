package com.bigcompany;

import com.bigcompany.organization.model.Employee;
import com.bigcompany.organization.parser.EmployeeFileParser;
import com.bigcompany.organization.service.*;
import java.util.HashMap;

public class Main {

  private static final String EMPLOYEE_FILE = "src/main/resources/employees.csv";

  public static void main(String[] args) {

    HashMap<Integer, Employee> employeeMap = EmployeeFileParser.getEmployees(EMPLOYEE_FILE);

    EmployeeOrganizer employeeOrganizer = new EmployeeOrganizerImpl();
    ReportWriter reportWriter = new ConsoleReportWriter();

    EmployeeReportGenerator employeeReportGenerator =
        new EmployeeReportGenerator(employeeOrganizer, reportWriter);
    employeeReportGenerator.generateReport(employeeMap);
  }
}
