package com.bigcompany.organization.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.organization.model.Employee;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeOrganizerImplTest {

  private EmployeeOrganizerImpl employeeOrganizer;
  HashMap<Integer, Employee> employeeMap;

  @BeforeEach
  void setUp() {
    employeeOrganizer = new EmployeeOrganizerImpl();
    employeeMap = new HashMap<>();
    employeeMap.put(123, new Employee(123, "Joe", "Doe", 60000, null));
    employeeMap.put(124, new Employee(124, "Martin", "Chekov", 45000, 123));
    employeeMap.put(125, new Employee(125, "Bob", "Ronstad", 47000, 123));
    employeeMap.put(126, new Employee(126, "Richard", "Doe", 45000, 125));
  }

  @AfterEach
  void tearDown() {}

  @Test
  void whenEmployeeMapGiven_thenGetDirectSubordinates() {
    // given
    Employee employeeRichard = employeeMap.get(126);

    // when
    HashMap<Integer, List<Employee>> result = employeeOrganizer.getDirectSubordinates(employeeMap);

    // then
    assertEquals(2, result.get(123).size());
    assertEquals(employeeRichard, result.get(125).get(0));
  }

  @Test
  void whenEmployeeMapIsNull_thenExceptionThrown() {

    // when
    Exception exception =
        assertThrows(RuntimeException.class, () -> employeeOrganizer.getDirectSubordinates(null));

    // then
    String expectedMessage = "Employee map shouldn't be null!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void whenManagerIdIsNull_thenReturnCeoReportLevel() {
    // given
    Employee employeeCeo = employeeMap.get(123);

    // when
    Integer reportingLine =
        employeeOrganizer.getReportingLine(employeeCeo, employeeMap, new HashMap<>());

    // then
    assertEquals(0, reportingLine);
  }

  @Test
  void whenReportingLinesContainsEmployeeLine_thenReturnItDirectly() {
    // given
    Employee employeeMartin = employeeMap.get(124);
    HashMap<Integer, Integer> reportingLines = new HashMap<>();
    reportingLines.put(employeeMartin.getId(), 1);

    // when
    Integer reportingLine =
        employeeOrganizer.getReportingLine(employeeMartin, employeeMap, reportingLines);

    // then
    assertEquals(reportingLines.get(employeeMartin.getId()), reportingLine);
  }

  @Test
  void whenReportingLinesNotContainsEmployeeLine_thenReturnItAfterCalculation() {
    // given
    Employee employeeMartin = employeeMap.get(124);
    HashMap<Integer, Integer> reportingLines = new HashMap<>();
    reportingLines.put(employeeMartin.getId(), 1);

    Employee employeeRichard = employeeMap.get(126);

    // when
    Integer reportingLine =
        employeeOrganizer.getReportingLine(employeeRichard, employeeMap, reportingLines);

    // then
    assertEquals(2, reportingLine);
  }
}
