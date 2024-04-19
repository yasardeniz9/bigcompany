package com.bigcompany.organization.service;

import com.bigcompany.organization.model.Employee;
import java.util.HashMap;
import java.util.List;

/**
 * The {@link EmployeeOrganizer} is defining a contract for calculating and getting organizational
 * data.
 * <li>getting the direct subordinates.
 * <li>calculating and getting the employee's reporting line(number of managers between employee and
 *     CEO)
 */
public interface EmployeeOrganizer {

  HashMap<Integer, List<Employee>> getDirectSubordinates(HashMap<Integer, Employee> employeeMap);

  Integer getReportingLine(
      Employee employee,
      HashMap<Integer, Employee> employeeMap,
      HashMap<Integer, Integer> reportingLines);
}
