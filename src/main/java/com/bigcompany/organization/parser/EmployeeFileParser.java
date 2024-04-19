package com.bigcompany.organization.parser;

import com.bigcompany.organization.exception.FileParsingError;
import com.bigcompany.organization.model.Employee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EmployeeFileParser {
  private static final String COMA_DELIMITER = ",";
  private static final String EXCEPTED_HEADER = "Id,firstName,lastName,salary,managerId";

  public static HashMap<Integer, Employee> getEmployees(String fileName) {
    HashMap<Integer, Employee> employeeMap = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      int lineNumber = 1;
      String line = br.readLine();
      if (line != null) {
        checkHeader(line);
        while ((line = br.readLine()) != null) {
          lineNumber++;
          Employee employee = buildEmployee(line, lineNumber);

          employeeMap.put(employee.getId(), employee);
        }
      }
      return employeeMap;
    } catch (IOException e) {
      throw new RuntimeException("Employee file couldn't be read!", e);
    }
  }

  private static Employee buildEmployee(String line, int lineNumber) {
    String[] columns = line.split(COMA_DELIMITER);
    Integer managerId;
    if (columns.length == 5) {
      managerId = Integer.parseInt(columns[4]);
    } else if (columns.length == 4) {
      managerId = null;
    } else {
      throw new FileParsingError(
          "Employee file is not structured correctly. Error in line number: " + lineNumber);
    }
    return new Employee(
        Integer.parseInt(columns[0]),
        columns[1],
        columns[2],
        Double.parseDouble(columns[3]),
        managerId);
  }

  private static void checkHeader(String header) {
    String[] headerFields = header.split(COMA_DELIMITER);
    String[] expectedHeaderFields = EXCEPTED_HEADER.split(COMA_DELIMITER);
    for (int i = 0; i < headerFields.length; i++) {
      if (!headerFields[i].equalsIgnoreCase(expectedHeaderFields[i])) {
        throw new FileParsingError(
            "The header of file is not as expected! The header and data values should be in the following order: "
                + EXCEPTED_HEADER);
      }
    }
  }
}
