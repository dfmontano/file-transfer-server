package filetransferserver;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class grafica {

    XYSeries con_win;
    XYSeries umbral;
    XYSeries lim;

    public grafica() {
        con_win = new XYSeries("con_win");
        umbral = new XYSeries("umbral");
        lim = new XYSeries("lim");
    }

    public void plotTcp(int conta, int con_win1, int umbral1, int lim1) { // create a dataset...
        con_win.add(conta, con_win1);
        umbral.add(conta, umbral1);
        lim.add(conta, lim1);
    }

    public void plotGraph() {
        //         Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(con_win);
        dataset.addSeries(umbral);
        dataset.addSeries(lim);

        //         Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart("FTP (Imagenes) con TCP-RENO", // Title
                "Iteracion", // x-axis Label
                "Ventana", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        // create and display a frame...
        ChartFrame frame = new ChartFrame("TCP - FTP", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
