package racing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
					
	private static Group 	background,  midground, foreground, platforms;
			
	
	private static Color 	sColor = Color.DEEPSKYBLUE,
							fColor = Color.WHITE,
							mColor = Color.GREEN,
							gColor = Color.WHEAT;
	
	
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int tLength)
	{	
		// new Background
		background = new Group();
		double 	fHeight = 0;
		Stop	s1 = new Stop(0.5*fHeight, sColor), 
				s2 = new Stop(0.5, fColor),
				g1 = new Stop(0.5, blend(blend(mColor, sColor, 0.3), fColor, 0.2)), 
				g2 = new Stop(1, blend(mColor, fColor, 0));
		LinearGradient sFill = new LinearGradient(0, 0, 0, 1, true, null, s1, s2, g1, g2);
		background.getChildren().add(new Rectangle(Resolution[0], Resolution[1], sFill));
		
		// mountain
		for(double i = 0, layers = 1, j = 1; i < layers; i++, j*=-1)
		{	
			// mountain layer
			double[] shift = {(int)(i+1)/2*j/layers-1, 0.5*(1+i/layers)},
					 position = {Resolution[0]*shift[0],  Resolution[1]*shift[1]},
					 dimension = {Resolution[0], Resolution[1]/3};
			Polygon	 mLayer = new Polygon();	
System.out.println(0.2+0.8*i/layers);		
			while(position[0]-Resolution[0] <= tLength*Math.pow(0.2+0.8*i/layers, 3)) 
			{	double[] 	start = position.clone(),
							sRange = {0, dimension[1]/(dimension[0]*0.4)},
							sRates = {1, 0, 0, 0.8};
				while(position[0] <= start[0]+dimension[0]*0.9 && position[1] <= start[1])
				{	mLayer.getPoints().addAll(position[0], position[1]);
					double 	width = random(dimension[0]*0.1),
			   	   			slope = random(sRange, sRates)*Math.signum(position[0]-start[0]-dimension[0]/2);
					if(position[0] >= start[0]+dimension[0]*0.4 && slope < 0)
					{	slope /= 3; 
					}
					position = new double[]{position[0]+width, position[1]+width*slope};
				}
				mLayer.getPoints().addAll(position[0] = start[0]+dimension[0], position[1] = start[1]);
			}	
			// TO DO
			// fog = 0, g:(B,0.2, S,0.3, F,0.3) g2:(F,0)	m:(B,0.3, S,0.3, F,0.3)
			// fog = 1, g:(B,0.2, S,0.3, F,1) 	g2:(F,0.5) 	m:(B,0.3, S,0.3, F,1)
				
			// Static
			double 	distance = 1-i/layers;
			Color	sumit = blend(blend(mColor, Color.BLACK, distance*0.2), sColor, distance*0.3),
					base = blend(sumit, fColor, distance*0.2);
					
			Stop[] 	colors = 
			{	new Stop(shift[1]-0.5*(1-fHeight), sumit), 
				new Stop(shift[1], base)
			};
			
			mLayer.setFill(new LinearGradient(0, 0, 0, Resolution[1], false, null, colors));
			background.getChildren().add(mLayer);
		}
	
		// Platforms
		platforms = new Group();
		double[] pPosition = {-Resolution[0]/2, Resolution[1]*0.8},
				 pDimension = {Resolution[0]*2, Resolution[1]/2};
		platforms.getChildren().add(newPolygon(pPosition, pDimension, 0));
		
		while(pPosition[0] < tLength)
		{	pPosition[0] += random(xRange, pGapRates, 0);
			pPosition[1] += random(yRange, pDropRates, -random(yRange, pWallRates, 0));
			pDimension = new double[] {random(xRange, pWidthRates, xRange[0]), Resolution[0]};
			double slope = random(sRange, pDeclineRates, -random(sRange, pInclineRates, 0));
			platforms.getChildren().add(newPolygon(pPosition, pDimension, slope));
		}
	}
	
	// Create Polygon
	private static Polygon newPolygon(double[] position, double[] dimension, double slope)
	{	Polygon p = new Polygon
		(	(int)position[0], 			position[1],
			position[0]+dimension[0], 	position[1]+dimension[0]*slope,
			position[0]+dimension[0], 	position[1]+dimension[1],
			(int)position[0], 			position[1]+dimension[1]
		);
		position[0] += dimension[0];
		position[1] += dimension[0]*slope;
		
		p.setFill(gColor);
		return p;
	}
	
	private static Color blend(Color a, Color b, double bR, Color c, double cR)
	{	return blend(blend(a, b, bR), c, cR);
	}
	private static Color blend(Color a, Color b, double ratio)
	{	return Color.rgb
		(	(int)(255*(a.getRed()+(b.getRed()-a.getRed())*ratio)), 
			(int)(255*(a.getGreen()+(b.getGreen()-a.getGreen())*ratio)), 
			(int)(255*(a.getBlue()+(b.getBlue()-a.getBlue())*ratio))
		);
	}
	
	// Random Values
	private static double random(double range)
	{	return random(new double[]{0,range}, new double[]{1});
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
	{	// Background
		List<Node> nodes = background.getChildren();
		for(int i = 1, s = nodes.size(); i < s; i++)
		{	nodes.get(i).setTranslateX(cPosition[0]*Math.pow(0.2+0.8*(double)(i-1)/s, 3));
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
	static Group getForeground()
	{	return foreground;
	}
	static Group getPlatforms()
	{	return platforms;
	}
}
