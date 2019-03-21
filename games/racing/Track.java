package racing;

import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Track
{	// Track Variables
	private static 			
	final double[]			Resolution ={Game.getResolution()[0], Game.getResolution()[1]},
							
							// Ranges	{Min,				Max}
							xRange =	{Resolution[0]*0.1,	Resolution[0]*0.8},
							yRange =	{Resolution[1]*0.1,	Resolution[1]*0.8},
							sRange =	{0,					0.8},
							
							// Rates 		{Sm,	Md,		Lg		Max}
							pGapRates =		{0,    	0,    	0,		0	},
							pWallRates =	{0,   	0,  	0,		0	},
							pDropRates =	{0.0,   0,  	0,		0	},
							pWidthRates =	{0.3,   0.3,   	0.3,	0	},
							pInclineRates =	{0.0,   0.0,   	0.1,	0	},
							pDeclineRates =	{0.0,   0.0,   	0.1,	0}; 
					
	private static Group 	background, 	platforms;
	
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int trackLength)
	{	// Background
		Polygon sky = newPolygon(new double[2], Resolution, 0);
		sky.setFill(Color.DEEPSKYBLUE);
		background = new Group();
		background.getChildren().add(sky);
		
		Polygon mountain = new Polygon();
		double[] position = {0, Resolution[1]*0.7},
				 // Rates 		{Sm,	Md,		Lg		Max}
				 mWidthRates = 	{1, 	0,		0,		0},
				 mInclineRates ={1, 	0, 		0, 		0.7};
		while(position[0] < Resolution[0])
		{	double[] dimension = {random(xRange, mWidthRates)/5, Resolution[1]-position[1]};
			double 	 topSlope = -random(sRange, mInclineRates)*1.3;
			if(position[0] >= Resolution[0]*0.45)
			{	topSlope = -random(sRange, mInclineRates)/2;
			}
			if(position[0] >= Resolution[0]*0.5)
			{	topSlope = random(sRange, mInclineRates)*1.3;
			}
			merge(mountain, newPolygon(position, dimension, topSlope));
		}
		
		mountain.setFill(Color.LIGHTSTEELBLUE);
		background.getChildren().add(mountain);
		background.setCache(true);
		background.setCacheHint(CacheHint.QUALITY);
		
		// Platforms
		platforms = new Group();
		position = new double[]{-Resolution[0], Resolution[1]*0.8};
		while(position[0] < Resolution[0])
		{	double[] dimension = {random(xRange, pWidthRates), Resolution[0]};
			Polygon runway = newPolygon(position, dimension, 0);
			runway.setFill(Color.ALICEBLUE);
			platforms.getChildren().add(runway);
		}
		while(position[0] < trackLength)
		{	position[0] += random(xRange, pGapRates, 0);
			position[1] += random(yRange, pDropRates, -random(yRange, pWallRates, 0));
			double[] dimension = {random(xRange, pWidthRates), Resolution[0]};
			double 	 topSlope = random(sRange, pDeclineRates, -random(sRange, pInclineRates));
			Polygon platform = newPolygon(position, dimension, topSlope);
			platform.setFill(Color.ALICEBLUE);
			platforms.getChildren().add(platform);
		}
		platforms.setCache(true);
		platforms.setCacheHint(CacheHint.SPEED);
	}
	
	// Create Polygon
	private static Polygon newPolygon(double[] position, double[] dimension, double topSlope)
	{	Polygon p = new Polygon
		(	(int)position[0], 			position[1],
			position[0]+dimension[0], 	position[1]+dimension[0]*topSlope,
			position[0]+dimension[0], 	position[1]+dimension[1],
			(int)position[0], 			position[1]+dimension[1]
		);
	
		// Next Position
		position[0] += dimension[0];
		position[1] += dimension[0]*topSlope;
		return p;
	}
	
	// Merge Polygons
	private static Polygon merge(Polygon A, Polygon B)
	{	List<Double> aPoints = A.getPoints(),
			 		 bPoints = B.getPoints();
		
		// Overlap Check
		if(aPoints.size() > 0 && aPoints.get(aPoints.size()/2-1).equals(bPoints.get(1)))
		{	bPoints = bPoints.subList(2, bPoints.size()-2);
		}
		
		aPoints.addAll(aPoints.size()/2, bPoints);
		return A;
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
	static void translate(double[] cPosition) 
	{	// Platforms
		platforms.setTranslateY(cPosition[1]);
		platforms.setTranslateX(cPosition[0]);
	}
	
	// Getters
	static Group getBackground()
	{	return background;
	}
	static Group getPlatforms()
	{	return platforms;
	}
}
