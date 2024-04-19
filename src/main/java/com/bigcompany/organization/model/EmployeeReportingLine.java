package com.bigcompany.organization.model;

public class EmployeeReportingLine {
  private final Employee employee;
  private final Integer reportingLine;

  public EmployeeReportingLine(Employee employee, Integer reportingLine) {
    this.employee = employee;
    this.reportingLine = reportingLine;
  }

  public Employee getEmployee() {
    return employee;
  }

  public Integer getReportingLine() {
    return reportingLine;
  }
}
