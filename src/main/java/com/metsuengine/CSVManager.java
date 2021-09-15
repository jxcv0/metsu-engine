package com.metsuengine;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.opencsv.CSVWriter;

public class CSVManager {

    private Path path = null;

    public CSVManager(String file) {
        this.path = Paths.get(file);
    }

    public void writeLine(String[] line) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(this.path.toString(), true));
            writer.writeNext(line);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TradeSeries createFromCSV(String file) {
        // TODO load from bybit trading data
        return new TradeSeries();
    }
}
