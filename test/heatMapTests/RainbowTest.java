package heatMapTests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Test;

import HeatMap.HeatMap;

public class RainbowTest {
	
	@Test
	public void test() {
		assertEquals(4, 4);
	}
	
	@Test
	public void Rainbow() {
		int size = 30;
		int frameSize = 300;
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
		frame.setSize(frameSize, frameSize);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(rainbow);
/*		try {
			Robot rb = new Robot();
			Color[] colors = new Color[size];
			for (int i=0; i<size; i++) {
				colors[i] = rb.getPixelColor(i * frameSize/size, i * frameSize/size);
			}
			
			for (int i=0; i<size-1; i++) {
				assertTrue(colors[i].getRed() != colors[i+1].getRed() && colors[i].getBlue() != colors[i+1].getBlue() && colors[i].getGreen() != colors[i+1].getGreen());
			}
		} catch (Exception AWTException) {
			fail("Failed to process pixels correctly");
		}*/
		
	}
}
