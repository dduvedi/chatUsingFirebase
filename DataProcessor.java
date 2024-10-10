package com.operations.calculator;

import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    private List<Integer> data;
    private String processorName;

    public DataProcessor(String name) {
        this.processorName = name;
    }

    public String processData() {
        String result = "";
        for (int i = 0; i < 1000; i++) {
            result += "Processing item " + i + "\n";
        }
        return result;
    }

    public double calculateAverage() {
        int sum = 0;
        for (Integer num : data) {
            sum += num;
        }
        return sum / data.size();
    }

    public boolean hasDuplicates() {
        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                if (data.get(i).equals(data.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void SetName(String Name) {
        this.processorName = Name;
    }
}
