/* ----------------------
 * MultipleAxisDemo3.java
 * ----------------------
 * (C) Copyright 2003-2014, by Object Refinery Limited.
 *
 * http://www.object-refinery.com
 *
 */
package javafxchart;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class MultipleXYaxis5 extends ApplicationFrame {

    static int weightAxisMax = 0;
    static long startTime = 0,
            endTime = 0;

    static NumberAxis xAxis0hours = null;
    static DateAxis xAxis1time = null;
    static NumberAxis yAxis0weight = null;

    public MultipleXYaxis5(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        setContentPane(chartPanel);
    }

    private static JFreeChart createChart() {
        weightAxisMax = 3500;
        startTime = getStartTime();
        // DATASET 0 = Weight, DOMAIN AXIS 0 = Time, RANGE AXIS 0 = Weight
        XYDataset dataset0Weight = createDatasetDomainHours("Weight", COLUMN_WEIGHT);
        JFreeChart chart = ChartFactory.createXYLineChart("Multiple X and Y axis",
                    "Hours since start", "", dataset0Weight, PlotOrientation.HORIZONTAL, true, false, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        xAxis0hours = (NumberAxis) plot.getDomainAxis();
       
       
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setDomainCrosshairVisible(true);
        //plot.setRangeCrosshairVisible(true);
        // Attach an AxisChangeListener to the first axis and give that listener a reference to the second axis.
        //In the axisChanged method, retrieve the axis from the AxisChangeEvent, cast that to a ValueAxis, get its lower and upper bound,
        //multiply both with the desired value, and set the bound for the second axis."

        // AxisChangeListener
        // DATASET 2 = Temperatures, DOMAIN AXIS 0 = Time, RANGE AXIS 1 = Temperature

        xAxis1time = new DateAxis("Date and time");
        xAxis1time.setVerticalTickLabels(true);
        DateFormat formatter = new SimpleDateFormat("dd.MM  HH:mm");
        xAxis1time.setDateFormatOverride(formatter);

        plot.setDomainAxis(1, xAxis1time);
        plot.setDomainAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
        adjustHoursAxis();
       
        // Range Axis 0 : Weight
       
        yAxis0weight = (NumberAxis) plot.getRangeAxis();
       
        // Range Axis 1  : Temperature
       
        NumberAxis yAxis1Temp = new NumberAxis("Temperature");
        yAxis1Temp.setUpperBound(300.0);
        plot.setRangeAxis(1, yAxis1Temp);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);

        XYDataset datasetTemperature = createDatasetDomainHours("Temperature", COLUMN_TEMPERATURE);
        plot.setDataset(2, datasetTemperature);
        plot.mapDatasetToDomainAxis(2, 0);
        plot.mapDatasetToRangeAxis(2, 1);

        // DATASET 1 = Stirrer, DOMAIN AXIS 0 = Time, RANGE AXIS 2 = Stirrer
        NumberAxis yAxis2stirrer = new NumberAxis("Stirrer RPM");

        yAxis2stirrer.setLowerBound(-50.0);
        yAxis2stirrer.setUpperBound(+50.0);
        plot.setRangeAxis(2, yAxis2stirrer);
        plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
       
        XYDataset datasetStirrer = createDatasetDomainHours("Stirrer", COLUMN_STIRRER);
        plot.setDataset(1, datasetStirrer);
        plot.mapDatasetToDomainAxis(1, 0);
        plot.mapDatasetToRangeAxis(1, 2);

        plot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(2, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(3, new XYLineAndShapeRenderer(true, false));
        ChartUtilities.applyCurrentTheme(chart);

        AxisRangeAdjuster5 adj = new AxisRangeAdjuster5();
        adj.registerAxis(getStartTime(), getEndTime(), (NumberAxis) plot.getDomainAxis(), xAxis1time);
        return chart;

    }

    static int COLUMN_WEIGHT = 0;
    static int COLUMN_TEMPERATURE = 1;
    static int COLUMN_STIRRER = 2;

    static float dataSeries[][] = {
        { 100f, 70f, 20f},
        {1200f, 70f, 20f},
        {2300f, 70f, 45f},
        {3002f, 65f, 45f},
        {3502f, 65f, 30f},
        {3802f, 65f, 30f},
        {3810f, 65f, 15f},
        {3805f, 70f, 15f},
        {3720f, 70f, 0f},
        {2500f, 75f, -20f},
        {1300f, 75f, -20f},
        {1000f, 100f, 5f},
        { 430f, 100f, 5f},
        {  30f, 22f,   0f}
    };
    static String timeSeries[]
            = { "2017-01-31 13:00:04", // ->  0,0 hours
                "2017-01-31 13:30:00",
                "2017-01-31 13:31:00",
                "2017-01-31 18:00:00",
                "2017-01-31 18:01:00",
                "2017-01-31 21:58:00",
                "2017-01-31 22:00:00",
                "2017-02-01 02:22:00",
                "2017-02-01 02:24:00",
                "2017-02-01 06:22:00",
                "2017-02-01 07:22:00",
                "2017-02-01 07:24:00",
                "2017-02-01 13:20:00",
                "2017-02-02 13:22:58"
        };
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private static long sampleTime(String sTime) {
        Date d = null;
        long time = 0;
        try {
            d = df.parse(sTime);
            time = d.getTime();
        } catch (ParseException e) {
            time = -1;
        }
        return time;
    }
   
    private static double sampleHours(int i) {
        Date d = new Date();

        try {
            d = df.parse(timeSeries[i]);
        } catch (ParseException e) {
        }
        double hours = hoursSinceStart(startTime, d.getTime() );
        return hours;
    }
    private static long getStartTime() {
        return sampleTime(timeSeries[0]);
    }

    private static long getEndTime() {
        return sampleTime(timeSeries[timeSeries.length - 1]);
    }


    public static Double hoursSinceStart(long startTime, long thisTime) {
        Double hours = ((double) (thisTime - startTime)) / (double) (60 * 60 * 1000);
        return hours;
    }

    public static Long timeFromHours(long startTime, Double hours) {
        Long time = ( (long) (hours * 60 * 60 * 1000)) + startTime;
        return time;
    }
    static void adjustHoursAxis() {
        xAxis1time.setLowerBound( timeFromHours( startTime, xAxis0hours.getLowerBound() ) );
        xAxis1time.setUpperBound( timeFromHours( startTime, xAxis0hours.getUpperBound() ) );
    }

           
    private static XYDataset createDatasetDomainHours(String name, int dataSeriesColumn) {
        XYSeries series = new XYSeries(name);
        for (int i = 0; i < timeSeries.length; i++) {
            double hours = sampleHours(i);
            series.add(hours, dataSeries[i][dataSeriesColumn]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;

    }

    /**
     * Creates a panel for the demo.
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {

        MultipleXYaxis5 demo = new MultipleXYaxis5(
                "Based on JFreeChart: MultipleXYaxis5.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}

class AxisRangeAdjuster5 implements AxisChangeListener {

    private DateAxis timeAxis = null;
    private ValueAxis hoursAxis = null;

    private ArrayList<ValueAxis> axes = new ArrayList<ValueAxis>();

    private boolean active = true;

    public void registerAxis(long pStartTime, long pEndTime, ValueAxis pHoursAxis, DateAxis pDateAxis ) {
        timeAxis = pDateAxis;
        hoursAxis = pHoursAxis;
        timeAxis.addChangeListener(this);
        hoursAxis.addChangeListener(this);
    }

    public void axisChanged(AxisChangeEvent ace) {

        if (active) {
            Axis a = ace.getAxis();
            if ((a instanceof DateAxis)) {
                DateAxis da = (DateAxis)a;              
                System.out.println("\"" + da.getLabel() + "\" : " + MultipleXYaxis5.df.format(new Date()));
                System.out.println("axisChanged : Time lower : " + timeAxis.getLowerBound() + " upper  :" + timeAxis.getUpperBound());
                System.out.println("axisChanged : Hour lower : " + hoursAxis.getLowerBound() + " upper  :" + hoursAxis.getUpperBound());
                active = false;
                MultipleXYaxis5.adjustHoursAxis();
                active = true;
            } else if ((a instanceof NumberAxis)) {
                NumberAxis na = (NumberAxis)a;
                System.out.println("\"" + na.getLabel() );
                System.out.println("axisChanged : Time lower : " + timeAxis.getLowerBound() + " upper  :" + timeAxis.getUpperBound());
                System.out.println("axisChanged : Hour lower : " + hoursAxis.getLowerBound() + " upper  :" + hoursAxis.getUpperBound());
                active = false;
                MultipleXYaxis5.adjustHoursAxis();
                active = true;
            }
        }
    }
}