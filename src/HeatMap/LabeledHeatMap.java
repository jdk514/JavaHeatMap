package HeatMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class LabeledHeatMap extends HeatMap {
	boolean grid;
	boolean margin;
	int scaleInterval;
	String yAxis;
	String xAxis;
	int xMin;
	int xMax;
	int xInterval;
	int yMin;
	int yMax;
	int yInterval;
	int gridInterval;

	public LabeledHeatMap(double[][] data, boolean scale, String yAxis, String xAxis, String title) {
		super(data, scale);
		this.grid = true;
		this.margin = true;
		this.title = title;
		this.scaleInterval = 7;
		this.yAxis = yAxis;
		this.xAxis = xAxis;	
		this.gridInterval = 10;
	}
	
	public LabeledHeatMap(double[][] data, boolean scale, String yAxis, String xAxis, String title, boolean grid, int gridInterval, int scaleInterval) {
		super(data, scale);
		this.grid = grid;
		this.gridInterval = gridInterval;
		
		this.scaleInterval = scaleInterval;
		
		this.title = title;
		this.yAxis = yAxis;
		this.xAxis = xAxis;
		
		this.margin = true;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		//super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Could be used to scale image
        int w = getWidth();
        int h = getHeight();
        
        //Margins
        int xMargin = 75;
        int yMargin = 75;
        
        //Define how the graph scales
        if (scale) {
            pixelSize = Math.min((w-2*xMargin)/data[0].length, (h-2*yMargin)/data.length);
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
                double x = j*pixelSize + xMargin;
                double y = i*pixelSize + yMargin;
                //g2.fill(new Rectangle2D.Double(xMargin+x, yMargin+y, pixelSize, pixelSize));
                g2.fill(new Rectangle2D.Double(x, y, pixelSize, pixelSize));
            }
        }
        
        //Draw Scale - 10 intervals
    	int x = xMargin + data[0].length*pixelSize + 20;
    	//Set y corr so scale is centered
    	int scaleHeight = 2;
    	while (scaleInterval * scaleHeight * pixelSize >= data[0].length*pixelSize && scaleHeight > 1) {
    		scaleHeight -= 1;
    	}
    	
    	//Draw a scale for color interval
    	if (scale) {
	    	//Initial y coordinate needs to be placed at yMargin + 1/2 the height of the grid + half the height of the scale
	    	int y = yMargin + data[0].length*pixelSize/2 + (scaleInterval*scaleHeight*pixelSize/2);
	        for(int i=(int)min; i<=(int)max; i+=(int)Math.ceil((max-min)/scaleInterval)) {
	        	Color currColor = getGradientColor(i);
	        	g2.setPaint(currColor);
	        	g2.fill(new Rectangle2D.Double(x, y - scaleHeight * pixelSize, pixelSize, pixelSize*scaleHeight));
	        	g2.setPaint(Color.BLACK);
	        	g2.drawString(Integer.toString(i), x + pixelSize + 15, y - scaleHeight * pixelSize / 3);
	        	if (i < (int)max - (int)Math.ceil((max-min)/scaleInterval) && i != min) {
	        		g2.drawLine(x, y, x+pixelSize-1, y);
	        	}
	        	y = y - pixelSize*scaleHeight;
	        }
    	}
        
        //Set Title
        g2.setPaint(Color.black);
        Font font = new Font(null, Font.BOLD, Math.max(12, 2*pixelSize));
        g2.setFont(font);
        int stringWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, xMargin + (data.length * pixelSize)/2 - stringWidth/2, yMargin - 15);
        g2.setFont(defaults.getFont("TextField.font"));
        
        //Draw Grid
        if (grid) {
        	gridInterval = Math.min(data.length, gridInterval);
	        for(int i=0; i<gridInterval; i++) {
	        	float xcoor = (float) (i*(data[0].length*pixelSize/gridInterval));
	        	float ycoor = (float) ((i+1)*data.length*pixelSize/gridInterval);
	            g2.setPaint(Color.black);
	        	g2.drawLine((int)(xMargin + xcoor), yMargin, (int)(xMargin + xcoor), yMargin + data.length*pixelSize);
	        	g2.drawLine(xMargin, (int) (yMargin + ycoor), xMargin + data[0].length*pixelSize, (int) (yMargin + ycoor));
/*	        	g2.setPaint(Color.black);
	        	g2.drawString("0."+i, xMargin + xcoor - 7, yMargin + data.length*pixelSize + 20);
	        	g2.drawString("0."+(9-i), xMargin - 30, yMargin + ycoor + 3);
	            g2.drawLine((int)(xcoor), 0, (int)(xcoor), data.length*pixelSize);
	        	g2.drawLine(0, (int) (ycoor), data[0].length*pixelSize, (int) (ycoor));
	        	g2.setPaint(Color.black);
	        	//The label axis for the grid
	        	g2.drawString("0."+i, xMargin + xcoor - 7, yMargin + data.length*pixelSize + 20);
	        	g2.drawString("0."+(9-i), xMargin - 30, yMargin + ycoor + 3);*/
	        }
	        
	        //Draw the enclosing lines
	        g2.drawLine(xMargin + data[0].length * pixelSize, yMargin, xMargin + data[0].length * pixelSize, yMargin + data.length * pixelSize);
	        g2.drawLine(xMargin, yMargin, xMargin + data[0].length * pixelSize, yMargin);
        }
        
        //Draw the axis lines
    	//Draw X axis line
		g2.setPaint(Color.black);
		g2.drawLine(xMargin, yMargin + data.length*pixelSize + 20, xMargin + data[0].length*pixelSize, yMargin + data.length*pixelSize + 20);
		//Draw end lines
		g2.drawLine(xMargin, yMargin + data.length*pixelSize + 15, xMargin, yMargin + data.length*pixelSize + 25);
		g2.drawLine(xMargin + data[0].length*pixelSize, yMargin + data.length*pixelSize + 15, xMargin + data[0].length*pixelSize, yMargin + data.length*pixelSize + 25);
		//Draw center line
		g2.drawLine(xMargin + data[0].length*pixelSize/2, yMargin + data.length*pixelSize + 15, xMargin + data[0].length*pixelSize/2, yMargin + data.length*pixelSize + 20);
		//Draw length of x axis
		font = new Font(null, Font.PLAIN, Math.max(12, pixelSize));
		g2.setFont(font);
		g2.drawString(xAxis, xMargin + data[0].length*pixelSize/2 - 20, yMargin + data.length*pixelSize + 35 + pixelSize/2);
		
		//Draw Y axis line
		g2.drawLine(xMargin-20, yMargin, xMargin-20, yMargin + data.length*pixelSize);
		//Draw end lines
		g2.drawLine(xMargin-25, yMargin + data.length*pixelSize, xMargin-15, yMargin + data.length*pixelSize);
		g2.drawLine(xMargin-25, yMargin, xMargin-15, yMargin);
		//Draw center line
		g2.drawLine(xMargin-20, yMargin + data.length*pixelSize/2, xMargin-15, yMargin + data.length*pixelSize/2);
        

        
        //Rotate to draw Y axis Label
        font = new Font(null, Font.PLAIN, Math.max(12, pixelSize));    
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2.setFont(rotatedFont);
        //Draw length of y axis
        g2.drawString(yAxis, xMargin-25, yMargin + data.length*pixelSize/2 + 20);
        g2.setFont(defaults.getFont("TextField.font"));
	}

}
