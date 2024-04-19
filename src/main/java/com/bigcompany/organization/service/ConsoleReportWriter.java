package com.bigcompany.organization.service;

import static com.bigcompany.organization.constant.OrganizationConstants.*;

import com.bigcompany.organization.model.ReportData;

/** The {@link ConsoleReportWriter} is responsible to print the report to a console. */
public class ConsoleReportWriter implements ReportWriter {

  @Override
  public void write(ReportData reportData) {
    displayManagersWithLessEarning(reportData);
    displayManagersWithMoreEarning(reportData);
    displayLongReportingLines(reportData);
  }

  /**
   * Display managers earn less than they should, and by how much
   *
   * @param reportData report information
   */
  private void displayManagersWithLessEarning(ReportData reportData) {
    //
    StringBuilder result = new StringBuilder();
    result
        .append(
            "--Manager list which earn less than %,.2f%% the average salary of its direct subordinates--"
                .formatted(MIN_SALARY_THRESHOLD_RATIO * 100))
        .append(System.lineSeparator())
        .append(
            "Manager Id, First Name, Last Name, Salary, Average Sub Ordinates Salary, Diff From Average(Ratio), How Much Less Earning Than Required(Amount)")
        .append(System.lineSeparator());
    reportData
        .getLessEarnerManagers()
        .forEach(
            (managerSalaryDetails) ->
                result
                    .append(
                        String.format(
                            "%d, %s, %s, %,.2f, %,.2f, %,.2f%%, %,.2f",
                            managerSalaryDetails.getManager().getId(),
                            managerSalaryDetails.getManager().getFirstName(),
                            managerSalaryDetails.getManager().getLastName(),
                            managerSalaryDetails.getManager().getSalary(),
                            managerSalaryDetails.getAverageSubordinatesSalary(),
                            managerSalaryDetails.getDiffFromAverageRatio(),
                            managerSalaryDetails.getDiffAmountFromRequired()))
                    .append(System.lineSeparator()));
    System.out.println(result);
  }

  /**
   * Display managers earn less than they should, and by how much
   *
   * @param reportData report information
   */
  private void displayManagersWithMoreEarning(ReportData reportData) {
    StringBuilder result = new StringBuilder();
    result
        .append(
            "--Manager list which earn more than %,.2f%% the average salary of its direct subordinates--"
                .formatted(MAX_SALARY_THRESHOLD_RATIO * 100))
        .append(System.lineSeparator())
        .append(
            "Manager Id, First Name, Last Name, Salary, Average Sub Ordinates Salary, Diff From Average(Ratio), How Much More Earning Than Required(Amount)")
        .append(System.lineSeparator());
    reportData
        .getMoreEarnerManagers()
        .forEach(
            (managerSalaryDetails) ->
                result
                    .append(
                        String.format(
                            "%d, %s, %s, %,.2f, %,.2f, %,.2f%%, %,.2f",
                            managerSalaryDetails.getManager().getId(),
                            managerSalaryDetails.getManager().getFirstName(),
                            managerSalaryDetails.getManager().getLastName(),
                            managerSalaryDetails.getManager().getSalary(),
                            managerSalaryDetails.getAverageSubordinatesSalary(),
                            managerSalaryDetails.getDiffFromAverageRatio(),
                            managerSalaryDetails.getDiffAmountFromRequired()))
                    .append(System.lineSeparator()));
    System.out.println(result);
  }

  /**
   * Display employees with too long reporting line
   *
   * @param reportData report information
   */
  private void displayLongReportingLines(ReportData reportData) {
    StringBuilder result = new StringBuilder();
    result
        .append(
            "--Employees which have more than %d managers between them and the CEO--"
                .formatted(REPORT_LINE_THRESHOLD))
        .append(System.lineSeparator())
        .append("Employer Id, First Name, Last Name, Reporting Line Until CEO")
        .append(System.lineSeparator());
    reportData
        .getEmployeesWithLongReportingLine()
        .forEach(
            (employeeReportingLine) ->
                result
                    .append(
                        "%d, %s, %s, %d"
                            .formatted(
                                employeeReportingLine.getEmployee().getId(),
                                employeeReportingLine.getEmployee().getFirstName(),
                                employeeReportingLine.getEmployee().getLastName(),
                                employeeReportingLine.getReportingLine()))
                    .append(System.lineSeparator()));
    System.out.println(result);
  }
}
