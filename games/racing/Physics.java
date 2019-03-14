package racing;

import java.util.ArrayList;

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
		ArrayList<Polygon> Platforms = Track.getPlatforms();
		for(int i = 0; i < Platforms.size(); i++)
		{	// Surface Scans
			double[] xPoints = Platforms.get(i).getXPoints(),
					 yPoints = Platforms.get(i).getYPoints();
			for(int j = 0; j < xPoints.length; j++)
			{	double startX = xPoints[j];
				double startY = yPoints[j];
				double endX = xPoints[(j+1)%xPoints.length];
				double endY = yPoints[(j+1)%yPoints.length];
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