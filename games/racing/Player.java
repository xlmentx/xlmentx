package racing;

public class Player
{	// Instance Variables
	private int		yKeyHeld,		
					jumpCounter;
	
	// Player Constructor
	public Player()
	{}

	/*/ Player Update
	public void update(int xInput, int yInput) 
	{	// X Input
		if(Math.abs(xInput) == 1)
		{	// Acceleration
			if(Math.abs(velocity[0]) < tSpeed)
			{	// Ground
				if(Math.abs(contact.uVector[0]) >= 0.8)
				{	velocity[0] += gAccel*xInput*contact.uVector[0]; 	 
					velocity[1] += gAccel*xInput*contact.uVector[1];
					//cImage.setImage(Run[type]);
					
				}
				// Air
				else
				{	velocity[0] += aAccel*xInput;
					if(velocity[1]>0)
					{//	cImage.setImage(Fall[type]);
					}
				}
			}
		}
				
		// Y Input
		if(yInput == 1) 
		{	// Jump
			if(yKeyHeld == 0) 
			{	// Ground
				if(Math.abs(contact.uVector[0]) >= 0.8)
				{	velocity[0] += gJump*contact.nVector[0];
		    		velocity[1] = gJump;
		    		jumpCounter = 1;	
		    		//cImage.setImage(Jump[type]);
				}
				////////////////////// AERIAL JUMP //////////////////////
				else
				{	if(jumpCounter == 1)
					{	velocity[1] = aJump;
						jumpCounter = 0;					
					}
				}
				yKeyHeld = 1;
			}
		}
		////////////////////// Y RELEASE //////////////////////
		else
		{	yKeyHeld = 0;
		}
		
		// Player Update
		update();
		//cImage.setX(position[0]);	
		//cImage.setY(position[1]);
	
		
		// Camera Update
		if(position[0]+velocity[0] < Game.CameraD[0] || position[0]+velocity[0] > Game.CameraD[1])
		{	Game.Camera[0] = -velocity[0];
		}
		if(position[1]+velocity[1] < Game.CameraR[0] || position[1]+velocity[1] > Game.CameraR[1])
		{	Game.Camera[1] = -velocity[1];
		}
	}*/
}	