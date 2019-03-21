package racing;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Racer extends Physics
{	// Racer Images
	private static 
	final Image[]		Stand = {new Image("image/character/stand.png")},	
						Run = 	{new Image("image/character/run.png")},		
						Slide = {new Image("image/character/slide.png")},   
						WSlide ={new Image("image/character/wallSlide.png")}, 		 
						Push = 	{new Image("image/character/push.png")},	
						Climb = {new Image("image/character/climb.png")},		
						Jump = 	{new Image("image/character/jump.png")}, 	
						WJump = {new Image("image/character/wallJump.png")},
						Fall = 	{new Image("image/character/fall.png")};
	private ImageView	rImage;
	
	// Racer Variables		
	private static			//	{gAcl,	aAcl,	tSpd,	gJmp,	aJmp}
	final double[] 		BaseStats =	{0.21,	0.2,	30,		-15,	-11	},
						WaterStats ={0,		0,		0,		0,		0	},
						EarthStats ={0,		0,		0,		0,		0	},
						FireStats =	{0,		0,		0,		0,		0	},
						AirStats =	{0,		0,		0,		0,		0	};
	private String		profile;
	private int			player, 	type;
	private double[]	baseXP,		classXP;
	private double		gAccel,		aAccel,		tSpeed,		
						gJump,		aJump;
				
	
	// Racer Constructor
	public Racer(double[] position, String profile)
	{	this.position = position;
		this.dimension = new double[]{Stand[0].getWidth(), Stand[0].getHeight()}; 
		this.velocity = new double[2];
		this.profile = profile;
	
		// Profile Class and XP
		player = 0;
		type = 0;
		baseXP = new double[BaseStats.length];
		classXP = new double[BaseStats.length];
		
		// Racer Class
		double[] cStats = new double[BaseStats.length];
		switch(type)		
		{	case 1: cStats = WaterStats;	break;
			case 2: cStats = EarthStats;	break;
			case 3: cStats = FireStats;		break;
			case 4:	cStats = AirStats;		break;
		}
		
		// Racer Stats
		for(int i = 0; i < BaseStats.length; i++)	
		{	switch(i)
			{	case 0:	gAccel = BaseStats[i]*(1+baseXP[i])+cStats[i]*classXP[i];	break;
				case 1:	aAccel = BaseStats[i]*(1+baseXP[i])+cStats[i]*classXP[i];	break;
				case 2:	tSpeed = BaseStats[i]*(1+baseXP[i])+cStats[i]*classXP[i];	break;
				case 3:	gJump =  BaseStats[i]*(1+baseXP[i])+cStats[i]*classXP[i];	break;
				case 4:	aJump =  BaseStats[i]*(1+baseXP[i])+cStats[i]*classXP[i]; 	break;
			}
		}
		
		// Character Image
		rImage = new ImageView(Fall[type]);
		rImage.setX(position[0]-dimension[0]/2);	
		rImage.setY(position[1]-dimension[1]/2);
		
	}	
	
	public void save()
	{	// Open data File
			// Look for Profile name
			//profileName = profileName;
				// Insert Base XP 
				//baseXP = baseXP;
				// Look for Current Class
				//currentClass = currentClass;
					// Insert Class XP 
					//baseXP = baseXP;
	}
	
	// Racer Update
	public void update(ArrayList<String> keyInputs)
	{	for(int i = 0; i < keyInputs.size(); i++)
		{	if(player == 0)
			{	// X Input
				if(keyInputs.get(i).contentEquals("RIGHT"))
				{	move(1);
				}
				else if(keyInputs.get(i).contentEquals("LEFT"))
				{	move(-1);
				}
				else
				{ 	stop();
				}	
			}
			//if(player == 1)
			//{}
		}
	
		physicsUpdate();
		rImage.setX(position[0]-dimension[0]/2);	
		rImage.setY(position[1]-dimension[1]/2);
	}
	
	// Move
	private void move(int input)
	{	// Ground
		if(Math.abs(cVector[0]) >= 0.707)
		{	// Run
			if(input*velocity[0] > 0)
			{	// Accelerate
				if(Math.abs(velocity[0]+gAccel*cVector[0]*input) <= tSpeed)
				{	velocity[0] += gAccel*input*cVector[0]; 	 
					velocity[1] += gAccel*input*cVector[1];
				}	
				rImage.setImage(Run[type]);	
			}
			// Slide
			else
			{	velocity[0] += gAccel*input*cVector[0]*2; 	 
				velocity[1] += gAccel*input*cVector[1]*2;
				rImage.setImage(Slide[type]);	
			}
		}
		// Air
		else 
		{	// Fly
			if(Math.abs(velocity[0]+aAccel*input) <= tSpeed || input*velocity[0] < 0)
			{	velocity[0] += aAccel*input;
			}
		}
	}
	
	// stop
	private void stop()
	{	
		
	}
	
	// Graphics Update
	public void translate(double[] cPosition)
	{	rImage.setTranslateX(cPosition[0]);	
		rImage.setTranslateY(cPosition[1]);
	}
	
	// Getters
	public ImageView getRacer()
	{	return rImage;
	}
	
}