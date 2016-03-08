package test;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Test;

import HeatMap.HeatMap;

public class SimpleHeatMaps {
	
	public static void main(String args[]) {
		Rainbow();
	}
	
	public static void Rainbow() {
		int size = 30;
		double[][] data = new double[size][size];
		double counter = 1.0;
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				data[i][j] = counter;
				counter += 1;
			}
		}
		
		HeatMap rainbow = new HeatMap(data);
		JFrame frame = new JFrame();
		frame.setSize(300,300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(rainbow);
	}
}
