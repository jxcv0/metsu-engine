package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;
import com.metsuengine.TickSeries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StandardDeviationIndicator extends AbstractIndicator {

    private final int lag;
    private final double k; 
    private final DescriptiveStatistics descStat;
    private final Indicator indicator;

    public StandardDeviationIndicator(String name, Indicator indicator, int lag, double k, TickSeries... tickSeries) {
        super(name);
        for (TickSeries series : tickSeries) {
            series.addChangeListener(this);
        }
        this.lag = lag;
        this.k = k;
        this.descStat = new DescriptiveStatistics(this.lag);
        this.indicator = indicator;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        descStat.addValue(indicator.getValue());
        values.put(source.getLastTick().time(), descStat.getStandardDeviation() * k);
    }
}
