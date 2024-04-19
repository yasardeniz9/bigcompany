package com.bigcompany.organization.model;

import java.util.Objects;

public class Employee {
  private final int id;
  private final String firstName;
  private final String lastName;
  private final double salary;
  private final Integer managerId;

  public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
    this.managerId = managerId;
  }

  public int getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public double getSalary() {
    return salary;
  }

  public Integer getManagerId() {
    return managerId;
  }

  @Override
  public String toString() {
    return "Employee{"
        + "id="
        + id
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", salary="
        + salary
        + ", managerId="
        + managerId
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return id == employee.id
        && Double.compare(employee.salary, salary) == 0
        && firstName.equals(employee.firstName)
        && lastName.equals(employee.lastName)
        && Objects.equals(managerId, employee.managerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
