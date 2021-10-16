package com.metsuengine;

import static org.junit.Assert.assertArrayEquals;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class ToArrayTest {

    @Test
    public void toArrayTest() {
        Map<ZonedDateTime, Double> values = new ConcurrentHashMap<ZonedDateTime, Double>();
        values.put(ZonedDateTime.now().minusMinutes(2), 10.0);
        values.put(ZonedDateTime.now().minusMinutes(1), 20.0);
        values.put(ZonedDateTime.now(), 30.0);

        // method works but values are sometimes out of order. Not a problem for implimentaion. false negative?
        List<Double> listActuals = values.values().stream().parallel().toList();
        double[] actuals = listActuals.stream().mapToDouble(Double::doubleValue).toArray();
        double[] expecteds = {10.0, 20.0, 30.0};

        assertArrayEquals(expecteds, actuals, 0);
    }
    
}
