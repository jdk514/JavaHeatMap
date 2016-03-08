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

	public LabeledHeatMap(double[][] data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Could be used to scale image
        int w = getWidth();
        int h = getHeight();
        
        //Define how the graph scales
        if (scale) {
            pixelSize = Math.min(w/data[0].length, h/data.length);
        }
        
        //Margin defining where graph begins
        /*int xMargin = Math.max(50, (w-(data[0].length*pixelSize) + 30)/2);
        int yMargin = Math.max(30, (h-(data.length*pixelSize)-25)/2);*/

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
                //g2.fill(new Rectangle2D.Double(xMargin+x, yMargin+y, pixelSize, pixelSize));
                g2.fill(new Rectangle2D.Double(x, y, pixelSize, pixelSize));
            }
        }
        
        int xMargin = 0;
        int yMargin = 0;
        //Draw Scale - 10 intervals
        /*int scaleInterval = 15;
        if (this.quartile) {
        	scaleInterval = 4;
        }
    	int x = xMargin + data[0].length*pixelSize + 15;
    	//Set y corr so scale is centered
    	int y = (yMargin+data.length*pixelSize)/2 + (scaleInterval / 2)*2*pixelSize;
        for(int i=(int)this.min; i<=(int)this.max; i+=(int)Math.ceil((this.max-this.min)/scaleInterval)) {
        	Color currColor = getGradientColor(i);
        	g2.setPaint(currColor);
        	g2.fill(new Rectangle2D.Double(x, y, pixelSize, pixelSize*2));
        	g2.setPaint(Color.BLACK);
        	g2.drawString(Integer.toString(i), x + 15, y+pixelSize+5);
        	g2.drawLine(x, y, x+pixelSize-1, y);
        	y = y - pixelSize*2;
        }*/
        
        //Set Title
        g2.setPaint(Color.black);
        g2.setFont(defaults.getFont("TextField.font"));
        g2.setFont(getTitleFont());
        g2.drawString(title, this.getWidth()/2 - title.length()*3, yMargin/2);
        g2.setFont(defaults.getFont("TextField.font"));
        
        //Draw Grid
        if (grid) {
	        for(int i=0; i<10; i++) {
	        	float xcoor = (float) (i*(data[0].length*pixelSize/10.0));
	        	float ycoor = (float) ((i+1)*data.length*pixelSize/10.0);
	            g2.setPaint(Color.white);
	        	g2.drawLine((int)(xMargin + xcoor), yMargin, (int)(xMargin + xcoor), yMargin + data.length*pixelSize);
	        	g2.drawLine(xMargin, (int) (yMargin + ycoor), xMargin + data[0].length*pixelSize, (int) (yMargin + ycoor));
	        	g2.setPaint(Color.black);
	        	g2.drawString("0."+i, xMargin + xcoor - 7, yMargin + data.length*pixelSize + 20);
	        	g2.drawString("0."+(9-i), xMargin - 30, yMargin + ycoor + 3);
	            g2.drawLine((int)(xcoor), 0, (int)(xcoor), data.length*pixelSize);
	        	g2.drawLine(0, (int) (ycoor), data[0].length*pixelSize, (int) (ycoor));
	        	g2.setPaint(Color.black);
	        	//The label axis for the grid
	        	g2.drawString("0."+i, xMargin + xcoor - 7, yMargin + data.length*pixelSize + 20);
	        	g2.drawString("0."+(9-i), xMargin - 30, yMargin + ycoor + 3);
	        }
        }
        
       /* if (margin) {
	        //Draw X axis line
	        g2.drawLine(xMargin, yMargin + data.length*pixelSize + 40, xMargin, yMargin + data.length*pixelSize + 50);
	        g2.drawLine(xMargin + data[0].length*pixelSize, yMargin + data.length*pixelSize + 40, xMargin + data[0].length*pixelSize, yMargin + data.length*pixelSize + 50);
	        g2.drawLine(xMargin + data[0].length*pixelSize/2, yMargin + data.length*pixelSize + 45, xMargin + data[0].length*pixelSize/2, yMargin + data.length*pixelSize + 50);
	        g2.drawLine(xMargin, yMargin + data.length*pixelSize + 45, xMargin + data[0].length*pixelSize, yMargin + data.length*pixelSize + 45);
	        g2.drawString(data[0].length * 10 + "m", xMargin + data[0].length*pixelSize/2 - 20, yMargin + data.length*pixelSize +65);
	        
	        //Draw Y axis line
	        g2.drawLine(xMargin-45, yMargin, xMargin-45, yMargin + data.length*pixelSize);
	        g2.drawLine(xMargin-40, yMargin + data.length*pixelSize, xMargin-50, yMargin + data.length*pixelSize);
	        g2.drawLine(xMargin-40, yMargin, xMargin-50, yMargin);
	        g2.drawLine(xMargin-45, yMargin + data.length*pixelSize/2, xMargin-50, yMargin + data.length*pixelSize/2);
        }*/
    
    	//Draw X axis line
        g2.drawLine(0, data.length*pixelSize + 40, 0, data.length*pixelSize + 50);
        g2.drawLine(data[0].length*pixelSize, data.length*pixelSize + 40, data[0].length*pixelSize, data.length*pixelSize + 50);
        g2.drawLine(data[0].length*pixelSize/2, data.length*pixelSize + 45, data[0].length*pixelSize/2, data.length*pixelSize + 50);
        g2.drawLine(0, data.length*pixelSize + 45, data[0].length*pixelSize, data.length*pixelSize + 45);
        
        //Draw Y axis line
        g2.drawLine(45, 0, 45, yMargin + data.length*pixelSize);
        g2.drawLine(40, data.length*pixelSize, 50, data.length*pixelSize);
        g2.drawLine(40, 0, 50, yMargin);
        g2.drawLine(45, data.length*pixelSize/2, 50, data.length*pixelSize/2);
        
        //Rotate to draw Y axis Label
        Font font = new Font(null, Font.PLAIN, 12);    
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2.setFont(rotatedFont);
        g2.drawString(data.length*10 + "m", xMargin-60, yMargin + data.length*pixelSize/2 + 20);
        g2.setFont(defaults.getFont("TextField.font"));
	}

}
