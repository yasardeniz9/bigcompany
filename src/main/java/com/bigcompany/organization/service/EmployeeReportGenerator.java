package com.bigcompany.organization.service;

import static com.bigcompany.organization.constant.OrganizationConstants.*;

import com.bigcompany.organization.model.Employee;
import com.bigcompany.organization.model.EmployeeReportingLine;
import com.bigcompany.organization.model.ManagerSalaryDetails;
import com.bigcompany.organization.model.ReportData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The {@link EmployeeReportGenerator} is responsible to generate report of employees. It uses
 * {@link EmployeeOrganizerImpl} to prepare direct subordinates and reporting lines for employees.
 *
 * <p>
 *
 * <ul>
 *   It provides below information for report:
 *   <li>which managers earn less than they should, and by how much
 *   <li>which managers earn more than they should, and by how much
 *   <li>which employees have a reporting line which is too long, and by how much
 * </ul>
 */
public class EmployeeReportGenerator {

  private final EmployeeOrganizer employeeOrganizer;
  private final ReportWriter reportWriter;

  public EmployeeReportGenerator(EmployeeOrganizer employeeOrganizer, ReportWriter reportWriter) {
    this.employeeOrganizer = employeeOrganizer;
    this.reportWriter = reportWriter;
  }

  public ReportData generateReport(HashMap<Integer, Employee> employeeMap) {
    if (employeeMap == null) {
      throw new RuntimeException("Employee map shouldn't be null!");
    }
    HashMap<Integer, List<Employee>> directSubordinatesMap =
        employeeOrganizer.getDirectSubordinates(employeeMap);
    ReportData reportData = prepareReportData(employeeMap, directSubordinatesMap);
    reportWriter.write(reportData);
    return reportData;
  }

  private ReportData prepareReportData(
      HashMap<Integer, Employee> employeeMap,
      HashMap<Integer, List<Employee>> directSubordinatesMap) {
    List<ManagerSalaryDetails> lessEarnerManagers = new ArrayList<>();
    List<ManagerSalaryDetails> moreEarnerManagers = new ArrayList<>();
    List<EmployeeReportingLine> employeesWithLongReportingLine = new ArrayList<>();
    HashMap<Integer, Integer> reportingLines = new HashMap<>();

    for (Employee employee : employeeMap.values()) {
      checkManagerSalaryAndAddToRespectiveList(
          directSubordinatesMap, lessEarnerManagers, moreEarnerManagers, employee);

      evaluateReportingLine(employeeMap, employeesWithLongReportingLine, reportingLines, employee);
    }

    return new ReportData(lessEarnerManagers, moreEarnerManagers, employeesWithLongReportingLine);
  }

  private void checkManagerSalaryAndAddToRespectiveList(
      HashMap<Integer, List<Employee>> directSubordinatesMap,
      List<ManagerSalaryDetails> lessEarnerManagers,
      List<ManagerSalaryDetails> moreEarnerManagers,
      Employee employee) {

    List<Employee> directSubordinates =
        directSubordinatesMap.getOrDefault(employee.getId(), new ArrayList<>());

    if (directSubordinates.isEmpty()) {
      return;
    }

    double managerSalary = employee.getSalary();
    double averageSubordinatesSalary =
        directSubordinates.stream().mapToDouble(Employee::getSalary).average().orElse(0.0);
    double minSalaryThreshold =
        averageSubordinatesSalary + averageSubordinatesSalary * MIN_SALARY_THRESHOLD_RATIO;
    double maxSalaryThreshold =
        averageSubordinatesSalary + averageSubordinatesSalary * MAX_SALARY_THRESHOLD_RATIO;
    double diffFromAverageRatio =
        (managerSalary - averageSubordinatesSalary) * 100 / averageSubordinatesSalary;
    if (managerSalary < minSalaryThreshold) {
      double diffAmountFromRequired = managerSalary - minSalaryThreshold;
      lessEarnerManagers.add(
          new ManagerSalaryDetails(
              employee, averageSubordinatesSalary, diffFromAverageRatio, diffAmountFromRequired));
    } else if (managerSalary > maxSalaryThreshold) {
      double diffAmountFromRequired = managerSalary - maxSalaryThreshold;
      moreEarnerManagers.add(
          new ManagerSalaryDetails(
              employee, averageSubordinatesSalary, diffFromAverageRatio, diffAmountFromRequired));
    }
  }

  private void evaluateReportingLine(
      HashMap<Integer, Employee> employeeMap,
      List<EmployeeReportingLine> employeesWithLongReportingLine,
      HashMap<Integer, Integer> reportingLines,
      Employee employee) {

    Integer reportingLine =
        employeeOrganizer.getReportingLine(employee, employeeMap, reportingLines);
    if (reportingLine > REPORT_LINE_THRESHOLD) {
      employeesWithLongReportingLine.add(new EmployeeReportingLine(employee, reportingLine));
    }
  }
}
