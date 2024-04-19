package com.bigcompany.organization.util;

import com.bigcompany.organization.model.Employee;
import java.util.HashMap;

public class TestData {
  public static HashMap<Integer, Employee> getTestEmployeeData() {
    HashMap<Integer, Employee> employeeMap = new HashMap<>();
    Employee employeeJoe = new Employee(123, "Joe", "Doe", 60000, null);
    Employee employeeMartin = new Employee(124, "Martin", "Chekov", 45000, 123);
    Employee employeeBob = new Employee(125, "Bob", "Ronstad", 47000, 123);
    Employee employeeRichard = new Employee(126, "Richard", "Doe", 45000, 125);
    Employee employeeLucy = new Employee(127, "Lucy", "Smith", 50000, 126);
    Employee employeePaul = new Employee(128, "Paul", "Edwards", 75000, 127);
    Employee employeeCaroline = new Employee(129, "Caroline", "Knight", 43000, 128);

    employeeMap.put(123, employeeJoe);
    employeeMap.put(124, employeeMartin);
    employeeMap.put(125, employeeBob);
    employeeMap.put(126, employeeRichard);
    employeeMap.put(127, employeeLucy);
    employeeMap.put(128, employeePaul);
    employeeMap.put(129, employeeCaroline);

    return employeeMap;
  }
}
