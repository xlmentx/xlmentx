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
	
	private static double	maxHeight = 0,
							minHeight = Resolution[1],
							heights = 0,
							runs = 0; 
						
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int tLength)
	{	// Background
		background = new Group();	
		double 	fog = 0;					
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
System.out.println("mStart:");
		// Mountain 
		for(double i = 0, layers = 70, j = 1; i < layers; i++, j*=-1)
		{	
			// Mountain Layer
			double[] mShift = {(int)(i+1)/2*j/layers-1, 1.75-Math.sqrt(1.56-Math.pow((i+1)/(layers+1), 2))},
					 mPosition = {Resolution[0]*mShift[0], Resolution[1]*mShift[1], 1-(i+1)/(layers+1)},
					 pDimension = {Resolution[0], Resolution[1]}; //Resolution[1]/2};
			Polygon  mLayer = newMountainLayer(mPosition, pDimension, tLength);	

			
			Image mImage = blend(new Image("image/scenery/gTest.png"), mColor, 0.9);
			mImage = blend(mImage, sColor, Math.pow(mPosition[2],2)); 
			Bounds 	mBounds = mLayer.getLayoutBounds();
			double 	fEnd = (fog*Resolution[1]*0.5-mBounds.getMinY())/mBounds.getHeight();
			mImage = blend(mImage, fColor, Math.pow(mPosition[2],2), 1, fEnd);
			mLayer.setFill(new ImagePattern(mImage, 0, 0, pDimension[0]/mBounds.getWidth(), 1, true));
			//background.getChildren().add(mLayer);
		}
		
		// Mountain Test
		double[]mPosition = {0, Resolution[1], 0},
				pDimension2 = {Resolution[0], Resolution[1]};
				Polygon  mLayer = newMountainLayer(mPosition, pDimension2, tLength);	
				Image mImage = blend(new Image("image/scenery/gTest.png"), mColor, 0.9);
				mImage = blend(mImage, sColor, Math.pow(mPosition[2],2)); 
				Bounds 	mBounds = mLayer.getLayoutBounds();
				double 	fEnd = (fog*Resolution[1]*0.5-mBounds.getMinY())/mBounds.getHeight();
				mImage = blend(mImage, fColor, Math.pow(mPosition[2],2), 1, fEnd);
				mLayer.setFill(new ImagePattern(mImage, 0, 0, pDimension2[0]/mBounds.getWidth(), 1, true));
				background.getChildren().add(mLayer);
System.out.println("Max:  "+(int)maxHeight+"	Min:  "+(int)minHeight+"	Avg:  "+(int)(heights/runs));	
System.out.println("goal: "+(int)(720+720/4)+"	goal: "+(int)(720-720/4)+"	goal: "+720);	
System.out.println("dMax: "+(int)(720+720/4-maxHeight)+"	dMin: "+(int)(720-720/4-minHeight)+"	dAvg: "+(int)(720-heights/runs));	
		
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
	private static Polygon newMountainLayer(double[] position, double[] pDimension, int length)
	{	Polygon	 mLayer = new Polygon();	
		while(position[0] < length*Math.pow(1-position[2], 3)+Resolution[0]) 
		{	double[] 	pStart = position.clone();
			double h = 0;
			while(position[0] <= pStart[0]+pDimension[0]*0.8 && position[1] <= pStart[1])
			{	mLayer.getPoints().addAll(position[0], position[1]);
				
if(pStart[1]-position[1] > h) 
{	h = pStart[1]-position[1];
}			

//Work on Cap first then uphills
				double[] sRange = {0, pDimension[1]/pDimension[0]/0.37},
						 sRates = {0, 1, 0, 0, 0.7};
				if(position[0] > pStart[0]+pDimension[0]*0.4 && position[0] < pStart[0]+pDimension[0]*0.5)
				{	sRates = new double[] {1, 0, 0};
				}
				double slope = random(sRange, sRates);
				
				
				double[] wRange = {pDimension[0]*0.01, pDimension[0]*0.1};
				if(position[0] < pStart[0]+pDimension[0]*0.4 || position[0] > pStart[0]+pDimension[0]*0.55)
				{	if(slope < sRange[1]/2)
					{	wRange = new double[]{pDimension[0]*0.01, pDimension[0]*0.05};
					}	
				}
				
				double width = random(wRange);

				
				
				double height = width*slope*Math.signum(position[0]+width/2-pStart[0]-pDimension[0]/2);
				
				position = new double[]{position[0]+width, position[1]+height, position[2]};
			}

if(h > maxHeight)
{	maxHeight = h;
}			
if(h < minHeight)
{	minHeight = h;
}			
heights += h;
runs++;
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
	{	return blend(i, c, opacity,  1, 1);
	}
	private static Image blend(Image i, Color c, double opacity,  double start, double end)
	{	PixelReader iReader = i.getPixelReader(); 
		WritableImage bImage = new WritableImage((int)i.getWidth(), (int)i.getHeight()); 
		PixelWriter bWriter = bImage.getPixelWriter();           
		for(int y = 0; y < i.getHeight(); y++) 
	    {	double gOpacity = (end-y/i.getHeight())/(end-start+0.001);
	    	if(gOpacity < 0 || gOpacity > 1)
	    	{	gOpacity = (Math.signum(gOpacity)+1)/2;
	    	}
	    	for(int x = 0; x < i.getWidth(); x++) 
	    	{	Color 	iColor = iReader.getColor(x, y);
	    		double  bOpacity = 1-(1-opacity*gOpacity)*iColor.getOpacity();
	    		Color	bColor = blend(iColor, c, bOpacity);
	    		bWriter.setColor(x, y, blend(iColor, c, bOpacity));  
	    	}
	    }
		return bImage;
	}
	private static Color blend(Color a, Color b, double opacity)
	{	Color c = Color.rgb
		(	(int)(255*(a.getRed()+(b.getRed()-a.getRed())*opacity)+0.5), 
			(int)(255*(a.getGreen()+(b.getGreen()-a.getGreen())*opacity)+0.5), 
			(int)(255*(a.getBlue()+(b.getBlue()-a.getBlue())*opacity)+0.5)
		);
		return c;
	}
	
	// Random Values
	private static double random(double[] range)
	{	return random(range, new double[]{1});
	}
	private static double random(double[] range, double[] rates)
	{	return random(range, rates, range[0]);
	}
	private static double random(double[] range, double[] rates, double dValue)
	{	double 	rValue = dValue,
				rSegment = (range[1]-range[0])/rates.length;
		Random rand = new Random();
		for(int i = 0; i < rates.length; i++)
		{	double chance = rand.nextDouble();
			if(chance <= rates[i])
			{	double randomSegment = rand.nextInt((int)(rSegment*100))/100.00; 
				rValue = range[0]+rSegment*i+randomSegment;
			}
		}
		return rValue;
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
