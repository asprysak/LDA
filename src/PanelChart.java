import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/** 
 * Klasa rozszerzająca JPanel i zawierająca wykres.
 * 
 * @author Anna Sprysak
 */
public class PanelChart extends JPanel {

	private static final long serialVersionUID = 1L;
	XYSeries xyseries1 = new XYSeries("1 Series");
	XYSeries xyseries2 = new XYSeries("2 Series");
	XYSeriesCollection xyseriescollection = new XYSeriesCollection();
	XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer();
	XYLineAndShapeRenderer xylineandshaperenderer2 = new XYLineAndShapeRenderer();
	JFreeChart chart;

	public PanelChart() {
	}
	
	/** 
	 * Konstruktor przyjmujący dwa obieky typu DataSet.
	 * 
	 * @param data1 pierwszy zestaw danych do narysowania
	 * @param data2 drugi zestaw danych do narysowania
	 */
	public PanelChart(DataSet data1, DataSet data2) {
		XYDataset xydataset = createDataset(data1, data2);
		createChart(xydataset);
		ChartPanel chartpanel = new ChartPanel(chart);
		this.add(chartpanel);
	}

	/** 
	 * Metoda rysująca adnotację na wykresie w kształcie elipsy.
	 * 
	 * @param middleX składowa x środka elipsy
	 * @param middleY składowa y środka elipsy
	 * @param rX połowa długości osi poziomej
	 * @param rY połowa długości osi pionowej
	 * @param col kolor elipsy
	 * @param alpha kąt obrotu elipsy
	 */
	public void addElipse(float middleX, float middleY, float rX, float rY,
			Color col, double alpha) {

		Ellipse2D ell = new Ellipse2D.Double(middleX - rX, middleY - rY,
				2 * rX, 2 * rY);
		XYShapeAnnotation a = new XYShapeAnnotation(AffineTransform
				.getRotateInstance(alpha, middleX, middleY)
				.createTransformedShape(ell), new BasicStroke(1.0f), col);
		chart.getXYPlot().addAnnotation(a);
		chart.getXYPlot().getRendererCount();
	}

	/** 
	 * Metoda rysująca adnotację na wykresie w postaci linii prostej.
	 * 
	 * @param a współczynnik kierunkowy prostej
	 * @param b wyraz wolny równania prostej
	 * @param xStart składowa x punktu początkowego
	 * @param xEnd składowa x punktu końcowego
	 */
	public void addLine(double a, double b, float xStart, float xEnd) {
		XYSeries xyseries4 = new XYSeries("Line");
		xyseries4.add(xStart, a * xStart + b);
		xyseries4.add(xEnd, a * xEnd + b);
		xyseriescollection.addSeries(xyseries4);
		xylineandshaperenderer.setSeriesLinesVisible(
				xyseriescollection.getSeriesCount() - 1, true);
		xylineandshaperenderer.setSeriesShapesVisible(
				xyseriescollection.getSeriesCount() - 1, false);
		xylineandshaperenderer.setSeriesPaint(
				xyseriescollection.getSeriesCount() - 1, Color.magenta);
	}

	/** 
	 * Metoda dodająca do xyseriescollection dwa zestawy danych.
	 * 
	 * @param data1 pierwszy zestaw danych
	 * @param data2 drugi zestaw danych
	 * @return xyseriescollection
	 */
	public XYDataset createDataset(DataSet data1, DataSet data2) {
		for (int i = 0; i < data1.data[0].length; i++) {
			xyseries1.add(data1.data[0][i], data1.data[1][i]);
		}

		for (int i = 0; i < data2.data[0].length; i++) {
			xyseries2.add(data2.data[0][i], data2.data[1][i]);
		}
		xyseriescollection.addSeries(xyseries1);
		xyseriescollection.addSeries(xyseries2);
		return xyseriescollection;
	}
	
	/** 
	 * Metoda inicjalizująca pole chart.
	 * 
	 * @param xydataset
	 */
	public void createChart(XYDataset xydataset) {
		chart = ChartFactory.createXYLineChart("", "X", "Y", xydataset,
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot xyplot = (XYPlot) chart.getPlot();
		xylineandshaperenderer.setSeriesLinesVisible(0, false);
		xylineandshaperenderer.setSeriesShapesVisible(0, true);
		xylineandshaperenderer.setSeriesLinesVisible(1, false);
		xylineandshaperenderer.setSeriesShapesVisible(1, true);
		xylineandshaperenderer.setSeriesPaint(0, Color.blue);
		xylineandshaperenderer.setSeriesPaint(1, Color.red);
		xylineandshaperenderer.setUseOutlinePaint(true);
		xylineandshaperenderer.setDefaultEntityRadius(6);
		xylineandshaperenderer.setSeriesOutlinePaint(0, Color.blue);
		xylineandshaperenderer.setSeriesOutlinePaint(1, Color.red);
		xyplot.setRenderer(xylineandshaperenderer);
		NumberAxis domainAxis = (NumberAxis) xyplot.getRangeAxis();
		domainAxis.setAutoRangeIncludesZero(false);
	}

}
