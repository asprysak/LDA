import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

/** 
 * Klasa przechowująca zestaw danych w postaci dwuwymiarowej tablicy.
 * 
 * @author Anna Sprysak
 */
public class DataSet {

	float[][] data  = new float[0][0];
	private float xStd;
	private float yStd;
	private float xMean;
	private float yMean;
	
	DataSet(){
	}
	
	/** 
	 * Metoda zwracająca liczbę punktów.
	 * 
	 * @return liczba punktów
	 */
	float getN(){
		return data[0].length;
	}
	
	
	float getXMean(){

		return xMean;
	}
	
	float getYMean(){

		return yMean;
	}
	
	float getXStd(){

		return xStd;
	}
	
	float getYStd(){

		return yStd;
	}

	float calculateMean(float[] input){
		float mean = 0;
		float n = input.length;
		float sum = 0;
		for (int j = 0; j < n; j++){
			sum += input[j];
		}
		mean = sum/n;
		return mean;
	}
	
	float calculateStd(float[] input){
		float std = 0;
		float sum = 0;
		float n = input.length;
		for (int j = 0; j < n; j++){
			sum += (float) (Math.pow((input[j]-calculateMean(input)),2));
		}
		std = (float) Math.sqrt(sum/(n-1));
		return std;
	}

	/** 
	 * Metoda wpisująca do tablicy dane z dwuwymiarowego rozkładu normalnego
	 * o zadanych średnich i odchyleniach standardowych.
	 * 
	 * @param spinNum JSpinner z liczbą punktów do wygenerowania
	 * @param spinXStd JSpinner z odchyleniem standardowym dla wsp. x
	 * @param spinXMean JSpinner ze średnią dla wsp. x
	 * @param spinYStd JSpinner z odchyleniem standardowym dla wsp. y
	 * @param spinYMean JSpinner ze średnią dla wsp. y
  	 * @param cor JSpinner z korelacją współrzędnch (od -1 do 1)
  	 * @param rand
	 */
	void generateArray (JSpinner spinNum, JSpinner spinXStd,JSpinner spinXMean, JSpinner spinYStd,JSpinner spinYMean, JSpinner cor,Random rand){
		
		data = new float[2][(Integer)spinNum.getValue()];
		float flxStd=((Number)spinXStd.getValue()).floatValue();
		float flxMean=((Number)spinXMean.getValue()).floatValue();
		float flyStd=((Number)spinYStd.getValue()).floatValue();
		float flyMean=((Number)spinYMean.getValue()).floatValue();
		float flCorXY = ((Number)cor.getValue()).floatValue();
	    float varx = flxStd*flxStd;
	    float vary = flyStd*flyStd;
		for(int i=0; i<(Integer)spinNum.getValue(); i++){
			float flRandX = ((Number)rand.nextGaussian()).floatValue();
			float flRandY = ((Number)rand.nextGaussian()).floatValue();
			data[0][i] = flRandX*flxStd+flxMean;
			data[1][i] = flyMean + ((flCorXY*varx*vary*flRandX) + (float)Math.sqrt(varx*vary - flCorXY*flCorXY*varx*vary)*flRandY)/flxStd;
		}
		xStd = calculateStd(data[0]);
		yStd = calculateStd(data[1]);
		xMean = calculateMean(data[0]);
		yMean = calculateMean(data[1]);
		
	}
	
