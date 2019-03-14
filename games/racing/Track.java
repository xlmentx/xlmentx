package racing;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Track
{	// Track Variables
	private static 			
	final double[]			Resolution ={Game.getResolution()[0], Game.getResolution()[1]},
							// Ranges		{Min,				Max}
							xShiftRange =	{Resolution[0]*0.1,	Resolution[0]*0.8},
							yShiftRange =	{Resolution[1]*0.1,	Resolution[1]*0.8},
							widthRange =	{Resolution[0]*0.1,	Resolution[0]},
							slopeRange =	{0,					0.8},
							// Rates 		{Sm,	Md,		Lg}
							pGapRates =		{0,    	0,    	0},
							pWallRates =	{0,   	0,  	0},
							pDropRates =	{0.0,   0,  	0},
							pWidthRates =	{0.3,   0.3,   	0.3},
							pInclineRates =	{0.0,   0.0,   	0.1},
							pDeclineRates =	{0.0,   0.0,   	0.1}; 
					
	private static ArrayList<Polygon> background, platforms;
	
	// Private Constructor
	private Track()
	{}
	
	// Creates New Track
	static void newTrack(int trackLength)
	{	// Background
		background = new ArrayList<>();
		Color skyColor = Color.DEEPSKYBLUE;
		background.add(newPolygon(Resolution, skyColor));
		Color mountainColor = Color.LIGHTSTEELBLUE;
		double[] 			  //{Sm,	Md,		Lg,		xLg}
				 mWidthRates = 	{1, 	0, 		0,		0},
				 mInclineRates ={1, 	0.0, 	0,		0.7},
				 mDeclineRates ={1, 	0.0,	0,		0.7},
				 position = 	{0, Resolution[1]*0.7};
		while(position[0] < trackLength)
		{	double[] dimension = {random(widthRange, mWidthRates)/3, Resolution[1]-position[1]};
			double 	 topSlope = -random(slopeRange, mInclineRates);
			if(position[0] >= Resolution[0]*0.45)
			{	topSlope = random(slopeRange, mDeclineRates);
			}
			background.add(newPolygon(position, dimension, topSlope, mountainColor));
		}
		
		// Platforms
		platforms = new ArrayList<>();
		Color pColor = Color.ALICEBLUE;
		position = new double[]{-Resolution[0], Resolution[1]*0.8};
		while(position[0] < Resolution[0])
		{	double[] dimension = {random(widthRange, pWidthRates), Resolution[0]};
			platforms.add(newPolygon(position, dimension, pColor));
		}
		while(position[0] < trackLength)
		{	double[] shift = {random(xShiftRange, pGapRates, 0), -random(yShiftRange, pWallRates, 0)};
					 shift[1] = random(yShiftRange, pDropRates, shift[1]);
			double[] dimension = {random(widthRange, pWidthRates), Resolution[0]};
			double 	 topSlope = random(slopeRange, pDeclineRates, -random(slopeRange, pInclineRates));
			platforms.add(newPolygon(position, shift, dimension, topSlope, pColor));
		}
	
	}
	
	// Create Polygon
	private static Polygon newPolygon(double[] dimension, Color color)
	{	return newPolygon(new double[2], dimension, color);
	}
	private static Polygon newPolygon(double[] position, double[] dimension, Color color)
	{	return newPolygon(position, dimension, 0, color);
	}
	private static Polygon newPolygon(double[] position, double[] dimension, double topSlope, Color color)
	{	return newPolygon(position, new double[2], dimension, topSlope, color);
	}	
	private static Polygon newPolygon(double[] position, double[] shift, double[] dimension, double topSlope, Color color)
	{	// Shift
		position[0] += shift[0];
		position[1] += shift[1];
	
		// Polygon
		double[] xPoints = {(int)position[0], position[0]+dimension[0], position[0]+dimension[0], (int)position[0]};
		double[] yPoints = {position[1], position[1]+dimension[0]*topSlope, position[1]+dimension[1], position[1]+dimension[1]};
		Polygon p = new Polygon(xPoints, yPoints, color);

		// Next Position
		position[0] += dimension[0];
		position[1] += dimension[0]*topSlope;
		
		return p;
	}
	
	// Random Values
	private static double random(double[] range, double[] rates)
	{	return random(range, rates, range[0]);
	}
	private static double random(double[] range, double[] rates, double defaultValue)
	{	double 	randomValue = defaultValue,
				rangeSegments = (range[1]-range[0])/rates.length;
		Random r = new Random();
		for(int i = 0; i < rates.length; i++)
		{	if(r.nextDouble() <= rates[i])
			{	double randomSegment = r.nextInt((int)(rangeSegments*100))/100.00; 
				randomValue = range[0]+rangeSegments*i+randomSegment;
			}
		}
		return randomValue;
	}	
	
	// Track Update
	static void translate(double[] cPosition, GraphicsContext gc) 
	{	// BackGround
		for(int i = 0; i < background.size(); i++)
		{	background.get(i).draw(new double[2], gc);
		}
		
		// Platforms
		for(int i = 0; i < platforms.size(); i++)
		{	platforms.get(i).draw(cPosition, gc);
		}
	}
	
	// Getters
	static public ArrayList<Polygon> getPlatforms()
	{	return platforms;
	}
}
