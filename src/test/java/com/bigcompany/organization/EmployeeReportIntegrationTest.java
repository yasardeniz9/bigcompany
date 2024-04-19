package com.bigcompany.organization;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.organization.model.Employee;
import com.bigcompany.organization.parser.EmployeeFileParser;
import com.bigcompany.organization.service.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeReportIntegrationTest {
  private static final String TEST_DIRECTORY = "./src/test/resources/";
  public static final double MIN_SALARY_THRESHOLD_RATIO = 0.2;
  public static final double MAX_SALARY_THRESHOLD_RATIO = 0.5;
  public static final int REPORT_LINE_THRESHOLD = 4;

  EmployeeOrganizer employeeOrganizer;
  ReportWriter reportWriter;
  EmployeeReportGenerator employeeReportGenerator;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  void setUp() {
    employeeOrganizer = new EmployeeOrganizerImpl();
    reportWriter = new ConsoleReportWriter();
    employeeReportGenerator = new EmployeeReportGenerator(employeeOrganizer, reportWriter);
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  public void whenEmployeeFileDataIsCorrect_thenReportData() {
    String testFileName = "employees_test.csv";
    HashMap<Integer, Employee> employeeMap =
        EmployeeFileParser.getEmployees(TEST_DIRECTORY + testFileName);
    employeeReportGenerator.generateReport(employeeMap);

    assertTrue(
        outContent
            .toString()
            .contains(
                "--Manager list which earn less than %,.2f%% the average salary of its direct subordinates--"
                    .formatted(MIN_SALARY_THRESHOLD_RATIO * 100)));

    assertAll(
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("130, Harry, Longman, 62,000.00, 56,666.67, 9.41%, -6,000.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("134, Jake, Shortman, 37,000.00, 87,000.00, -57.47%, -67,400.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("135, Fred, Nina, 68,000.00, 72,000.00, -5.56%, -18,400.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("136, Elizabeth, Nouman, 48,000.00, 74,000.00, -35.14%, -40,800.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("138, Nancy, Clement, 84,000.00, 91,000.00, -7.69%, -25,200.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("140, Julian, Rob, 39,000.00, 47,000.00, -17.02%, -17,400.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("124, Martin, Chekov, 45,000.00, 62,500.00, -28.00%, -30,000.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("125, Bob, Ronstad, 47,000.00, 59,500.00, -21.01%, -24,400.00")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("127, Lucy, Smith, 50,000.00, 45,000.00, 11.11%, -4,000.00")));

    assertTrue(
        outContent
            .toString()
            .contains(
                "--Manager list which earn more than %,.2f%% the average salary of its direct subordinates--"
                    .formatted(MAX_SALARY_THRESHOLD_RATIO * 100)));
    assertTrue(
        outContent.toString().contains("139, April, Soft, 91,000.00, 60,000.00, 51.67%, 1,000.00"));

    assertTrue(
        outContent
            .toString()
            .contains(
                "--Employees which have more than %d managers between them and the CEO--"
                    .formatted(REPORT_LINE_THRESHOLD)));

    assertAll(
        () -> assertTrue(outContent.toString().contains("139, April, Soft, 5")),
        () -> assertTrue(outContent.toString().contains("140, Julian, Rob, 6")),
        () -> assertTrue(outContent.toString().contains("145, William, Hunter, 7")),
        () -> assertTrue(outContent.toString().contains("146, Grace, Martin, 6")));
  }

  @Test
  void whenEmployeeFileIsEmpty_thenReportShouldIncludeOnlyHeaders() {
    String testFileName = "EmptyEmployeeFile.csv";
    HashMap<Integer, Employee> employeeMap =
        EmployeeFileParser.getEmployees(TEST_DIRECTORY + testFileName);
    int emptyReportLineCount = 8;

    employeeReportGenerator.generateReport(employeeMap);

    String[] split = outContent.toString().split(System.lineSeparator());
    assertEquals(
        emptyReportLineCount,
        split.length,
        "Empty report can include just header lines and blank line before moving to the next header.");

    assertAll(
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains(
                        "--Manager list which earn less than %,.2f%% the average salary of its direct subordinates--"
                            .formatted(MIN_SALARY_THRESHOLD_RATIO * 100))),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains(
                        "Manager Id, First Name, Last Name, Salary, Average Sub Ordinates Salary, Diff From Average(Ratio), How Much Less Earning Than Required(Amount)")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains(
                        "--Manager list which earn more than %,.2f%% the average salary of its direct subordinates--"
                            .formatted(MAX_SALARY_THRESHOLD_RATIO * 100))),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains(
                        "Manager Id, First Name, Last Name, Salary, Average Sub Ordinates Salary, Diff From Average(Ratio), How Much More Earning Than Required(Amount)")),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains(
                        "--Employees which have more than %d managers between them and the CEO--"
                            .formatted(REPORT_LINE_THRESHOLD))),
        () ->
            assertTrue(
                outContent
                    .toString()
                    .contains("Employer Id, First Name, Last Name, Reporting Line Until CEO")));
  }
}
