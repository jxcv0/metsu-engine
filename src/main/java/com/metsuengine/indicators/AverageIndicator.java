package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;

public class AverageIndicator extends AbstractIndicator {

    private final Indicator indicator;
    private int cumulativeValue;
    private int cumulativeCount;
    
    public AverageIndicator(String name, Indicator indicator) {
        super(name);
        this.indicator = indicator;
        this.indicator.addChangeListener(this);
        this.cumulativeCount = 0;
        this.cumulativeValue = 0;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Indicator source = (Indicator) e.getSource();
        if (!indicator.isEmpty()) {
            cumulativeCount++;
            cumulativeValue += source.getValue();
            values.put(source.getTime(), calculate());
            fireStateChanged();
        }
    }

    @Override
    public double calculate() {
        return cumulativeValue/cumulativeCount;
    }
}
