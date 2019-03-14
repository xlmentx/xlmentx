package racing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Polygon 
{	private double[] xPoints = new double[2],
		 			 yPoints = new double[2];
	private Color 	 color = null;
	
	Polygon(double[] xPoints, double[] yPoints, Color color)
	{	this.xPoints = xPoints;
		this.yPoints = yPoints;
		this.color = color;
	}
	
	public void draw(double[] cPosition, GraphicsContext gc)
	{	double[] xTranslation = new double[xPoints.length],
	 			 yTranslation = new double[yPoints.length];
		for(int i = 0; i < xPoints.length; i++)
		{	xTranslation[i] = xPoints[i]+cPosition[0];
			yTranslation[i] = yPoints[i]+cPosition[1];
		}
		gc.setFill(color);
		gc.fillPolygon(xTranslation, yTranslation, xPoints.length);
	}
	
	// Getters
	public double[] getXPoints()
	{	return xPoints;
	}
	public double[] getYPoints()
	{	return yPoints;
	}
}
