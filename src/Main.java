import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.util.ShapeUtilities;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import net.miginfocom.swing.MigLayout;

/** 
 * Klasa główna rysująca okno programu, przeprowadzająca wszystkie obliczenia,
 * wyświetlająca wyniki i wykres.
 * 
 * @author Anna Sprysak
 */
public class Main {

	DataSet dataset1 = new DataSet();
	DataSet dataset2 = new DataSet();

	MainFrame frame = new MainFrame();
	JButton refershButton = new JButton("Next step");
	JButton clearButton = new JButton("Clear");
	JMenuItem addPoint = new JMenuItem("Add a point");
	PanelChart chartPanel = new PanelChart();
	int counter = 0;
	int pointCounter = 1;
	float xPoint = 0;
	float yPoint = 0;
	double a = 0;
	double b = 0;
	double A = 0;
	double B = 0;
	double C = 0;
	Color col1 = new Color(0, 0, 100);
	Color col2 = new Color(150, 0, 0);

	ExtendedTextField s00Field1 = new ExtendedTextField(" ");
	ExtendedTextField s01Field1 = new ExtendedTextField(" ");
	ExtendedTextField s10Field1 = new ExtendedTextField(" ");
	ExtendedTextField s11Field1 = new ExtendedTextField(" ");

	ExtendedTextField s00Field2 = new ExtendedTextField(" ");
	ExtendedTextField s01Field2 = new ExtendedTextField(" ");
	ExtendedTextField s10Field2 = new ExtendedTextField(" ");
	ExtendedTextField s11Field2 = new ExtendedTextField(" ");

	ExtendedTextField w00Field = new ExtendedTextField(" ");
	ExtendedTextField w01Field = new ExtendedTextField(" ");
	ExtendedTextField w10Field = new ExtendedTextField(" ");
	ExtendedTextField w11Field = new ExtendedTextField(" ");

	ExtendedTextField wod00Field = new ExtendedTextField(" ");
	ExtendedTextField wod01Field = new ExtendedTextField(" ");
	ExtendedTextField wod10Field = new ExtendedTextField(" ");
	ExtendedTextField wod11Field = new ExtendedTextField(" ");

	JTextField aField = new JTextField(" ");
	JTextField bField = new JTextField(" ");

