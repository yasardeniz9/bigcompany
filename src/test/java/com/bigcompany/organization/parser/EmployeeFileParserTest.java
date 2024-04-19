package com.bigcompany.organization.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.organization.exception.FileParsingError;
import com.bigcompany.organization.model.Employee;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeFileParserTest {
  private static final String TEST_DIRECTORY = "./src/test/resources/";

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void getEmployees() {}

  @Test
  void whenManagerIdIsNull_thenCreateEmployeeWithoutManagerId() {
    // given
    String testFileName = "CorrectEmployeeFile.csv";
    Employee expectedEmployee = new Employee(123, "Joe", "Doe", 60000.0, null);

    // when
    HashMap<Integer, Employee> result =
        EmployeeFileParser.getEmployees(TEST_DIRECTORY + testFileName);

    // then
    assertEquals(2, result.size());
    assertEquals(expectedEmployee, result.get(123));
  }

  @Test
  // whenManagerIdIsNull_thenCreateEmployeeWithoutManagerId
  void whenManagerIdIsNotNull_thenCreateEmployeeWithManagerId() {
    // given
    String testFileName = "CorrectEmployeeFile.csv";
    Employee expectedEmployee = new Employee(124, "Martin", "Chekov", 45000.0, 123);

    // when
    HashMap<Integer, Employee> result =
        EmployeeFileParser.getEmployees(TEST_DIRECTORY + testFileName);

    // then
    assertEquals(2, result.size());
    assertEquals(expectedEmployee, result.get(124));
  }

  @Test
  void testMissingDataInFile() {
    // given
    String testFileName = "IncorrectEmployeeFile.csv";

    // when
    Exception exception =
        assertThrows(
            FileParsingError.class,
            () -> EmployeeFileParser.getEmployees(TEST_DIRECTORY + testFileName));

    // then
    assertTrue(exception.getMessage().contains("Error in line number: "));
  }

  @Test
  void whenFileCouldntBeRead_thenExceptionThrown() {
    // given
    String wrongFileName = "WrongFile.csv";

    // when
    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> EmployeeFileParser.getEmployees(TEST_DIRECTORY + wrongFileName));

    // then
    String expectedMessage = "Employee file couldn't be read!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void whenHeaderIsNotAsExpected_thenExceptionThrown() {
    // given
    String wrongHeaderFileName = "IncorrectHeaderEmployeeFile.csv";

    // when
    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> EmployeeFileParser.getEmployees(TEST_DIRECTORY + wrongHeaderFileName));

    // then
    String expectedMessage =
        "The header of file is not as expected! The header and data values should be in the following order:";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
