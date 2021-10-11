package com.metsuengine;

public interface Indicator<T> {

    /**
     * @param index index of a value within the indicator
     * @return      the value of the indicator at the index
     */
    T value(int index);

    /**
     * @return the indicators TickSeries from which it was built
     */
    TickSeries getTickSeries();

}
