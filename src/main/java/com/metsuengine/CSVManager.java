package com.metsuengine;

import java.io.FileWriter;

import com.opencsv.CSVWriter;

public class CSVManager {

    public static void writeLine(String fileName, String[] lines) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
            writer.writeNext(lines);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    } 
}
