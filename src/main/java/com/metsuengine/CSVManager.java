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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVManager {

    private final Path path;
    private TradeSeries tickSeries;

    public CSVManager(String file) {
        this.path = Paths.get(file);
        this.tickSeries = new TradeSeries();
    }

    public CSVManager(String file, TradeSeries tickSeries) {
        this.path = Paths.get(file);
        this.tickSeries = tickSeries;
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

    public TradeSeries createTickSeries() {
        TradeSeries tickSeries = new TradeSeries();
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(this.path.toString());
            CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            List<String[]> lines = reader.readAll();
            reader.close();

            List<Trade> ticks = new ArrayList<Trade>();

            for (String[] line : lines) {
                ticks.add(new Trade(
                    epochtoZonedDateTime(Double.parseDouble(line[0])),
                    line[2],
                    Double.parseDouble(line[4]),
                    Double.parseDouble(line[3])));
            }
    
            Collections.reverse(ticks);
            tickSeries.addAll(ticks);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickSeries;
    }

    public List<Trade> getTicks() {
        return tickSeries.getTicks();
    }

    public List<Trade> buildAndSort(List<String[]> lines) {
        List<Trade> ticks = new ArrayList<Trade>();

        for (String[] line : lines) {
            ticks.add(new Trade(
                epochtoZonedDateTime(Double.parseDouble(line[0])),
                line[2],
                Double.parseDouble(line[4]),
                Double.parseDouble(line[3])));
        }

        Collections.reverse(ticks);
        return ticks;

    }

    private ZonedDateTime epochtoZonedDateTime(Double epochSeconds) {
        long epochMilli = Double.valueOf(epochSeconds * 1000).longValue();
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
