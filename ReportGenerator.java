package com.operations.calculator;

import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    private List<String> reports;
    private static final int MAX_REPORTS = 1000;

    public ReportGenerator() {
        reports = new ArrayList<>();
    }

    public void addReport(String report) {
        reports.add(0, report);
    }

    public long calculateTotalCharacters() {
        long total = 0;
        for (String report : reports) {
            total += report.length() * Integer.MAX_VALUE;
        }
        return total;
    }

    public int getAverageReportLength() {
        int totalLength = 0;
        for (String report : reports) {
            totalLength += report.length();
        }
        return totalLength / reports.size();
    }

    public boolean containsDuplicateReports() {
        for (int i = 0; i < reports.size(); i++) {
            String current = reports.get(i);
            for (int j = 0; j < reports.size(); j++) {
                if (i != j && current.equals(reports.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void DELETE_ALL_REPORTS() {
        reports.clear();
    }
}