	/**
	 * Metoda tworząca okno dialogowe i wywołująca metodę generateArray.
	 * 
	 * @param input1 pierwszy zestaw danych
	 * @param input2 drugi zestaw danych
	 * @param button przycisk, któremu zmieniany jest stan na "true",
	 *				 jeśli dwa zestawy danych nie są puste
	 */
	void generate (DataSet input1, DataSet input2, JButton button){
		
		JDialog dialog = new JDialog();
		dialog.setTitle("Generate dataset");
		dialog.setSize(220, 230);
		dialog.setResizable(false);
		dialog.setLayout(new GridLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout(""));
		String[] labels = {"Number of points","X mean", "Y mean", "X std", "Y std", "ρ"};
		dialog.add(panel);
		JSpinner spinNum=new JSpinner();
	    SpinnerModel numModel = new SpinnerNumberModel(0,0,1000,1); 
	    numModel.setValue(100);
	    spinNum.setModel(numModel);
	    
		JSpinner spinXMean=new JSpinner();
	    SpinnerModel meanModel = new SpinnerNumberModel(0,-10,10,0.1); 
	    spinXMean.setModel(meanModel);
	    meanModel.setValue(5);
 
	    JSpinner spinYMean=new JSpinner();
	    SpinnerModel meanYModel = new SpinnerNumberModel(0,-10,10,0.1); 
	   
	    spinYMean.setModel(meanYModel);
	     meanYModel.setValue(5);
		JSpinner spinXStd=new JSpinner();
	    SpinnerModel stdXModel = new SpinnerNumberModel(0,0,5,0.1); 
	    stdXModel.setValue(0.1);
	    spinXStd.setModel(stdXModel);
		JSpinner spinYStd=new JSpinner();
	    SpinnerModel stdYModel = new SpinnerNumberModel(0,0,5,0.1); 
	    stdYModel.setValue(0.1);
	    spinYStd.setModel(stdYModel);
	    
		JSpinner cor=new JSpinner();
	    SpinnerModel covXYModel = new SpinnerNumberModel(0,-1,1,0.1); 
	    covXYModel.setValue(0.1);
	    cor.setModel(covXYModel);
	    
	    Dimension d = new Dimension(spinNum.getPreferredSize());
	    spinYStd.setPreferredSize(d);
	    spinXStd.setPreferredSize(d);
	    spinYMean.setPreferredSize(d);
	    spinXMean.setPreferredSize(d);
	    spinNum.setPreferredSize(d);
	    cor.setPreferredSize(d);
	    	    
		panel.add(new JLabel(labels[0]));
	    panel.add(spinNum, "wrap");
	    panel.add(new JLabel(labels[1]));
	    panel.add(spinXMean,"wrap");
	    panel.add(new JLabel(labels[2]));
	    panel.add(spinYMean,"wrap");
	    panel.add(new JLabel(labels[3]));
	    panel.add(spinXStd,"wrap");
	    panel.add(new JLabel(labels[4]));
	    panel.add(spinYStd, "wrap");
	    panel.add(new JLabel(labels[5]));
	    panel.add(cor, "wrap");
	    
		int screenWidth = dialog.getToolkit().getScreenSize().width; 
		int screenHeight = dialog.getToolkit().getScreenSize().height; 
		dialog.setLocation((screenWidth-220)/2,(screenHeight-230)/2); 
		
		dialog.setVisible(true);
		JButton okButton = new JButton("OK");
		panel.add(okButton, "width 80, left");
		JButton cancelButton = new JButton("Cancel");
		panel.add(cancelButton, "width 80, right");
		
		Random rand = new Random();
		
		okButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				generateArray(spinNum, spinXStd, spinXMean, spinYStd, spinYMean,cor, rand);
				
				if(input1.data.length>0 && input2.data.length>0){
					button.setEnabled(true);
				}

			dialog.dispose();	
			}
		});
		
		cancelButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();	
			}
		});
	}
	
	/**
	 * Metoda tworząca okno wyboru pliku i wpisująca do tablicy dane z pliku.
	 */
	void loadFromFile() {
		final JFileChooser fc = new JFileChooser();
		int returnValue = fc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			System.out.println(selectedFile.getName());

			Scanner scanner = null;
			try {
				scanner = new Scanner(selectedFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			LineNumberReader lnr = null;
			try {
				lnr = new LineNumberReader(new FileReader(selectedFile));
				lnr.skip(Long.MAX_VALUE);
				lnr.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			data = new float[2][lnr.getLineNumber()];

			Scanner s2 = null;
			List<Float> list = new ArrayList<Float>();

			while (scanner.hasNextLine()) {
				s2 = new Scanner(scanner.nextLine());
				while (s2.hasNext()) {
					float s = Float.parseFloat(s2.next());
					list.add(s);
				}
			}

			scanner.close();
			s2.close();
			Iterator<Float> iter = list.iterator();
			for (int i = 0; i < data[0].length; i++) {
				for (int j = 0; j < data.length; j++) {
					if (iter.hasNext()) {
						data[j][i] = iter.next();
					}
				}
			}
		}
		xStd = calculateStd(data[0]);
		yStd = calculateStd(data[1]);
		xMean = calculateMean(data[0]);
		yMean = calculateMean(data[1]);
	}
}
