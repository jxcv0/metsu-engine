package com.metsuengine;

import java.io.FileWriter;

import com.opencsv.CSVWriter;

public class CSVManager {

    public static void writeLine(String fileName, double lines) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
            //String line = ;

            String[] record = line.split(",");
            writer.writeNext(record);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    } 
}
