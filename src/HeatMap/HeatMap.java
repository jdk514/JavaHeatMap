package HeatMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class HeatMap extends JPanel{
	public int pixelSize;
	public double[][] data;
	public Boolean scale=true;
	public double min;
	public double max;
	public double range;
	public String title;
	private boolean whiteZero;
	
	public HeatMap(double[][] data, boolean scale) {
		this.pixelSize = 10;
		this.data = data;
		max = -Double.MAX_VALUE;
		min = Double.MAX_VALUE;
		for (int i=0; i<data.length; i++) {
			for(int j=0; j<data[i].length; j++) {
				max = Math.max(max, data[i][j]);
				min = Math.min(min, data[i][j]);
			}
		}
		this.range = max-min;
		this.whiteZero = true;
		this.scale = scale;
	}

	public HeatMap(int pixelSize, double[][] data, String title){
		this.pixelSize = pixelSize;
		this.data = data;
		this.title = title;
		this.whiteZero = true;
		max = -Double.MAX_VALUE;
		min = Double.MAX_VALUE;
		for (int i=0; i<data.length; i++) {
			for(int j=0; j<data[i].length; j++) {
				max = Math.max(max, data[i][j]);
				min = Math.min(min, data[i][j]);
			}
		}
		range = max-min;
	}
	
	public void draw() {
    	JPanel panel = new JPanel();
        panel.add(this);
        panel.setSize(data[0].length*pixelSize,data.length*pixelSize);
        panel.setVisible(true);
	}
	
	public void save(String filepath) {
		this.setSize(data[0].length*pixelSize, data.length*pixelSize);
    	BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
    	this.printAll(image.getGraphics());
        try {
        	ImageIO.write(image, "png", new File(filepath));
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Could be used to scale image
        int w = getWidth()-1;
        int h = getHeight()-1;
        System.out.println(w);
        System.out.println(h);
        
        //Define how the graph scales
        if (scale) {
            pixelSize = Math.min(w/data[0].length, h/data.length);
        }

        // Mark data data.
        for(int i = 0; i < data.length; i++) {
            for(int j=0; j<data[i].length; j++) {
                if (Double.isNaN(data[i][j])) {
                    g2.setPaint(Color.black);
                } else {
                    Color currColor = getGradientColor(data[i][j]);
                    g2.setPaint(currColor);
                }
                double x = j*pixelSize;
                double y = i*pixelSize;
                g2.fill(new Rectangle2D.Double(x, y, pixelSize, pixelSize));
            }
        }        
    }
	
	//Determine the color for the given point
	public Color getGradientColor(double value) {
    	double place;
    	Float hue;
    	
    	// Return white for NaNs
    	if(Double.isNaN(value) || value > max || value < min) {
    		return(Color.white);
    	}

    	float percentage = 0.0f;
		place = (value-min);
    	hue = (float) (1-(place/range));
    	hue = 1-hue;
    	float ratio = 1.0f/11.0f;
    	
    	if (whiteZero && Double.compare(value, 0) == 0) {
			return Color.WHITE;
    	}
    	
    	if (hue.compareTo(1.0f/11.0f) <= 0) {
    		try {
    			percentage = hue/ratio;
    			return new Color(255, 0, Math.min(255, (int) (128 * (1 + percentage))));
    		} catch (Exception e) {
    			e.printStackTrace();
    			return Color.WHITE;
    		}
    	} else if (hue.compareTo(3.0f/11.0f) <= 0) {
    		percentage = (hue - ratio)/(2 * ratio);
    		return new Color(Math.max(0, (int) (255 - (255 * percentage))), 0, 255);
    	} else if (hue.compareTo(5.0f/11.0f) <= 0) {
    		percentage = (hue - 3 * ratio)/(2 * ratio);
    		return new Color(0, Math.min(255, (int) (255 * percentage)), 255);
    	} else if (hue.compareTo(7.0f/11.0f) <= 0) {
    		percentage = (hue - 5 * ratio)/(2 * ratio);
    		return new Color(0, 255, Math.max(0, (int) (255 - (255 * percentage))));
    	} else if (hue.compareTo(9.0f/11.0f) <= 0) {
    		percentage = (hue - 7 * ratio)/(2 * ratio);
    		return new Color(Math.min(255, (int) (255 * percentage)), 255, 0);
    	} else {
    		percentage = (hue - 9 * ratio)/(2 * ratio);
    		return new Color(255, Math.max(0, (int) (255 - (255 * percentage))), 0);
    	}
    	
    }
    
	//Determine the correct font for the title given margin
    public Font getTitleFont() {
    	int yMargin = (this.getHeight()-(data.length*pixelSize))/2;
    	int fontSize = Math.min(16, yMargin);
    	return new Font(this.getFont().getFontName(), Font.BOLD, fontSize);
    	
    }
}
