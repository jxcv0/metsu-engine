
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;



public class CreatePnLChart extends ApplicationFrame {

    public CreatePnLChart(final String title) {
        super(title);
        final XYDataset dataset = createDataset();
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
        }
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);

    }

    private JFreeChart createChart(final XYDataset dataset) {

        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Sample Chart",
                "Time",
                "PnL",
                dataset,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();

        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));
        return chart;
    }

    private XYDataset createDataset() {
        Calendar cal = Calendar.getInstance();
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        final TimeSeries series = new TimeSeries("Series 1", Second.class);

        series.add(new Second(cal.getTime()), 1); // Where I would like to add the pulled data every second

        dataset.addSeries(series);
        return dataset;
    }

    public static void main(final String[] args) {
        final CreatePnLChart demo = new CreatePnLChart("Time Series Demo 12");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);

        demo.setVisible(true);

    }

    private double getExternalData(){
        double PnL = 0;
        return PnL;
    }

}