	Main() {

		frame.setTitle("LDA");
		frame.controlPanel.setLayout(new MigLayout(""));
		frame.graphicPanel.add(chartPanel);
		frame.validate();
		refershButton.setEnabled(false);
		addPoint.setEnabled(false);

		refershButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (counter == 0) {
					chartPanel = new PanelChart(dataset1, dataset2);
					frame.graphicPanel.removeAll();
					frame.graphicPanel.add(chartPanel);
					chartPanel.revalidate();
					frame.graphicPanel.revalidate();
					frame.repaint();
				}

				float n1 = dataset1.getN();
				float n2 = dataset2.getN();

				double[][] S1 = new double[2][2];

				for (int i = 0; i < n1; i++) {
					S1[0][0] += Math.pow(
							(dataset1.data[0][i] - dataset1.getXMean()), 2);
					S1[1][0] += (dataset1.data[0][i] - dataset1.getXMean())
							* (dataset1.data[1][i] - dataset1.getYMean());
					S1[0][1] = S1[1][0];
					S1[1][1] += Math.pow(
							(dataset1.data[1][i] - dataset1.getYMean()), 2);
					s00Field1.setText(String
							.format("%.3f", S1[0][0] / (n1 - 1)));
					s01Field1.setText(String
							.format("%.3f", S1[0][1] / (n1 - 1)));
					s10Field1.setText(String
							.format("%.3f", S1[1][0] / (n1 - 1)));
					s11Field1.setText(String
							.format("%.3f", S1[1][1] / (n1 - 1)));

				}

				double[][] S2 = new double[2][2];

				for (int i = 0; i < n2; i++) {
					S2[0][0] += Math.pow(
							(dataset2.data[0][i] - dataset2.getXMean()), 2);
					S2[1][0] += (dataset2.data[0][i] - dataset2.getXMean())
							* (dataset2.data[1][i] - dataset2.getYMean());
					S2[0][1] = S2[1][0];
					S2[1][1] += Math.pow(
							(dataset2.data[1][i] - dataset2.getYMean()), 2);
				}
				s00Field2.setText(String.format("%.3f", S2[0][0] / (n2 - 1)));
				s01Field2.setText(String.format("%.3f", S2[0][1] / (n2 - 1)));
				s10Field2.setText(String.format("%.3f", S2[1][0] / (n2 - 1)));
				s11Field2.setText(String.format("%.3f", S2[1][1] / (n2 - 1)));

				double[][] W = new double[2][2];

				for (int i = 0; i < W[0].length; i++) {
					for (int j = 0; j < W.length; j++) {
						W[j][i] = (S2[j][i] + S1[j][i]) / (n1 + n2 - 2);
					}
				}

				w00Field.setText(String.format("%.3f", W[0][0]));
				w01Field.setText(String.format("%.3f", W[0][1]));
				w10Field.setText(String.format("%.3f", W[1][0]));
				w11Field.setText(String.format("%.3f", W[1][1]));

				double[][] Wod = new double[2][2];
				double det = W[0][0] * W[1][1] - W[0][1] * W[1][0];

				Wod[0][0] = W[1][1] / det;
				Wod[0][1] = -W[0][1] / det;
				Wod[1][0] = -W[1][0] / det;
				Wod[1][1] = W[0][0] / det;

				wod00Field.setText(String.format("%.3f", Wod[0][0]));
				wod01Field.setText(String.format("%.3f", Wod[0][1]));
				wod10Field.setText(String.format("%.3f", Wod[1][0]));
				wod11Field.setText(String.format("%.3f", Wod[1][1]));

				float dXMean = dataset1.getXMean() - dataset2.getXMean();
				float dYMean = dataset1.getYMean() - dataset2.getYMean();
				float sumXMean = dataset1.getXMean() + dataset2.getXMean();
				float sumYMean = dataset1.getYMean() + dataset2.getYMean();
				A = dXMean * Wod[0][0] + dYMean * Wod[0][1];
				B = dXMean * Wod[1][0] + dYMean * Wod[1][1];
				C = Math.log(n1 / n2) - (A * sumXMean + B * sumYMean) / 2;
				a = -A / B;
				b = -C / B;

				aField.setText(String.format("%.3f", a));
				bField.setText(String.format("%.3f", b));

				Matrix m1 = new Matrix(W);
				EigenvalueDecomposition val1 = m1.eig();
				double[] real1 = val1.getRealEigenvalues();
				Matrix vec1 = val1.getV();

				int ind1 = 0;
				double max1 = real1[0];
				for (int i = 0; i < real1.length; i++) {
					if (real1[i] > max1) {
						max1 = real1[i];
						ind1 = i;
					}
				}
				double min1 = real1[0];
				for (int i = 0; i < real1.length; i++) {
					if (real1[i] < min1) {
						min1 = real1[i];
					}
				}

				double[][] vec1Array = vec1.getArray();
				double x1 = vec1Array[0][ind1];
				double y1 = vec1Array[1][ind1];
				double ar1 = Math.atan(y1 / x1);

				float	rx = (float) Math.sqrt(max1);
				float	ry = (float) Math.sqrt(min1);

				if (counter == 1) {
					chartPanel.addElipse(dataset1.getXMean(),
							dataset1.getYMean(),
							(float) (rx * Math.sqrt(2.2914)),
							(float) (ry * Math.sqrt(2.2914)), col1, ar1);
					chartPanel.addElipse(dataset2.getXMean(),
							dataset2.getYMean(),
							(float) (rx * Math.sqrt(2.2914)),
							(float) (ry * Math.sqrt(2.2914)), col2, ar1);
				}
				if (counter == 2) {
					chartPanel.addElipse(dataset1.getXMean(),
							dataset1.getYMean(),
							(float) (rx * Math.sqrt(6.1582)),
							(float) (ry * Math.sqrt(6.1582)), col1, ar1);
					chartPanel.addElipse(dataset2.getXMean(),
							dataset2.getYMean(),
							(float) (rx * Math.sqrt(6.1582)),
							(float) (ry * Math.sqrt(6.1582)), col2, ar1);
				}
				if (counter == 3) {
					chartPanel.addElipse(dataset1.getXMean(),
							dataset1.getYMean(),
							(float) (rx * Math.sqrt(11.6182)),
							(float) (ry * Math.sqrt(11.6182)), col1, ar1);
					chartPanel.addElipse(dataset2.getXMean(),
							dataset2.getYMean(),
							(float) (rx * Math.sqrt(11.6182)),
							(float) (ry * Math.sqrt(11.6182)), col2, ar1);

				}
				if (counter == 4) {
					NumberAxis domainAxis = (NumberAxis) ((XYPlot) chartPanel.chart
							.getPlot()).getRangeAxis();
					domainAxis.setAutoRange(false);

					float minX = dataset1.data[0][0];
					float maxX = dataset1.data[0][0];
					float miny = dataset1.data[1][0];
					float maxy = dataset1.data[1][0];
					
					for (int j = 0; j < dataset1.data[0].length; j++) {
						if (dataset1.data[0][j] < minX) {
							minX = dataset1.data[0][j];
						}
						if (dataset1.data[0][j] > maxX) {
							maxX = dataset1.data[0][j];
						}
					}
					for (int j = 0; j < dataset2.data[0].length; j++) {
						if (dataset2.data[0][j] < minX) {
							minX = dataset2.data[0][j];
						}
						if (dataset2.data[0][j] > maxX) {
							maxX = dataset2.data[0][j];
						}
					}
					for (int j = 0; j < dataset1.data[0].length; j++) {
						if (dataset1.data[1][j] < miny) {
							miny = dataset1.data[1][j];
						}
						if (dataset1.data[1][j] > maxy) {
							maxy = dataset1.data[1][j];
						}
					}

					chartPanel.addLine(a, b, minX, maxX);
					addPoint.setEnabled(true);
				}
				counter++;

				if (counter == 5) {
					refershButton.setEnabled(false);
					counter = 0;
				}
			}
		});
		
		ActionListener clear = 	new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				counter = 0;
				chartPanel.removeAll();
				frame.graphicPanel.updateUI();
				if (dataset1.data.length > 0 && dataset2.data.length > 0) {
					refershButton.setEnabled(true);
				}
				addPoint.setEnabled(false);
				s00Field1.setText(" ");
				s01Field1.setText(" ");
				s10Field1.setText(" ");
				s11Field1.setText(" ");

				s00Field2.setText(" ");
				s01Field2.setText(" ");
				s10Field2.setText(" ");
				s11Field2.setText(" ");

				w00Field.setText(" ");
				w01Field.setText(" ");
				w10Field.setText(" ");
				w11Field.setText(" ");

				wod00Field.setText(" ");
				wod01Field.setText(" ");
				wod10Field.setText(" ");
				wod11Field.setText(" ");

				aField.setText(" ");
				bField.setText(" ");
				pointCounter = 1;

			}
		};
		clearButton.addActionListener(clear);
		frame.controlPanel.add(this.refershButton,"width 110:110:110, wrap");
		frame.controlPanel.add(this.clearButton, "width 110:110:110, wrap");

		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		JMenu file = new JMenu("Options");
		bar.add(file);
		addPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog pointchooser = new JDialog();
				pointchooser.setTitle("Add a point");
				int dialogWidth = 150;
				int dialogHeight = 150;
				int screenWidth = frame.getToolkit().getScreenSize().width;
				int screenHeight = frame.getToolkit().getScreenSize().height;
				pointchooser.setLocation((screenWidth - dialogWidth) / 2,
						(screenHeight - dialogHeight) / 2);
				pointchooser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				pointchooser.setSize(dialogWidth, dialogHeight);
				pointchooser.setLayout(new MigLayout(""));
				JSpinner spinX = new JSpinner();
				SpinnerModel xModel = new SpinnerNumberModel(0, -100, 100, 0.1);
				xModel.setValue(0.1);
				spinX.setModel(xModel);
				JSpinner spinY = new JSpinner();
				SpinnerModel yModel = new SpinnerNumberModel(0, -100, 100, 0.1);
				yModel.setValue(0.1);
				spinY.setModel(yModel);
				String[] labels = { "x", "y" };
				pointchooser.add(new JLabel(labels[0]));
				pointchooser.add(spinX, "wrap");
				pointchooser.add(new JLabel(labels[1]));
				pointchooser.add(spinY, "wrap");
				JButton okButton = new JButton("OK");
				pointchooser.add(okButton);
				JButton cancelButton = new JButton("Cancel");
				pointchooser.add(cancelButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						xPoint = ((Number) spinX.getValue()).floatValue();
						yPoint = ((Number) spinY.getValue()).floatValue();
						XYSeries series = new XYSeries("Point " + pointCounter);
						series.add(xPoint, yPoint);
						chartPanel.xyseriescollection.addSeries(series);
						Color c = new Color(0);

						if ((C + A * xPoint + B * yPoint) > 0) {
							c = (Color) chartPanel.xylineandshaperenderer
									.getSeriesPaint(0);
						} else {
							c = (Color) chartPanel.xylineandshaperenderer
									.getSeriesPaint(1);
						}

						chartPanel.xylineandshaperenderer
								.setSeriesLinesVisible(
										chartPanel.xyseriescollection
												.getSeriesCount() - 1, false);
						chartPanel.xylineandshaperenderer
								.setSeriesShapesVisible(
										chartPanel.xyseriescollection
												.getSeriesCount() - 1, true);
						chartPanel.xylineandshaperenderer.setSeriesPaint(
								chartPanel.xyseriescollection.getSeriesCount() - 1,
								c);
						chartPanel.xylineandshaperenderer.setSeriesShape(
								chartPanel.xyseriescollection.getSeriesCount() - 1,
								ShapeUtilities.createDiagonalCross(3, 3));
						chartPanel.xylineandshaperenderer
								.setSeriesOutlinePaint(
										chartPanel.xyseriescollection
												.getSeriesCount() - 1,
										Color.white);
						chartPanel.validate();
						pointCounter++;
						pointchooser.dispose();
					}
				});

				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						pointchooser.dispose();
					}
				});
				pointchooser.setVisible(true);
			}
		});
		file.add(addPoint);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(exit);

		JMenu dataSet1Option = new JMenu("Dataset 1");
		JMenu dataSet2Option = new JMenu("Dataset 2");
		bar.add(dataSet1Option);
		bar.add(dataSet2Option);
		JMenuItem loadItem = new JMenuItem("Load from a file");

		loadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataset1.loadFromFile();
				chartPanel.revalidate();
				chartPanel.revalidate();
				frame.validate();
				frame.repaint();
				chartPanel.validate();
				chartPanel.repaint();
				if (dataset1.data.length > 0 && dataset2.data.length > 0) {
					refershButton.setEnabled(true);
				}
			}
		});

		JMenuItem generateItem = new JMenuItem("Generate");
		generateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataset1.generate(dataset1, dataset2, refershButton);
			}
		});

		JMenuItem loadItem2 = new JMenuItem("Load from a file");
		loadItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataset2.loadFromFile();
				frame.graphicPanel.revalidate();
				frame.repaint();
				chartPanel.revalidate();
				if (dataset1.data.length > 0 && dataset2.data.length > 0) {
					refershButton.setEnabled(true);
				}
			}
		});

		JMenuItem generateItem2 = new JMenuItem("Generate");
		generateItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataset2.generate(dataset1, dataset2, refershButton);

			}
		});

		dataSet1Option.add(loadItem);
		dataSet1Option.add(generateItem);
		dataSet2Option.add(loadItem2);
		dataSet2Option.add(generateItem2);
		JLabel s1Label = new JLabel("<html>S<sub>1</sub>=</html>");
		frame.controlPanel.add(s1Label, "wrap");
		frame.controlPanel.add(s00Field1);
		frame.controlPanel.add(s01Field1, "wrap");
		frame.controlPanel.add(s10Field1);
		frame.controlPanel.add(s11Field1, "wrap");
		JLabel s2Label = new JLabel("<html>S<sub>2</sub>=</html>");
		frame.controlPanel.add(s2Label, "wrap");
		frame.controlPanel.add(s00Field2);
		frame.controlPanel.add(s01Field2, "wrap");
		frame.controlPanel.add(s10Field2);
		frame.controlPanel.add(s11Field2, "wrap");
		JLabel wLabel = new JLabel("W=");
		frame.controlPanel.add(wLabel, "wrap");
		frame.controlPanel.add(w00Field);
		frame.controlPanel.add(w01Field, "wrap");
		frame.controlPanel.add(w10Field);
		frame.controlPanel.add(w11Field, "wrap");
		JLabel wodLabel = new JLabel("<html>W<sup>-1</sup>=</html>");
		frame.controlPanel.add(wodLabel, "wrap");
		frame.controlPanel.add(wod00Field);
		frame.controlPanel.add(wod01Field, "wrap");
		frame.controlPanel.add(wod10Field);
		frame.controlPanel.add(wod11Field, "wrap");
		aField.setColumns(4);
		aField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel aLabel = new JLabel("a=");
		frame.controlPanel.add(aLabel, "wrap");
		frame.controlPanel.add(aField, "wrap");
		bField.setColumns(4);
		bField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel bLabel = new JLabel("b=");
		frame.controlPanel.add(bLabel, "wrap");
		frame.controlPanel.add(bField, "wrap");
		frame.validate();

	}

	public static void main(String[] args) throws IOException {

		new Main();

	}
}
