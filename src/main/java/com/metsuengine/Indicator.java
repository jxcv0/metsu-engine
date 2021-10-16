package com.metsuengine;

import java.time.ZonedDateTime;

import javax.swing.event.ChangeListener;

public interface Indicator extends ChangeListener{

    String getName();

    double getValue();

    ZonedDateTime getTime();

    boolean isEmpty();
}
