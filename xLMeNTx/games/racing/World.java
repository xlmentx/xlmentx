package racing;

import java.awt.Image;

public class World 
{	
		////////////////////// ATRIBUTES //////////////////////
	//////////////////////
	private int				index;
	private double[]		sceneryXY = new double[2];
	
	////////////////////// CONSTRUCTOR //////////////////////
	public World (int index, double[] sceneryXY)
	{	this.index = index;
		this.sceneryXY = sceneryXY;
	}

	////////////////////// BACKGROUND UPDATE //////////////////////
	public void update() 
	{	if (sceneryXY[0]+Game.Background[0].getWidth() <= -Game.ResolutionXY[0])
		{	sceneryXY[0] += Game.Background[0].getWidth();
		}
	}
	
	//////////////////////GETTERS AND SETTERS//////////////////////
	public double[] getSceneryXY() 
	{	return sceneryXY;
	}
}