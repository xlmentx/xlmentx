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
	
	private static Color 	lColor = Color.LIGHTGOLDENRODYELLOW,
							sColor = Color.DEEPSKYBLUE,
							fColor = Color.WHITE,
							mColor = Color.FORESTGREEN,
							gColor = Color.LIGHTGOLDENRODYELLOW;
	
	private static Image 	rocks = new Image("image/scenery/Rocks.png");
	
	
	// Private Constructor
	private Track(){}
	
	// Creates New Track
	static void newTrack(int tLength)
	{	// Background
		background = new Group();
		background.getChildren().add(new Rectangle(Resolution[0], Resolution[1], sColor));
		for(double i = 0, layers = 4, j = 0; i < layers; i++, j += i*Math.pow(-1, i))
		{	double[] dimension = {Resolution[0], Resolution[1]*0.3},
		
					 position = {dimension[0]*j/layers, Resolution[1]/2*(1.0/layers*i+1)};
			double 	peaks = tLength/dimension[0], 
					distance = 1-(i+1.0)/(layers+1);
			background.getChildren().add(newMountain(position, dimension, (int)peaks, distance));
System.out.println("i:"+i+" j/L:"+j/layers+" l:"+layers+" p:"+peaks+" d:"+distance);
		}	
		
		// Platforms
		platforms = new Group();
		double[] pPosition = {-Resolution[0]/2, Resolution[1]*0.8},
				 pDimension = {Resolution[0]*2, Resolution[1]};
		Polygon runWay = newPolygon(pPosition, pDimension, 0);
		platforms.getChildren().add(runWay);
		
		while(pPosition[0] < tLength)
		{	pPosition[0] += random(xRange, pGapRates, 0);
			pPosition[1] += random(yRange, pDropRates, -random(yRange, pWallRates, 0));
			pDimension = new double[] {random(xRange, pWidthRates, xRange[0]), Resolution[0]};
			double slope = random(sRange, pDeclineRates, -random(sRange, pInclineRates, 0));
			platforms.getChildren().add(newPolygon(pPosition, pDimension, slope));
		}
	}
	
	// Create Mountain
	private static Polygon newMountain(double[] position, double[] dimension, int peaks, double distance)
	{	double[] 	wRange = {0, dimension[0]*0.08},
				 	sRange = {0, dimension[1]/(dimension[0]*0.4)},
				 	sRates = {1, 0, 0, 0.8};

		Polygon mountain = new Polygon(position[0], Resolution[1], position[0], position[1]);	
		for(int i = 0; i < peaks; i++) 
    	{	double[] start = position.clone();		
			while(position[0]+wRange[1]*2 < start[0]+dimension[0] && position[1] <= start[1])
			{	double width = random(wRange),
			   	   	   slope = random(sRange, sRates)*Math.signum(position[0]-start[0]-dimension[0]/2);
				if(position[0] >= start[0]+dimension[0]*0.4 && slope < 0)
				{	slope /= 3;
				}
				mountain.getPoints().addAll(position[0] += width, position[1] += width*slope);
			}
			mountain.getPoints().addAll(position[0] = start[0]+dimension[0], position[1] = start[1]);
		}
		mountain.getPoints().addAll(position[0], Resolution[1]);
		
		Stop highlight = new Stop(0, blend(mColor.saturate(), sColor, distance)),
			 shadow = new Stop(1, blend(Color.BLACK, sColor, distance));
		mountain.setFill( new LinearGradient(0, 0, 0, 1, true, null, highlight, shadow));
		mountain.setEffect(new DropShadow(127, fColor));
		
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
		position[0] += dimension[0];
		position[1] += dimension[0]*slope;
		
		p.setFill(gColor);
		return p;
	}
	
	// Blend Colors
	private static Paint blend(Image i, Color c, double ratio)
	{	PixelReader iReader = i.getPixelReader(); 
		WritableImage bImage = new WritableImage((int)i.getWidth(), (int)i.getHeight()); 
		PixelWriter bWriter = bImage.getPixelWriter();           
		for(int x = 0; x < i.getWidth(); x++) 
    	{	for(int y = 0; y < i.getHeight(); y++) 
	    	{	bWriter.setColor(x, y, blend(iReader.getColor(x, y), c, ratio));              
    		}
    	}
		
		return new ImagePattern(bImage);
	}
	private static Color blend(Color a, Color b, double ratio)
	{	return Color.rgb
		(	(int)(255*(a.getRed()+(b.getRed()-a.getRed())*ratio)), 
			(int)(255*(a.getGreen()+(b.getGreen()-a.getGreen())*ratio)), 
			(int)(255*(a.getBlue()+(b.getBlue()-a.getBlue())*ratio))
		);
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
	{	// Background
		List<Node> nodes = background.getChildren();
		for(int i = 0, s = nodes.size(); i < s; i++)
		{	nodes.get(i).setTranslateX(cPosition[0]*(double)i/s);
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
