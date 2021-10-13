package com.metsuengine;

import java.time.ZonedDateTime;

import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Indicator extends ChangeListener{

    final Logger logger = LoggerFactory.getLogger(Indicator.class);
    final EventListenerList listenerList = new EventListenerList();

    /**
     * @param index index of a value within the indicator
     * @return      the value of the indicator at the index
     */
    double value(ZonedDateTime index);

    void calculate(TickSeries tickSeries);

    String getName();

    void addChangeListener(ChangeListener listener);

    void removeChangeListener(ChangeListener listener);

    void fireStateChanged();
}
