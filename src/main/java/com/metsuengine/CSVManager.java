package com.metsuengine;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import com.opencsv.CSVReader;
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

    public TradeSeries createFromCSV() {
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(this.path.toString());
            CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            List<String[]> lines = reader.readAll();
            reader.close();

            return build(lines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private TradeSeries build(List<String[]> lines) {
        TradeSeries tradeSeries = new TradeSeries();
        for (String[] line : lines) {
            tradeSeries.addTrade(new Trade(
                epochtoZonedDateTime(Double.parseDouble(line[0])),
                line[2],
                Double.parseDouble(line[4]),
                Double.parseDouble(line[3])));
        }

        return tradeSeries;
    }

    private ZonedDateTime epochtoZonedDateTime(Double epochMilliDouble) {
        long epochMilli = Double.valueOf(epochMilliDouble).longValue();
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
