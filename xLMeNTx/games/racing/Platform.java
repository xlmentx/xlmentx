package racing;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.IIOImage;

public class Platform
{	
			////////////////////// GAME PLAY //////////////////////
	//////////////////////
	static double[]	//////////// RANGES ///////////////
					xShiftRange ={Game.Stand[0].getWidth(), Game.ResolutionXY[0]*0.75},
					yShiftRange ={Game.Stand[0].getHeight()/2, Game.ResolutionXY[1]*0.75},
					widthRange = {Game.Stand[0].getWidth(), Game.ResolutionXY[0]*0.75},
					inclineRange = {0, 40},
					frictionRange = {0, 1},
													
					//////////// RATES ///////////////
					////////////{ SMALL,   MEDIUM,  LARGE}///////////////
					gapRate 	={  0.1,    0,    0},
					wallRate 	={  0.1,   	0,  	0},
					dropRate 	={  0.1,   	0.1,  0},
					widthRate 	={  0.1,   	0.3,   0.5},
					inclineRate ={  0.1,   	0.1,   0.1},
					declineRate ={  0.1,   	0.1,   0.1}, 
					frictionRate={  0,    0,   0}; 
				
	static Random 	Rand = new Random();

		////////////////////// STATIC DECLARATIONS //////////////////////
	//////////////////////
	static double[]	 ScrollSpeedXY = {0,0};
			////////////////////// ATRIBUTES //////////////////////
	//////////////////////
	private int		 index;
	private double[] position = {0,0},
					 dimension = {0,0};
	ArrayList<Surface> surfaces = new ArrayList();				
	private double 	 pTheta,
					 friction;
	private boolean  wall = false,
					 onScreen = false;
	
			////////////////////// CONSTRUCTORS //////////////////////
	//////////////////////
	public Platform (int index, double pTheta, double width)
	{	this.index = index;
		this.pTheta = pTheta;
		position = new double[]{(width-1)*index, Game.cameraRange[1]};
		dimension[0] = width;
	}
	public Platform (int pIndex)
	{	this.index = pIndex;
		position[0] = Game.Platform[index-1].position[0]+Game.Platform[index-1].dimension[0]-1;
		position[1] = Game.Platform[index-1].position[1]+Game.Platform[index-1].dimension[1];		
System.out.println(pIndex);
System.out.println(" pX="+position[0]);   		
System.out.println(" pY="+position[1]);   		
		randomize();
	}
	
			////////////////////// PLATFORM UPDATE //////////////////////
	//////////////////////
	public void update() 
	{	///////////////// LOCATION UPDATE /////////////////
		position = new double[]{position[0]+ScrollSpeedXY[0], position[1]+ScrollSpeedXY[1]};
		
		///////////////// ON SCREEN CHECK /////////////////
		onScreen = false;
		if(position[0]+dimension[0] >= 0 && position[0] <= Game.ResolutionXY[0])
		{	onScreen = true;
//System.out.println(" pX="+locationXY[0]+" sS="+ScrollSpeedXY[0]);   		
//System.out.println(" pY="+locationXY[1]+" sS="+ScrollSpeedXY[1]);   		
		}	
	}	
	
			///////////////////// PLATFORM RANDOMIZER //////////////////////
	//////////////////////
	public void randomize()
	{	//System.out.println("p"+pIndex+" previousXEnd="+previousXEnd+" previousYEnd="+previousYEnd);   		
		///////////////// GAPS /////////////////
		position[0] += offset(xShiftRange, gapRate, 0, "Gap");
		///////////////// WALLS /////////////////
		double yShift = -offset(yShiftRange, wallRate, 0, "Wall");
		///////////////// DROPS /////////////////
		position[1] += offset(yShiftRange, dropRate, yShift, "Drop");
//System.out.println(" yShift= "+yShift);
		///////////////// WIDTH /////////////////
		dimension[0] = offset(widthRange, widthRate, Game.Stand[0].getWidth(), "Width");
System.out.println(" width= "+dimension[0]);
		///////////////// INCLINE /////////////////
		pTheta = -offset(inclineRange, inclineRate, 0, "Incline");
		pTheta = Math.toRadians(offset(inclineRange, inclineRate, pTheta, "Decline"));
		dimension[1] = dimension[0]*Math.tan(pTheta);
	}
			///////////////////// OFFSET //////////////////////
	//////////////////////
	public double offset(double[] range, double[] rate, double offset, String type)
	{	double  segment = (range[1]-range[0])/rate.length;
		for(int i = 0; i < rate.length; i++)
		{	if(Rand.nextDouble() <= rate[i])
			{	offset = range[0]+segment*i+Rand.nextInt((int)segment);
			}
System.out.println(" "+type+" size"+i+" offset= "+offset);
		}
		return offset;
	}	
			////////////////////// GETTERS //////////////////////
	//////////////////////
	public double[] position() 
	{	return position;
	}
	public double[] dimension() 
	{	return dimension;
	}
	public double Theta() 
	{	return pTheta;
	}
	public double Friction() 
	{	return friction;
	}
	public boolean OnScreen() 
	{	return onScreen;
	}
}