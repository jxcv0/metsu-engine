package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

import com.metsuengine.Indicator;
import com.metsuengine.TickSeries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class AverageIndicator extends AbstractIndicator{

    private final int lag;
    private final DescriptiveStatistics descStat;
    private final Indicator indicator;

    public AverageIndicator(String name, int lag, TickSeries... tickSeries) {
        super(name);
        for (TickSeries series : tickSeries) {
            series.addChangeListener(this);
        }
        this.lag = lag;
        this.descStat = new DescriptiveStatistics(this.lag);
        this.indicator = null;
    }

    public AverageIndicator(String name, Indicator indicator, int lag, TickSeries... tickSeries) {
        super(name);
        for (TickSeries series : tickSeries) {
            series.addChangeListener(this);
        }
        this.lag = lag;
        this.descStat = new DescriptiveStatistics(this.lag);
        this.indicator = indicator;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TickSeries source = (TickSeries) e.getSource();
        if (indicator.equals(null)) {
            descStat.addValue(source.getLastTick().price());
            values.put(source.getLastTick().time(), descStat.getMean());
        } else {
            descStat.addValue(indicator.getValue());
            values.put(source.getLastTick().time(), descStat.getMean());
        }
    }
}
