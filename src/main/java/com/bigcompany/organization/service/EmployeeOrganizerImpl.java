package com.bigcompany.organization.service;

import static com.bigcompany.organization.constant.OrganizationConstants.CEO_REPORT_LEVEL;

import com.bigcompany.organization.model.Employee;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The {@link EmployeeOrganizerImpl} is responsible for calculating and getting organizational data.
 * <li>getting the direct subordinates.
 * <li>calculating and getting the employee's reporting line(number of managers between employee and
 *     CEO)
 */
public class EmployeeOrganizerImpl implements EmployeeOrganizer {

  /**
   * Getting the direct subordinates
   *
   * @param employeeMap initial employee map
   * @return managers with direct subordinates.
   */
  public HashMap<Integer, List<Employee>> getDirectSubordinates(
      HashMap<Integer, Employee> employeeMap) {

    if (employeeMap == null) {
      throw new RuntimeException("Employee map shouldn't be null!");
    }

    HashMap<Integer, List<Employee>> managerDirectSubordinates = new HashMap<>();
    for (Employee employee : employeeMap.values()) {

      Integer managerId = employee.getManagerId();
      if (managerId != null) {
        List<Employee> subOrdinates =
            managerDirectSubordinates.getOrDefault(managerId, new ArrayList<>());
        subOrdinates.add(employee);
        managerDirectSubordinates.put(managerId, subOrdinates);
      }
    }
    return managerDirectSubordinates;
  }

  /**
   * Calculating and returning the report line of employee.
   *
   * @param employee employee
   * @param employeeMap initial employee map
   * @param reportingLines pre-calculated employee-reportLine map to improve search performance
   * @return reporting line of employee
   */
  public Integer getReportingLine(
      Employee employee,
      HashMap<Integer, Employee> employeeMap,
      HashMap<Integer, Integer> reportingLines) {
    if (reportingLines.containsKey(employee.getId())) {
      return reportingLines.get(employee.getId());
    }
    if (employee.getManagerId() == null) {
      reportingLines.put(employee.getId(), CEO_REPORT_LEVEL);
      return CEO_REPORT_LEVEL;
    }

    int reportingLine =
        getReportingLine(employeeMap.get(employee.getManagerId()), employeeMap, reportingLines) + 1;
    reportingLines.put(employee.getId(), reportingLine);
    return reportingLine;
  }
}
