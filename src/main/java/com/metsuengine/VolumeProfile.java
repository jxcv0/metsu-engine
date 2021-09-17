package com.metsuengine;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class VolumeProfile extends TradeSeries {

    private HashMap<Double, Double> profile;

    public VolumeProfile() {
        this.profile = new HashMap<Double, Double>();
    }

    public HashMap<Double, Double> getHashMap() {
        return this.profile;
    }

    private void addToProfile(Trade trade) { 
        double price = trade.getPrice();
        double size = trade.getSize();

        if(this.profile.containsKey(price)) {
            double oldSize = this.profile.get(price);
            this.profile.put(price, oldSize + price);
        } else {
            this.profile.put(price, size);
        }
    }

    @Override // why u no work
    public void addTrade(Trade trade) {
        this.tradeSeries.add(trade);
        addToProfile(trade);
        fireStateChanged();
    }

    public void writeVolumeProfile(ZonedDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        String formattedDate = formatter.format(date);

        CSVManager manager = new CSVManager(formattedDate + "-profile.csv");

        List<Double> keys = new ArrayList<>(this.profile.keySet());
        Collections.sort(keys);

        for (Double price : keys) {
            String[] line = {String.valueOf(price), String.valueOf(this.profile.get(price))};
            manager.writeLine(line);
        }
    }
}
