package com.bigcompany.organization.model;

import java.util.List;

public class ReportData {
  private final List<ManagerSalaryDetails> lessEarnerManagers;
  private final List<ManagerSalaryDetails> moreEarnerManagers;
  private final List<EmployeeReportingLine> employeesWithLongReportingLine;

  public ReportData(
      List<ManagerSalaryDetails> lessEarnerManagers,
      List<ManagerSalaryDetails> moreEarnerManagers,
      List<EmployeeReportingLine> employeesWithLongReportingLine) {
    this.lessEarnerManagers = lessEarnerManagers;
    this.moreEarnerManagers = moreEarnerManagers;
    this.employeesWithLongReportingLine = employeesWithLongReportingLine;
  }

  public List<ManagerSalaryDetails> getLessEarnerManagers() {
    return lessEarnerManagers;
  }

  public List<ManagerSalaryDetails> getMoreEarnerManagers() {
    return moreEarnerManagers;
  }

  public List<EmployeeReportingLine> getEmployeesWithLongReportingLine() {
    return employeesWithLongReportingLine;
  }
}
