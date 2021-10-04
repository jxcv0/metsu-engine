package com.metsuengine;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

public class GaussianIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final int poles;
    private int sign;
    private double previousValue; // TODO

    protected GaussianIndicator(BarSeries series, int barCount, int poles) {
        super(series);
        this.barCount = barCount;
        this.poles = poles;
        this.sign = 1;
    }

    @Override
    protected Num calculate(int index) {
        // int startIndex = Math.max(0, index - barCount + 1);

        double beta = (1 - Math.cos(2 * Math.PI / barCount)) / (Math.pow(Math.sqrt(2), 2.0 / poles) - 1);
        double alpha = (-1 * beta) + Math.sqrt(beta * beta + 2 * beta);
        double preFilter = this.getBarSeries().getBar(index).getClosePrice().doubleValue() * Math.pow(alpha, poles);
        double result = 0;
        if (index > 0) {
            result = getPoles(index, preFilter, alpha);
        }

        double filter = preFilter + result;

        return numOf(filter);
    }

    private double getPoles(int index, double f, double alpha) {
        int results = 0  + index;
        int max = Math.max(Math.min(poles, index), 1);
        for (int i = 1; i < max; i++) {
            double multiplier = CombinatoricsUtils.factorial(poles) / CombinatoricsUtils.factorial(poles - i) * CombinatoricsUtils.factorial(i);
            double matpo = Math.pow(1 - alpha, i);
            double previousValue;
            if (this.calculate(index-1).isNaN()) {
                previousValue = 0;
            } else {
                previousValue = this.calculate(index-1).doubleValue();
            }
            double sum = sign * multiplier * matpo * previousValue;
            results += sum;
            sign *= -1;
        }
        results -= index;
        System.out.print(results);
        return results;
    }    
}
