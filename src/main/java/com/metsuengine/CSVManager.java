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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

public class CSVManager {

    private Path path = null;
    private TickSeries tickSeries = new TickSeries();

    public CSVManager(String file) {
        this.path = Paths.get(file);
    }

    public CSVManager(String file, TickSeries tickSeries) {
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

    public void createFromCSV() {
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(this.path.toString());
            CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            List<String[]> lines = reader.readAll();
            reader.close();

            buildAndSort(lines);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildAndSort(List<String[]> lines) {
        LinkedList<Tick> ticks = new LinkedList<Tick>();

        for (String[] line : lines) {
            ticks.add(new Tick(
                epochtoZonedDateTime(Double.parseDouble(line[0])),
                line[2],
                Double.parseDouble(line[4]),
                Double.parseDouble(line[3])));
        }

        Collections.reverse(ticks);

        for (Tick tick : ticks) {
            this.tickSeries.addTick(tick);
        }
    }

    private ZonedDateTime epochtoZonedDateTime(Double epochSeconds) {
        long epochMilli = Double.valueOf(epochSeconds * 1000).longValue();
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public BarSeries barSeriesFromCSV() {
        BarSeries barSeries = new BaseBarSeries();
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(this.path.toString());
            CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            List<String[]> lines = reader.readAll();
            reader.close();

            barSeries = buildKline(lines);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return barSeries;
    }

    private BarSeries buildKline(List<String[]> lines) {
        BarSeries barSeries = new BaseBarSeries();

        for (String[] line : lines) {
            barSeries.addBar(
                ZonedDateTime.parse(line[0]),
                Double.parseDouble(line[1]),
                Double.parseDouble(line[2]),
                Double.parseDouble(line[3]),
                Double.parseDouble(line[4]),
                Double.parseDouble(line[5]));
        }

        return barSeries;
    }
}
