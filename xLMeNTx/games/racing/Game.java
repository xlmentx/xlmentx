package racing;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Game 
extends Applet 
implements Runnable, KeyListener
{		////////////////////// STATIC DECLARATIONS //////////////////////
	//////////////////////
	static int 			Fps=60,					
						CurrentLevel= 0;
	static double[] 	cameraPosition = {0, 0},
						ResolutionXY = {1280, 720},
						cameraDomain = {(int)(ResolutionXY[0]*0.3), (int)(ResolutionXY[0]*0.4)},
						cameraRange = {(int)(ResolutionXY[1]*0.4), (int)(ResolutionXY[1]*0.7)},
						XInput = {0,0,0,0},
						YInput = {0,0,0,0};
	
		////////////////////// OBJECT DECLARATIONS //////////////////////
	//////////////////////
	static 	World[]	Scenery= new World[5];
	static 	World[]	BackGround= new World[1];
	static 	World[]	MidGround= new World[3];
	static 	World[]	ForeGround= new World[3];
	static 	Platform[]	Platform = new Platform[200];
	static 	Player[]	Player = new Player[1];
	
		////////////////////// SPRITE DECLARATIONS //////////////////////
	//////////////////////
	private Graphics second;
	static Image 		image,  				none;
	
	////////////////////// SCENERY SPRITES //////////////////////
	static BufferedImage[]	Background = new BufferedImage[1],	
							Midground = new BufferedImage[1],
							Foreground = new BufferedImage[1],
	
	////////////////////// PLATFORM SPRITES //////////////////////
							BaseSprite = new BufferedImage[1],
							SlopeSprite = new BufferedImage[1],					
	
	////////////////////// CHARACTER SPRITES //////////////////////
							Stand = new BufferedImage[2], 				
							Run = new BufferedImage[2], 	
							Slide = new BufferedImage[2], 
							Push = new BufferedImage[2],
							Jump = new BufferedImage[2], 
							Fall = new BufferedImage[2];
					
		////////////////////// INITIALIZATION //////////////////////
	//////////////////////
	@Override 
	public void init()
	{	////////////////////// ROOM PROPERTIES //////////////////////
		setSize((int)ResolutionXY[0],(int)ResolutionXY[1]);
		setBackground(Color.BLACK);
		setFocusable(true);
		Frame myframe = (Frame) this.getParent().getParent();
		myframe.setTitle("pulse");
		addKeyListener(this);
		try 
	   	{	////////////////////// BACKGROUND SPRITE DIRECTORY //////////////////////			
		   	Background[0]= ImageIO.read(new File("image/scenery/background.png"));
		   	Midground[0] = ImageIO.read(new File("image/scenery/midground.png"));
		   	Foreground[0]= ImageIO.read(new File("image/scenery/foreground.png"));
		   	
			//////////////////////PLATFORM SPRITE DIRECTIORY //////////////////////					
			BaseSprite[0] = ImageIO.read(new File("image/platform/base.png"));
			SlopeSprite[0]= ImageIO.read(new File("image/platform/slope.png")); 	
				
		   	//////////////////////CHARACTER SPRITE DIRECTORY //////////////////////			
			Stand[0]= ImageIO.read(new File("image/character/standleft.png"));
			Stand[1]= ImageIO.read(new File("image/character/standright.png"));
			Run[0]	= ImageIO.read(new File("image/character/runleft.png"));
			Run[1] 	= ImageIO.read(new File("image/character/runright.png"));
			Slide[0]= ImageIO.read(new File("image/character/slideleft.png")); 
			Slide[1]= ImageIO.read(new File("image/character/slideright.png"));
			Jump[0] = ImageIO.read(new File("image/character/jumpleft.png"));
			Jump[1] = ImageIO.read(new File("image/character/jumpright.png"));
			Fall[0] = ImageIO.read(new File("image/character/fallLeft.png"));
			Fall[1] = ImageIO.read(new File("image/character/fallRight.png"));
		} 
		catch (Exception e) 
		{
		}
	}
	
			////////////////////// OBJECT START //////////////////////
	//////////////////////
	@SuppressWarnings("static-access")
	@Override
   	public void start() 
 	{	////////////////////// BACKGROUND START //////////////////////
		for(int i=0; i < BackGround.length; i++)
   		{	double[] backgroundXY = {Background[0].getWidth()*i, 0};
   			BackGround[i] = new World(i, backgroundXY); 
   		}
		
		////////////////////// MIDGROUND START //////////////////////
		for(int i=0; i < MidGround.length; i++)
		{	double[] midgroundXY = {Midground[0].getWidth()*i, ResolutionXY[1]*0.2};
			MidGround[i] = new World(i, midgroundXY); 
		}
		
		////////////////////// FOREGROUND START //////////////////////
		for(int i=0; i < ForeGround.length; i++)
		{	double[] foregroundXY = {Foreground[0].getWidth()*i, ResolutionXY[1]*0.4};
			ForeGround[i] = new World(i, foregroundXY); 
		}

  		////////////////////// PLATFORM START //////////////////////
		for(int i=0; i < 4; i++)
   		{	Platform[i] = new Platform(i, Math.toRadians(0), ResolutionXY[0]/2); 
   		}
		for(int i=4; i < Platform.length; i++)
   		{	Platform[i] = new Platform(i); 
   		}

   		////////////////////// PLAYER START //////////////////////
   		for(int i=0; i < Player.length; i++)
   		{	double[] centerXY = {cameraDomain[0]+50*i, cameraRange[0]};
   			Player[i] = new Player(i, centerXY);	
   		}

   		////////////////////// GAMETHREAD START //////////////////////
   	   	Thread nonamethread = new Thread(this);	
   		nonamethread.start();
 	}   		

   		////////////////////// RUN LOOP //////////////////////
	//////////////////////
	@Override
   	public void run()
   	{	while (true) 
   		{	////////////////////// GAME LOOP SPEED //////////////////////
   			try 
   			{	Thread.sleep(1000/(long)Fps);
   			} 
   			catch (InterruptedException e) 
   			{	e.printStackTrace();
   			}
   			
   			//////////////////////BACKGROUND //////////////////////
   			for(int i=0; i < BackGround.length; i++)
   			{	BackGround[i].update();
   			}

   			////////////////////// MIDGROUND //////////////////////
   			for(int i=0; i < MidGround.length; i++)
   			{	MidGround[i].update();
   			}

   			////////////////////// FOREGROUND //////////////////////
   			for(int i=0; i < ForeGround.length; i++)
   			{	ForeGround[i].update();
   			}

   			////////////////////// PLATFORM UPDATE //////////////////////
   			for(int i=0; i < Platform.length; i++)
   			{	Platform[i].update();
   			}
   		
   			////////////////////// CHARACTER UPDATE //////////////////////
   			for(int i=0; i < Player.length; i++)
   			{	Player[i].update((int)XInput[i], (int)YInput[i]);
   			}
   				
   			////////////////////// GRAPHICS UPDATE //////////////////////
   			repaint();
   		}
   	}
				
   		////////////////////// PAINT //////////////////////
	//////////////////////
	@SuppressWarnings("static-access")
	@Override
   	public void paint(Graphics g) 
   	{	////////////////////// BACKGROUND //////////////////////
		for(int i=0; i < BackGround.length; i++)
   		{	g.drawImage(Background[CurrentLevel], (int)BackGround[i].getSceneryXY()[0], (int)BackGround[i].getSceneryXY()[1], Background[0].getWidth()*2, Background[0].getHeight()*2, this);
   		}
		
		////////////////////// MIDGROUND //////////////////////
		for(int i=0; i < MidGround.length; i++)
		{	g.drawImage(Midground[CurrentLevel], (int)MidGround[i].getSceneryXY()[0], (int)MidGround[i].getSceneryXY()[1], Midground[0].getWidth()*2, Midground[0].getHeight()*2, this);
   		}
		
		////////////////////// FOREGROUND //////////////////////
		for(int i=0; i < ForeGround.length; i++)
		{	g.drawImage(Foreground[CurrentLevel], (int)ForeGround[i].getSceneryXY()[0], (int)ForeGround[i].getSceneryXY()[1], Foreground[0].getWidth()*2, Foreground[0].getHeight()*2, this);
   		}

		////////////////////// PLATFORMS //////////////////////
   		for(int i=0; i < Platform.length; i++)
   		{	////////////////////// IF VISIBLE //////////////////////
   	   		if(Platform[i].OnScreen() == true)
   			{	////////////////////// TOP SLOPE //////////////////////
   	    		int offSet = (int)((Math.signum(Platform[i].Theta())-1)/2);
   	    		
   	   			double[] sDimensionXY = {Platform[i].dimension()[0]*Math.signum(Platform[i].Theta()), Math.abs(Platform[i].dimension()[1])},
   	    				 sLocationXY  = {Platform[i].position()[0]+sDimensionXY[0]*offSet, Platform[i].position()[1]+sDimensionXY[1]*offSet};
   	    		g.drawImage(SlopeSprite[CurrentLevel], (int)(sLocationXY[0]+cameraPosition[0]), (int)(sLocationXY[1]+cameraPosition[1]), (int)sDimensionXY[0], (int)sDimensionXY[1], this);
//System.out.println(i+"'s TopSlope is on screen x="+sLocationXY[0]+" y="+sLocationXY[1]+" w="+sDimensionXY[0]+" h="+sDimensionXY[1]);
//System.out.println("	yS="+Platform[i].pLocationXY()[1]+" yE="+(Platform[i].pLocationXY()[1]+Platform[i].pDimensionXY()[0]*Math.tan(Platform[i].pTheta())));
//System.out.println("	yMax="+Platform[i].pLocationXY()[1]+" yMin="+(Platform[i].pLocationXY()[1]+Platform[i].pDimensionXY()[0]*Math.tan(Platform[i].pTheta())));
   	   			
   	   			////////////////////// DRAW BASE //////////////////////
				double[] bDimensionXY = {Platform[i].dimension()[0], BaseSprite[0].getHeight()},
   	    				 bLocationXY  = {Platform[i].position()[0], Platform[i].position()[1]+sDimensionXY[1]*(Math.signum(Platform[i].Theta())+1)/2};
   	    		boolean drawBase = true;
				int J = 0;
	   			while(drawBase)
				{	drawBase = false;
					if(bLocationXY[1] <= ResolutionXY[1])
   					{	g.drawImage(BaseSprite[CurrentLevel], (int)(bLocationXY[0]+cameraPosition[0]), (int)(bLocationXY[1]+cameraPosition[1]), (int)bDimensionXY[0], (int)bDimensionXY[1], this);
   						J++;
   						drawBase = true;
//System.out.println(i+"'s Base is on screen x="+bLocationXY[0]+" y="+bLocationXY[1]+" w="+bDimensionXY[0]+" h="+bDimensionXY[1]);
   					}
					bLocationXY[1] += bDimensionXY[1];
	    		}
   			}
   		}
   		
   		////////////////////// PLAYERS //////////////////////   	   	
   		for(int i=0; i < Player.length; i++)
   		{	g.drawImage(Player[i].CurrentSprite(), (int)(Player[i].CenterXY()[0]-50+cameraPosition[0]), (int)(Player[i].CenterXY()[1]-50+cameraPosition[1]), (int)Player[i].DimensionXY()[0], (int)Player[i].DimensionXY()[1], this);
   		}
   	}
   	
   		////////////////////// GRAPHICS UPDATE //////////////////////
	//////////////////////
	@Override
   	public void update(Graphics g) 
   	{	if (image == null) 
		{	image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		g.drawImage(image, 0, 0, this);
   	}
   	
   		////////////////////// KEY PRESS //////////////////////
	//////////////////////
	@Override
   	public void keyPressed(KeyEvent user) 
   	{	switch (user.getKeyCode()) 
   		{	case KeyEvent.VK_LEFT:	XInput[0] = -1;		break;
	   		case KeyEvent.VK_RIGHT:	XInput[0] = 1;		break;
	   		case KeyEvent.VK_SPACE:	YInput[0] = 1;		break;
	   	}
   	}
	
		////////////////////// KEY RELEASE //////////////////////
	//////////////////////
	@Override
   	public void keyReleased(KeyEvent user) 
   	{	switch (user.getKeyCode()) 
   		{	case KeyEvent.VK_LEFT: 	if(XInput[0] == -1)
   									{	XInput[0] = 0;
   									} 					break;				
	   		case KeyEvent.VK_RIGHT:	if(XInput[0] == 1)
									{	XInput[0] = 0;
									}					break;
	   		case KeyEvent.VK_SPACE:	YInput[0] = 0;		break;
	   	}
   	}
	
////////////////////// NOT USED //////////////////////
   	@Override
   	public void stop(){}
   	@Override
   	public void destroy(){}
   	@Override
   	public void keyTyped(KeyEvent e){}
	
	
}