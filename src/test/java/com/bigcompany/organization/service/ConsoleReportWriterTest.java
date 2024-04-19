package com.bigcompany.organization.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.organization.model.Employee;
import com.bigcompany.organization.model.EmployeeReportingLine;
import com.bigcompany.organization.model.ManagerSalaryDetails;
import com.bigcompany.organization.model.ReportData;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsoleReportWriterTest {

  ConsoleReportWriter consoleReportWriter;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  public static final double MIN_SALARY_THRESHOLD_RATIO = 0.2;
  public static final double MAX_SALARY_THRESHOLD_RATIO = 0.5;
  public static final int REPORT_LINE_THRESHOLD = 4;

  @BeforeEach
  void setUp() {
    consoleReportWriter = new ConsoleReportWriter();
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  void write() {
    // given
    ReportData reportData = getTestReportData();

    // when
    consoleReportWriter.write(reportData);

    // then
    assertTrue(
        outContent
            .toString()
            .contains(
                "--Manager list which earn less than %,.2f%% the average salary of its direct subordinates--"
                    .formatted(MIN_SALARY_THRESHOLD_RATIO * 100)));
    assertTrue(
        outContent
            .toString()
            .contains("125, Bob, Ronstad, 47,000.00, 45,000.00, 4.44%, -7,000.00"));
    assertTrue(
        outContent
            .toString()
            .contains("126, Richard, Doe, 45,000.00, 50,000.00, -10.00%, -15,000.00"));
    assertTrue(
        outContent
            .toString()
            .contains("127, Lucy, Smith, 50,000.00, 75,000.00, -33.33%, -40,000.00"));

    assertTrue(
        outContent
            .toString()
            .contains(
                "--Manager list which earn more than %,.2f%% the average salary of its direct subordinates--"
                    .formatted(MAX_SALARY_THRESHOLD_RATIO * 100)));
    assertTrue(
        outContent
            .toString()
            .contains("128, Paul, Edwards, 75,000.00, 43,000.00, 74.42%, 10,500.00"));

    assertTrue(
        outContent
            .toString()
            .contains(
                "--Employees which have more than %d managers between them and the CEO--"
                    .formatted(REPORT_LINE_THRESHOLD)));
    assertTrue(outContent.toString().contains("129, Caroline, Knight, 5"));
  }

  private ReportData getTestReportData() {
    List<ManagerSalaryDetails> lessEarnerManagers = new ArrayList<>();
    List<ManagerSalaryDetails> moreEarnerManagers = new ArrayList<>();
    List<EmployeeReportingLine> employeesWithLongReportingLine = new ArrayList<>();

    Employee employeeBob = new Employee(125, "Bob", "Ronstad", 47000, 123);
    Employee employeeRichard = new Employee(126, "Richard", "Doe", 45000, 125);
    Employee employeeLucy = new Employee(127, "Lucy", "Smith", 50000, 126);
    lessEarnerManagers.add(new ManagerSalaryDetails(employeeBob, 45000.0, 4.44, -7000.0));
    lessEarnerManagers.add(new ManagerSalaryDetails(employeeRichard, 50000.0, -10.0, -15000.0));
    lessEarnerManagers.add(new ManagerSalaryDetails(employeeLucy, 75000.0, -33.33, -40000.0));

    Employee employeePaul = new Employee(128, "Paul", "Edwards", 75000, 127);
    moreEarnerManagers.add(new ManagerSalaryDetails(employeePaul, 43000.0, 74.418, 10500.0));

    Employee employeeCaroline = new Employee(129, "Caroline", "Knight", 43000, 128);
    employeesWithLongReportingLine.add(new EmployeeReportingLine(employeeCaroline, 5));

    return new ReportData(lessEarnerManagers, moreEarnerManagers, employeesWithLongReportingLine);
  }
}
