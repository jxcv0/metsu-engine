package com.metsuengine;

import java.time.ZonedDateTime;

import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Indicator extends ChangeListener{

    final Logger logger = LoggerFactory.getLogger(Indicator.class);

    /**
     * @param index index of a value within the indicator
     * @return      the value of the indicator at the index
     */
    double value(ZonedDateTime index);

    // void trim();
}
