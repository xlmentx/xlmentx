package racing;

import java.util.ArrayList;
import java.util.Arrays;
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
							sRange =	{0,					1},
							
							// Rates 		{Sm,	Md,		Lg}
							pGapRates =		{0,    	0,    	0},
							pWallRates =	{0,   	0,  	0},
							pDropRates =	{0.0,   0,  	0},
							pWidthRates =	{0.3,   0.3,   	0.3},
							pInclineRates =	{0.0,   0.0,   	0.0},
							pDeclineRates =	{0.0,   0.0,   	0.0}; 
					
	private static Group 	background,  midground,  platforms;
	private static int 		mDistance = 30;
	
	private static double 		min = 0;
	
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int tLength)
	{	// Background
		background = new Group();
		
		Polygon sky = newPolygon(new double[2], Resolution, 0);
		Color sColor = Color.BLACK;
		sky.setFill(sColor);
		background.getChildren().add(sky);
		
		double [] position = {0, Resolution[1]*0.6},
				  dimension = {Resolution[0], position[1]};
		Polygon mountain = newMountain(position, dimension);
		mountain.setFill(Color.DIMGRAY);
		background.getChildren().add(mountain);
		background.setCache(true);
		background.setCacheHint(CacheHint.QUALITY);
		
		// Midground
		midground = new Group();
		position = new double[]{-Resolution[0]/mDistance, Resolution[1]*0.7};
		dimension = new double[]{Resolution[0], position[1]/2};
		while(position[0] < tLength/mDistance+Resolution[0])
		{	Polygon hill = newMountain(position, dimension);
			hill.setFill(Color.DARKGRAY);
			midground.getChildren().add(hill);
		}

		// Platforms
		platforms = new Group();
		platforms.setCache(true);
		platforms.setCacheHint(CacheHint.SPEED);
		
		position = new double[]{-Resolution[0], Resolution[1]*0.8};
		while(position[0] < Resolution[0])
		{	dimension = new double[]{random(xRange, pWidthRates), Resolution[0]};
			Polygon runway = newPolygon(position, dimension, 0);
			runway.setFill(Color.WHITE);
			platforms.getChildren().add(runway);
		}
		
		while(position[0] < tLength)
		{	position[0] += random(xRange, pGapRates, 0);
			position[1] += random(yRange, pDropRates, -random(yRange, pWallRates, 0));
			dimension = new double[]{random(xRange, pWidthRates, xRange[0]), Resolution[0]};
			double 	 topSlope = random(sRange, pDeclineRates, -random(sRange, pInclineRates, 0));
			Polygon platform = newPolygon(position, dimension, topSlope);
			platform.setFill(Color.WHITE);
			platforms.getChildren().add(platform);
		}
	}
	
	// Create Mountain
	private static Polygon newMountain(double[] position,  double[] dimension)
	{	double[] 	wRange = {0, dimension[0]*0.08},
				 	sRange = {0, dimension[1]/(dimension[0]*0.4)},
				 	sRates = {1, 0, 0, 0.8};

		Polygon mountain = new Polygon((int)position[0], Resolution[1], (int)position[0], position[1]);	
		double[] start = position.clone();		
		while(position[0]+wRange[1]*2 < start[0]+dimension[0] && position[1] <= start[1])
		{	double width = random(wRange),
				   slope = random(sRange, sRates)*Math.signum(position[0]-start[0]-dimension[0]/2);
			if(position[0] >= start[0]+dimension[0]*0.4 && slope < 0)
			{	slope /= 3;
			}
			mountain.getPoints().addAll(position[0]+=width, position[1]+=width*slope);
		}
		mountain.getPoints().addAll(position[0]=start[0]+dimension[0], position[1]=start[1]);
		mountain.getPoints().addAll(position[0], Resolution[1]);
		return mountain;
	}
		
	// Create Polygon
	private static Polygon newPolygon(double[] position, double[] dimension, double slope)
	{	Polygon p = new Polygon
		(	(int)position[0], 			position[1],
			position[0]+dimension[0], 	position[1]+dimension[0]*slope,
			position[0]+dimension[0], 	position[1]+dimension[1],
			(int)position[0], 			position[1]+dimension[1]
		);
	
		// Next Position
		position[0] += dimension[0];
		position[1] += dimension[0]*slope;
		return p;
	}
	
	
	
	// Random Values
	private static double random(double[] range)
	{	return random(range, new double[]{1});
	}
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
	{	// Midground
		midground.setTranslateX(cPosition[0]/mDistance);
		
		double translation = midground.getTranslateX();
		for(int i = 0; i < midground.getChildren().size(); i++)
		{	Polygon p = (Polygon)midground.getChildren().get(i);
			List<Double> points = p.getPoints();
			double[] domain = {points.get(0), points.get(points.size()-2)};
			if(domain[0]+translation < Resolution[0] && domain[1]+translation > 0)
			{	p.setVisible(true);
			}
			else
			{	p.setVisible(false);
			}
		}
		
		// Platforms
		platforms.setTranslateY(cPosition[1]);
		platforms.setTranslateX(cPosition[0]);
	}
	
	// Getters
	static Group getBackground()
	{	return background;
	}
	static Group getMidground()
	{	return midground;
	}
	static Group getPlatforms()
	{	return platforms;
	}
}
