package racing;
import java.awt.Image;
import java.math.BigDecimal;
public class Player extends Character
{	// Class Objects and Variables
	private int			xKeyLast, yKeyHeld,		
						jumpCounter;
	private double[]	dimensionXY = new double[2];
	String				action = "falling";
	private Image		currentSprite;
	
	// Constructor
	public Player (int index, double[] position)
	{	this.xKeyLast = 1;
		this.position = position;
		dimensionXY = new double[]{100,100};
		updateStats(index, 0, 0);
	}

	// Player Update
	public void update(int xInput, int yInput) 
	{	////////////////////// X INPUT //////////////////////
//System.out.println();
		if(Math.abs(xInput) == 1)
		{	xKeyLast = xInput;
			////////////////////// GROUND ACCELERATION //////////////////////
			if(Math.abs(cos0) <= Math.toRadians(45))
			{	////////////////////// TOPSPEED LIMIT //////////////////////
				if(Math.abs(velocity[0]) <= topSpeed && xInput*velocity[0] >= 0)
				{	velocity[0] += groundAccel*xInput*Math.cos(cos0); 	 
					velocity[1] -= groundAccel*xInput*Math.sin(cos0); 	 
//System.out.println("x.1 vX="+velocityXY[0]+" aX="+groundAccel*xInput*Math.cos(thetaXY[1])+" tS="+topSpeed*Math.cos(thetaXY[1]));						
//System.out.println("y.1 vY="+velocityXY[1]+" aY="+(-groundAccel*xInput*Math.sin(contactThetaXY[1])));	
					action = "running";
				}
			}
			////////////////////// AERIAL ACCELERATION //////////////////////
			else
			{	////////////////////// TOPSPEED LIMIT //////////////////////
				if(Math.abs(velocity[0]) <= topSpeed)
				{	velocity[0] += airAccel*xInput; 	 
//System.out.println("x.2 xa="+airAccel*xInputPL[0]+" xv="+velocityXY[0]+" yv="+velocityXY[1]+" tS="+topSpeed);
				}
			}
		}
				
		////////////////////// Y INPUT //////////////////////
		if(yInput == 1) 
		{	if(yKeyHeld == 0) 
			{	////////////////////// GROUND JUMP//////////////////////
				if(cos0 <= Math.toRadians(45))
				{	velocity[0] -= jump*Math.sin(cos0/3);
		    		velocity[1] = jump*Math.cos(cos0/3);
		    		jumpCounter = 1;					
//System.out.println("x.4 vX="+velocityXY[0]+" aX="+jump*Math.sin(contactTheta/4));						
//System.out.println("y.3 vY="+velocityXY[1]+" aY="+jump*Math.cos(contactTheta/4));						
				}
				////////////////////// AERIAL JUMP //////////////////////
				else
				{	if(jumpCounter == 1)
					{	velocity[1] = doubleJump;
						jumpCounter = 0;					
//System.out.println("y.5  vY="+velocityXY[1]);							
					}
				}
				yKeyHeld = 1;
			}
		}
		////////////////////// Y RELEASE //////////////////////
		else
		{	yKeyHeld = 0;
		}
		
		// Update Physics
		physicsUpdate();		
		
		// Camera Update
		if(position[0]+velocity[0] < Game.cameraDomain[0] || position[0]+velocity[0] > Game.cameraDomain[1])
		{	Game.cameraPosition[0] = -velocity[0];
//System.out.println("x.6 cX="+centerXY[0]+" vX="+velocityXY[0]+" sS="+Platform.ScrollSpeedXY[0]);	
		}
		if(position[1]+velocity[1] < Game.cameraRange[0] || position[1]+velocity[1] > Game.cameraRange[1])
		{	Game.cameraPosition[1] = -velocity[1];
//System.out.println("x.6 cX="+centerXY[0]+" vX="+velocityXY[0]+" sS="+Platform.ScrollSpeedXY[0]);	
		}
		
		////////////////////// SPRITE UPDATE //////////////////////
		currentSprite = getSprite(xInput, xKeyLast, velocity, cos0);
	}
	
		////////////////////// GETTERS //////////////////////
	//////////////////////
	public double[] CenterXY() 
	{	return position;
	}
	public double[] DimensionXY() 
	{	return dimensionXY;
	}
	public Image CurrentSprite() 
	{	return currentSprite;
	}
	
}	