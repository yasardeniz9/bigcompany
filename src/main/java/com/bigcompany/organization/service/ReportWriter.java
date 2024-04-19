package com.bigcompany.organization.service;

import com.bigcompany.organization.model.ReportData;

/**
 * The {@link ReportWriter} is defining a contract for printing the report to a different
 * destinations(console, file, database, etc.).
 */
public interface ReportWriter {
  void write(ReportData reportData);
}
