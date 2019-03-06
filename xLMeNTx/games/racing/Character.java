package racing;
import java.awt.Image;
public class Character extends Physics
{		////////////////////// ATRIBUTES //////////////////////
	//////////////////////
	public double	groundAccel,	airAccel,	topSpeed,	jump,	doubleJump;
		
		////////////////////// STAT UPDATES //////////////////////
	////////////////////// 		
	public void updateStats(int playerNum, int classType, int xpFile)
	{	////////////////////// DEFAULT STATS AND LIMITS //////////////////////
		double[]	////////////{groundAccel,	airAccel,	 	topSpeed,	 	jump,	  	 	doubleJump} 		
					BaseStats = {0.2,			0.2, 			30,		 		-15,		 	-11		  },
					StatLimits= {0.2,		  	0.2,		  	3,		  		-3,		  		-3		  },
					////////////////////// CLASS PERKS //////////////////////
					EarthClass= {0,	 	      	0,		  		0,		  		0,		  		0		  },
					FireClass = {0,			  	0,		  		0,		  		0,		  		0		  },
					WaterClass= {0,			  	0,		  		0,		  		0,		  		0		  },
					AirClass  = {0,			  	0,		  		0,		  		0,		  		0		  };
		
		////////////////////// CURRENT VALUES //////////////////////
		double[] currentXP = new double[BaseStats.length],
				 currentClass = new double[BaseStats.length];
				switch(classType)
				{	case 1:	currentClass = EarthClass;	break;
					case 2:	currentClass = FireClass;	break;
					case 3:	currentClass = WaterClass;	break;
					case 4:	currentClass = AirClass;  	break;
				}
	
		////////////////////// SUMATION AND STATS RETURN //////////////////////
		for(int i = 0; i < BaseStats.length; i++)	
		{	switch(i)
			{	case 0:	groundAccel = BaseStats[i]+(currentXP[i]/10)*StatLimits[i]+currentClass[i];	break;
				case 1:	airAccel = BaseStats[i]+(currentXP[i]/10)*StatLimits[i]+currentClass[i];	break;
				case 2:	topSpeed = BaseStats[i]+(currentXP[i]/10)*StatLimits[i]+currentClass[i];	break;
				case 3:	jump = BaseStats[i]+(currentXP[i]/10)*StatLimits[i]+currentClass[i];		break;
				case 4:	doubleJump = BaseStats[i]+(currentXP[i]/10)*StatLimits[i]+currentClass[i]; 	break;
			}
		}
	}
	
		////////////////////// SPRITE UPDATE //////////////////////
	////////////////////// 		
	public static Image getSprite(int xInput, int xKeyLast, double[] velocityXY, double contactTheta)
	{	int	spriteIndex = (xKeyLast+1)/2;
		Image	currentSprite = Game.Stand[spriteIndex];
				
		////////////////////// GROUND //////////////////////
		if(Math.abs(contactTheta) <= Math.toRadians(45))
		{	////////////////////// STAND //////////////////////
			if(velocityXY[0] == 0)
			{	currentSprite = Game.Stand[spriteIndex];
			}
			////////////////////// RUN //////////////////////
			else
			{	currentSprite = Game.Run[spriteIndex];
				////////////////////// SLIDE //////////////////////
				if(velocityXY[0]*xInput < 0)
				{	currentSprite = Game.Slide[spriteIndex];
				}
			}
		}
		
		////////////////////// AIR //////////////////////
		else
		{	////////////////////// JUMP //////////////////////
			if(velocityXY[1] < 0)
			{	currentSprite = Game.Jump[spriteIndex];
			}
			////////////////////// FALL //////////////////////
			else
			{	currentSprite = Game.Fall[spriteIndex];
			}
		}
		
	
		
		////////////////////// RETURN SPRITE //////////////////////
		return currentSprite;
	}	
}