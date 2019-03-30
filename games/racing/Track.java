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
							sRange =	{0,					1},
							
							// Rates 		{Sm,	Md,		Lg}
							pGapRates =		{0,    	0,    	0},
							pWallRates =	{0,   	0,  	0},
							pDropRates =	{0.0,   0,  	0},
							pWidthRates =	{0.3,   0.3,   	0.3},
							pInclineRates =	{1.0,   0.0,   	0.0},
							pDeclineRates =	{1.0,   0.0,   	0.0}; 
					
	private static Group 	background,  midground,  platforms;
	private static int 		midgroundSpeed = 20;
	
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int trackLength)
	{	// Background
		background = new Group();
		background.setCache(true);
		background.setCacheHint(CacheHint.QUALITY);
		
		Polygon sky = newPolygon(new double[2], Resolution, 0);
		sky.setFill(Color.DEEPSKYBLUE);
		background.getChildren().add(sky);
		
		double [] position = {0, Resolution[1]*0.8},
				  dimension = {Resolution[0], Resolution[1]};
		Polygon mountain = newMountain(position, dimension, 1);
		mountain.setFill(Color.LIGHTSTEELBLUE);
		background.getChildren().add(mountain);
		
		// Midground
		midground = new Group();
		midground.setCache(true);
		midground.setCacheHint(CacheHint.SPEED);
		
		position = new double[]{0, Resolution[1]*0.8};
		dimension = new double[]{Resolution[0], Resolution[1]};
		Polygon hills = newMountain(position, dimension, 2);
		hills.setFill(Color.LIGHTBLUE);
		midground.getChildren().add(hills);
	
		// Platforms
		platforms = new Group();
		platforms.setCache(true);
		platforms.setCacheHint(CacheHint.SPEED);
		
		position = new double[]{-Resolution[0], Resolution[1]*0.8};
		while(position[0] < Resolution[0])
		{	dimension = new double[]{random(xRange, pWidthRates), Resolution[0]};
			Polygon runway = newPolygon(position, dimension, 0);
			runway.setFill(Color.ALICEBLUE);
			platforms.getChildren().add(runway);
		}
		
		while(position[0] < trackLength)
		{	position[0] += random(xRange, pGapRates, 0);
			position[1] += random(yRange, pDropRates, -random(yRange, pWallRates, 0));
			dimension = new double[]{random(xRange, pWidthRates, xRange[0]), Resolution[0]};
			double 	 topSlope = random(sRange, pDeclineRates, -random(sRange, pInclineRates, 0));
			Polygon platform = newPolygon(position, dimension, topSlope);
			platform.setFill(Color.ALICEBLUE);
			platforms.getChildren().add(platform);
		}
	}
	
	// Create Mountain
	private static Polygon newMountain(double[] position,  double[] mDimension, double peaks)
	{	double[] 	wRange = {0, mDimension[0]*0.1/peaks},
				 	sRange = {0, mDimension[1]/(mDimension[0]*0.4/peaks)},
				 	sRates = {1, 0, 0, 0.8};

		Polygon mountain = new Polygon();	
System.out.println("start Mountain:");				
		for(int i = 1; i <= peaks; i++)
		{	double[] start = position.clone();
System.out.println("   Peak XStart:"+ position[0]+" YStart:"+ position[1]);				
			while(position[0]+wRange[1] <= start[0]+mDimension[0]/peaks && position[1] <= start[1])
			{	double[] dimension = {random(wRange), Resolution[1]-position[1]};
				double 	 slope = -random(sRange, sRates);
				if(position[0]+dimension[0]/2 > start[0]+mDimension[0]*0.5/peaks)
				{	slope *= -1;
				}
				else if(position[0]+dimension[0]/2 >= start[0]+mDimension[0]*0.4/peaks)
				{	slope /= 3;
				}
System.out.println("	Slope:"+((int)(slope*10))/10.0 +" x:"+(int)position[0]+" y:"+(int)position[1]);				
				merge(mountain, newPolygon(position, dimension, slope));
			}
			double[] dimension = {mDimension[0]*i/peaks-position[0], Resolution[1]-position[1]};
			double	slope = (start[1]-position[1])/dimension[0];
			merge(mountain, newPolygon(position, dimension, slope));
System.out.println("   Peak XEnd:"+ position[0]+" YEnd:"+ position[1]);				
		}	
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
		midground.setTranslateX(cPosition[0]/midgroundSpeed);
		
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
