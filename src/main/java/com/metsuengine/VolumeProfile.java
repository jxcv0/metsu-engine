package com.metsuengine;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class VolumeProfile {

    private HashMap<Double, Double> profile;

    public VolumeProfile() {
        this.profile = new HashMap<Double, Double>();
    }

    public void add(Trade trade) { 
        double price = trade.getPrice();
        double size = trade.getSize();

        if(this.profile.containsKey(price)) {
            double oldSize = this.profile.get(price);
            this.profile.put(price, oldSize + price);
        } else {
            this.profile.put(price, size);
        }
    }

    public void writeVolumeProfile(ZonedDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        String formattedDate = formatter.format(date);

        CSVManager manager = new CSVManager(formattedDate + "-profile.csv");

        for (Double price : this.profile.keySet()) {
            String[] line = {String.valueOf(price), String.valueOf(this.profile.get(price))};
            manager.writeLine(line);
        }
    }
}
