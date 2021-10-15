package com.metsuengine;

import java.time.ZonedDateTime;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public interface Indicator extends ChangeListener{

    final EventListenerList listenerList = new EventListenerList();

    String getName();

    double calculate();

    double getValue();

    ZonedDateTime getTime();

    boolean isEmpty();

    default void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    default void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    default void fireStateChanged() {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            ChangeEvent event = new ChangeEvent(this);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(event);
            }
        }
    }
}
