import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @see https://stackoverflow.com/a/13205322/230513
 * @see https://stackoverflow.com/questions/4637215
 */
public final class ChartWorker {

    private static final String S = "0.000000000000000";
    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel label = new JLabel(S, JLabel.CENTER);
    private final XYSeries series = new XYSeries("Result");
    private final XYDataset dataset = new XYSeriesCollection(series);

    private void create() {
        JFrame f = new JFrame("√2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(progressBar, BorderLayout.NORTH);

        JFreeChart chart = ChartFactory.createXYLineChart(
            "Newton's Method", "X", "Y", dataset,
            PlotOrientation.VERTICAL, false, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRangeAxis().setRange(1.4, 1.51);
        plot.getDomainAxis().setStandardTickUnits(
            NumberAxis.createIntegerTickUnits());

        XYLineAndShapeRenderer renderer
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);

        f.add(new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        }, BorderLayout.CENTER);
        
        f.add(label, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        runCalc();
    }

    private void runCalc() {
        progressBar.setIndeterminate(true);
        TwoWorker task = new TwoWorker();
        task.addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("progress".equals(e.getPropertyName())) {
                progressBar.setIndeterminate(false);
                progressBar.setValue((Integer) e.getNewValue());
            }
        });
        task.execute();
    }

    private class TwoWorker extends SwingWorker<Double, Double> {

        private static final int N = 5;
        private final DecimalFormat df = new DecimalFormat(S);
        double x = 1;
        private int n;

        @Override
        protected Double doInBackground() throws Exception {
            for (int i = 1; i <= N; i++) {
                x = x - (((x * x - 2) / (2 * x)));
                setProgress(i * (100 / N));
                publish(x);
                Thread.sleep(1000); // simulate latency
            }
            return x;
        }

        @Override
        protected void process(List<Double> chunks) {
            for (double d : chunks) {
                label.setText(df.format(d));
                series.add(++n, d);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new ChartWorker()::create);
    }
}