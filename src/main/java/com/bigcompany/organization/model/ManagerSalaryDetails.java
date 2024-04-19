package com.bigcompany.organization.model;

public class ManagerSalaryDetails {
  private final Employee manager;
  private final Double averageSubordinatesSalary;
  private final Double diffFromAverageRatio;
  private final Double diffAmountFromRequired;

  public ManagerSalaryDetails(
      Employee manager,
      Double averageSubordinatesSalary,
      Double diffFromAverageRatio,
      Double diffAmountFromRequired) {
    this.manager = manager;
    this.averageSubordinatesSalary = averageSubordinatesSalary;
    this.diffFromAverageRatio = diffFromAverageRatio;
    this.diffAmountFromRequired = diffAmountFromRequired;
  }

  public Employee getManager() {
    return manager;
  }

  public Double getAverageSubordinatesSalary() {
    return averageSubordinatesSalary;
  }

  public Double getDiffFromAverageRatio() {
    return diffFromAverageRatio;
  }

  public Double getDiffAmountFromRequired() {
    return diffAmountFromRequired;
  }

  @Override
  public String toString() {
    return "ManagerSalaryDetails{"
        + "manager="
        + manager
        + ", averageSubordinatesSalary="
        + averageSubordinatesSalary
        + ", diffAmountRatio="
        + diffFromAverageRatio
        + ", diffAmount="
        + diffAmountFromRequired
        + '}';
  }
}
