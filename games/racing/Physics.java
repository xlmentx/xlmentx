package racing;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;

abstract class Physics 
{	// Physics Variables
	private static 
	final double		Gravity = 0.5;
	protected double[] 	position,	dimension,	velocity,
						cVector;
	
	// Game Physics
	protected void physicsUpdate()
	{	// Gravitational Force
		velocity[1] += Gravity;

		// Platform Scans
		cVector = new double[]{0, 0};
		Group Platforms = (Group)Track.getPlatforms();
		for(int i = 0; i < Platforms.getChildren().size(); i++)
		{	Polygon platform = (Polygon)Platforms.getChildren().get(i);
			
			// Surface Scans
			int pointSize = platform.getPoints().size();
			for(int j = 0; j < pointSize; j += 2)
			{	double startX = platform.getPoints().get(j);
				double startY = platform.getPoints().get(j+1);
				double endX = platform.getPoints().get((j+2)%pointSize);
				double endY = platform.getPoints().get((j+3)%pointSize);
				scanSurface(startX, startY, endX, endY);
			}
		}
		
		// Position Update
		position[0] += velocity[0];
		position[1]	+= velocity[1];
	}
	
	// Surface Scan
	private void scanSurface(double startX, double startY, double endX, double endY)
	{	double 	 sMagnitude = Math.sqrt(Math.pow(endX-startX,2)+Math.pow(endY-startY,2));
		double[] sVector = {(endX-startX)/sMagnitude, (endY-startY)/sMagnitude};
		
		// Collision Orientation
		double	 determinant = sVector[0]*velocity[1]-sVector[1]*velocity[0];
		if(determinant > 0) 
		{ 	// Collision Point
			double 	 t = (velocity[0]*(startY-position[1])-velocity[1]*(startX-position[0]))/determinant;
			double[] cPoint = {sVector[0]*t+startX, sVector[1]*t+startY};
			
			// Wall Range Decrease
			if(Math.abs(sVector[0]) <= 0.707)
			{	startY += Math.signum(sVector[1]);
				endY -= Math.signum(sVector[1]);
			}	
			// Platform Domain and Range
			if(cPoint[0] >= Math.min(startX, endX) && cPoint[0] <= Math.max(startX, endX)) 
			{	if(cPoint[1] >= Math.min(startY, endY) && cPoint[1] <= Math.max(startY, endY)) 
				{	//Velocity Domain and Range
					if(Math.abs(velocity[0]) >= Math.abs(cPoint[0]-position[0])) 
					{ 	if(Math.abs(velocity[1]) >= Math.abs(cPoint[1]-position[1])) 
						{	cVector = sVector;
							position = cPoint;

							// Normal Force
							double[] nVector ={sVector[1], -sVector[0]};
							double   nForce = Math.abs(velocity[0]*nVector[0]+velocity[1]*nVector[1]);
							velocity[0] += nForce*nVector[0];
							velocity[1] += nForce*nVector[1];
						}
					}
				}
			}
		}
	}
}