package com.bigcompany.organization.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.organization.model.Employee;
import com.bigcompany.organization.model.ManagerSalaryDetails;
import com.bigcompany.organization.model.ReportData;
import com.bigcompany.organization.util.TestData;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeReportGeneratorTest {

  EmployeeReportGenerator employeeReportGenerator;
  EmployeeOrganizer mockedEmployeeOrganizer;
  ReportWriter mockedReportWriter;

  HashMap<Integer, Employee> employeeMap;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  void setUp() {
    employeeMap = TestData.getTestEmployeeData();
    HashMap<Integer, List<Employee>> managerDirectSubordinates = new HashMap<>();
    managerDirectSubordinates.put(123, List.of(employeeMap.get(124), employeeMap.get(125)));
    managerDirectSubordinates.put(125, List.of(employeeMap.get(126)));
    managerDirectSubordinates.put(126, List.of(employeeMap.get(127)));
    managerDirectSubordinates.put(127, List.of(employeeMap.get(128)));
    managerDirectSubordinates.put(128, List.of(employeeMap.get(129)));

    mockedEmployeeOrganizer =
        new EmployeeOrganizer() {
          @Override
          public HashMap<Integer, List<Employee>> getDirectSubordinates(
              HashMap<Integer, Employee> employeeMap) {
            return managerDirectSubordinates;
          }

          @Override
          public Integer getReportingLine(
              Employee employee,
              HashMap<Integer, Employee> employeeMap,
              HashMap<Integer, Integer> reportingLines) {
            return 5;
          }
        };
    mockedReportWriter = reportData -> System.out.println("Mocked report writer is called.");
    employeeReportGenerator =
        new EmployeeReportGenerator(mockedEmployeeOrganizer, mockedReportWriter);
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  // whenEmployeeMapIsCorrect_thenGenerateReportData
  void whenEmployeeMapIsCorrect_thenGenerateReportData() {
    // given
    HashMap<Integer, ManagerSalaryDetails> expectedManagerSalaryDetails = new HashMap<>();
    expectedManagerSalaryDetails.put(
        125, new ManagerSalaryDetails(employeeMap.get(125), 45000.00, 4.444444444444445, -7000.0));
    expectedManagerSalaryDetails.put(
        126, new ManagerSalaryDetails(employeeMap.get(126), 50000.0, -10.0, -15000.0));
    expectedManagerSalaryDetails.put(
        127,
        new ManagerSalaryDetails(employeeMap.get(127), 75000.0, -33.333333333333336, -40000.0));
    expectedManagerSalaryDetails.put(
        128, new ManagerSalaryDetails(employeeMap.get(128), 43000.0, 74.418, 10500.0));

    // when
    ReportData reportData = employeeReportGenerator.generateReport(employeeMap);

    // then
    assertNotNull(reportData.getLessEarnerManagers());
    assertNotNull(reportData.getMoreEarnerManagers());
    assertNotNull(reportData.getEmployeesWithLongReportingLine());
    assertEquals(3, reportData.getLessEarnerManagers().size());
    for (ManagerSalaryDetails managerSalaryDetails : reportData.getLessEarnerManagers()) {
      Employee manager = managerSalaryDetails.getManager();
      assertTrue(employeeMap.containsKey(manager.getManagerId()));
      assertEquals(
          expectedManagerSalaryDetails.get(manager.getId()).getAverageSubordinatesSalary(),
          managerSalaryDetails.getAverageSubordinatesSalary());
      assertEquals(
          expectedManagerSalaryDetails.get(manager.getId()).getDiffFromAverageRatio(),
          managerSalaryDetails.getDiffFromAverageRatio());
      assertEquals(
          expectedManagerSalaryDetails.get(manager.getId()).getDiffAmountFromRequired(),
          managerSalaryDetails.getDiffAmountFromRequired());
    }
  }

  @Test
  void whenEmployeeMapIsNull_thenExceptionThrown() {

    // when
    Exception exception =
        assertThrows(RuntimeException.class, () -> employeeReportGenerator.generateReport(null));

    // then
    String expectedMessage = "Employee map shouldn't be null!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
