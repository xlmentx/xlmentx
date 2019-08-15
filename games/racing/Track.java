package racing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
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
							mColor = Color.WHEAT.darker().saturate(),
							gColor = Color.WHEAT;
	
	
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int tLength)
	{	// Background
		background = new Group();	
		double 	fog = 1;					
		Stop[]	sColors = new Stop[10]; 
		sColors[0] = new Stop(0.5*fog, sColor);
		for(int i = 0; i < sColors.length-1; i++)
		{	double 	z = 1-i/(sColors.length-2.0),
					y = 1.75-Math.sqrt(1.56-Math.pow(1-z, 2));
			Color	c = blend(blend(mColor, sColor, z*z), fColor, z*z);
			sColors[i+1] = new Stop(y, c);	
		}
		LinearGradient sFill = new LinearGradient(0, 0, 0, 1, true, null, sColors);
		background.getChildren().add(new Rectangle(Resolution[0], Resolution[1], sFill));

		// Mountain 
		for(double i = 0, layers = 5, j = 1; i < layers; i++, j*=-1)
		{	
			// Mountain Layer
			double[] mShift = {(int)(i+1)/2*j/layers-1, 1.75-Math.sqrt(1.56-Math.pow((i+1)/(layers+1), 2))},
					 mPosition = {Resolution[0]*mShift[0], Resolution[1]*mShift[1], 1-(i+1)/(layers+1)},
					 pDimension = {Resolution[0], Resolution[1]/3};
			Polygon  mLayer = newMountainLayer(mPosition, pDimension, tLength);	


			Image 	mShader = new Image("image/scenery/MountainShader.png"),
					mFill = blend(mShader, mColor, 0.95); 
			mFill = blend(mFill, sColor, 1.367-Math.sqrt(2-Math.pow(mPosition[2]+0.365,2)));
			
			
			
			
			
			
			
			
			
			
			
			
System.out.println("Fog:");			
			mFill = blend(mFill, fColor, Math.pow(mPosition[2],2), 0.55, 0.5);
System.out.println("");			
			
			double  peaks = pDimension[0]/mLayer.getLayoutBounds().getWidth();
			mLayer.setFill(new ImagePattern(mFill, 0, 0, peaks, 1, true));
			
			
						
			Color	summit = blend(blend(mColor, Color.BLACK, 0.1), sColor, Math.pow(mPosition[2],2)),	
					base = blend(summit, fColor, Math.pow(mPosition[2],2));
			Stop[] 	stops = {new Stop(0.5*fog, summit), new Stop(mShift[1], base),};
			LinearGradient colors = new LinearGradient(0, 0, 0, Resolution[1], false, null, stops);
			
			mLayer.setFill(colors);
			
			
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
	
	// Create mLayer
	private static Polygon newMountainLayer(double[] position, double[] pDimension, int tLength)
	{	Polygon	 mLayer = new Polygon();	
		while(position[0] < tLength*Math.pow(1-position[2], 3)+Resolution[0]) 
		{	double[] 	pStart = position.clone(),
						sRange = {0, pDimension[1]/pDimension[0]/0.35},
						sRates = {1, 0, 0, 0.8};
			while(position[0] <= pStart[0]+pDimension[0]*0.8 && position[1] <= pStart[1])
			{	mLayer.getPoints().addAll(position[0], position[1]);
				double 	width = random(pDimension[0]*0.1),
						slope = random(sRange, sRates)*Math.signum(position[0]-pStart[0]-pDimension[0]/2);
				if(position[0] >= pStart[0]+pDimension[0]*0.4 && slope < 0)
				{	slope /= 3; 
				}
				position = new double[]{position[0]+width, position[1]+width*slope, position[2]};
			}
			mLayer.getPoints().addAll(position[0] = pStart[0]+pDimension[0], position[1] = pStart[1]);
		}	
		return mLayer;
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
		
			
		p.setFill(mColor);
		return p;
	}
	
	// Blend Colors
	private static Image blend(Image i, Color c, double opacity)
	{	PixelReader iReader = i.getPixelReader(); 
		WritableImage bImage = new WritableImage((int)i.getWidth(), (int)i.getHeight()); 
		PixelWriter bWriter = bImage.getPixelWriter();           
		for(int y = 0; y < i.getHeight(); y++) 
	    {	for(int x = 0; x < i.getWidth(); x++) 
	    	{	Color 	iColor = iReader.getColor(x, y),
	    				bColor = blend(iColor, c, 1-(1-opacity)*iColor.getOpacity());
	    		bWriter.setColor(x, y, bColor);  
	    	}
	    }
		return bImage;
	}
	/*
	(1-y/h)/(e-s)							
	
	__		(-1,1)		(0,1)		(0.5, 1)	(1, 1)		(1.5, 1)	(2, 1)		
	y=0		g = 0.5		g = 0.1		g = 1		g = 1		g = 0		g = 0
	
	y=90	g = 0.25	g = 0.5		g = 1		g = 1		g = 0		g = 0
	__		
	y=180	g = 0		g = 0		g = 0		g = 1		g = 0		g = 0
	
	
	(1-y/h)/(e-s)							
	
	__
	
	
	__		(-1, 0.5)	(0,0.5)		(0.5, 0.5)	(1, 0.5)	(1.5, 0.5)	(2, 0.5)			
	y=0		g = 0.333	g = 1		g = 0		g = 0		g = 0		g = 0		
	
	y=90	g = 0		g = 0		g = 0		g = 0		g = 0		g = 0		
	__		
	y=180	g = 0		g = 0		g = 0		g = 1		g = 0.5		g = 0.333		
	
		
	__
	*/
	private static Image blend(Image i, Color c, double opacity,  double start, double end)
	{	PixelReader iReader = i.getPixelReader(); 
		WritableImage bImage = new WritableImage((int)i.getWidth(), (int)i.getHeight()); 
		PixelWriter bWriter = bImage.getPixelWriter();           
		for(int y = 0; y < i.getHeight(); y++) 
	    {	double gOpacity = (end-y/i.getHeight())/(end-start);
	    	if(gOpacity < 0)
	    	{	gOpacity = 0;
	    	}
	    	if(gOpacity > 1)
	    	{	gOpacity = 1;
	    	}
if(y == 0 || y == 89 || y == 179)	    	
{System.out.println("Y:"+y+"	SE: ("+start+", "+end+")	G:"+gOpacity);	    	
}			
			for(int x = 0; x < i.getWidth(); x++) 
	    	{	Color 	iColor = iReader.getColor(x, y),
	    				bColor = blend(iColor, c, 1-(1-opacity)*iColor.getOpacity());
	    		bWriter.setColor(x, y, bColor);  
	    	}
	    }
		return bImage;
	}
	private static Color blend(Color a, Color b, double opacity)
	{	return Color.rgb
		(	(int)(255*(a.getRed()+(b.getRed()-a.getRed())*opacity)), 
			(int)(255*(a.getGreen()+(b.getGreen()-a.getGreen())*opacity)), 
			(int)(255*(a.getBlue()+(b.getBlue()-a.getBlue())*opacity))
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
		for(double i = 1, layers = nodes.size(); i < layers; i++)
		{	nodes.get((int)i).setTranslateX(cPosition[0]*Math.pow(i/layers, 3));
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
