package com.metsuengine;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

public class GaussianIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final int poles;
    private int sign;

    protected GaussianIndicator(BarSeries series, int barCount, int poles) {
        super(series);
        this.barCount = barCount;
        this.poles = poles;
    }

    @Override
    protected Num calculate(int index) {
        // int startIndex = Math.max(0, index - barCount + 1);

        double beta = (1 - Math.cos(2 * Math.PI / barCount)) / (Math.pow(Math.sqrt(2), 2.0 / poles) - 1);
        double alpha = (-1 * beta) + Math.sqrt(beta * beta + 2.0 * beta);
        double pre = this.getBarSeries().getBar(index).getClosePrice().doubleValue() * Math.pow(alpha, poles);

        return null;
    }
}
