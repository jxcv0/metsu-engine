package com.metsuengine.indicators;

import javax.swing.event.ChangeEvent;

public class CointegrationFactorIndicator extends AbstractIndicator {

    private final MultivariateTimeSeriesIndicator multivariateTimeSeriesIndicator;

    public CointegrationFactorIndicator(String name, MultivariateTimeSeriesIndicator multivariateTimeSeriesIndicator) {
        super(name);
        this.multivariateTimeSeriesIndicator = multivariateTimeSeriesIndicator;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
