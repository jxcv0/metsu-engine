package com.metsuengine;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVManager {

    private Path path = null;
    TradeSeries tradeSeries = new TradeSeries();

    public CSVManager(String file) {
        this.path = Paths.get(file);
    }

    public CSVManager(String file, TradeSeries tradeSeries) {
        this.path = Paths.get(file);
        this.tradeSeries = tradeSeries;
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

    public TradeSeries createFromCSV() {
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(this.path.toString());
            CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            List<String[]> lines = reader.readAll();
            reader.close();

            return buildAndSort(lines);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO first
    private TradeSeries buildAndSort(List<String[]> lines) {
        for (String[] line : lines) {
            this.tradeSeries.addTrade(new Trade(
                epochtoZonedDateTime(Double.parseDouble(line[0])),
                line[2],
                Double.parseDouble(line[4]),
                Double.parseDouble(line[3])));
        }

        return tradeSeries;
    }

    private ZonedDateTime epochtoZonedDateTime(Double epochSeconds) {
        long epochMilli = Double.valueOf(epochSeconds * 1000).longValue();
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
