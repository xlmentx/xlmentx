package racing;

public class Physics 
{	// Class Objects and Variables
	protected double[] position = new double[2],
					   velocity = new double[2], 
					   cVector = new double[2];
	protected double	cos0 = 91;
	
	// Game Physics
	protected void physicsUpdate()
	{	// Gravitational Force
		velocity[1] += 0.5;

		// Platform Proximity Scans
		cos0 = 91;
		for(int i = 0; i < Game.Platform.length; i++)
		{	for(int j = 0; j < Game.Platform[i].surfaces.size(); j++)
			{	Surface s = Game.Platform[i].surfaces.get(j);

System.out.println("A");							
				// Collision Orientation
				double determinant = s.dimension[0]*velocity[1]-s.dimension[1]*velocity[0];
				if(determinant > 0)
				{	// Collision Point
					double t = (velocity[0]*(s.position[1]-position[1])-velocity[1]*(s.position[0]-position[0]))/determinant;
					double[] cPoint = {s.dimension[0]*t+s.position[0], s.dimension[1]*t+s.position[1]};
						
System.out.println("B");							
					// Collision Domain and Range
					if(cPoint[0] >= s.domain[0] && cPoint[0] <= s.domain[1] && Math.abs(velocity[0]) >= Math.abs(cPoint[0]-position[0]))
					{	
System.out.println("C");							
						if(cPoint[1] >= s.range[0] && cPoint[1] <= s.range[1] && Math.abs(velocity[1]) >= Math.abs(cPoint[1]-position[1]))
						{	// Snap to Contact
							cos0 = s.cos0;
							cVector = s.nVector;
							position = cPoint;
							
							// Normal Force
							double 	 nForce = Math.sqrt(Math.pow(velocity[0],2)+Math.pow(velocity[1],2))*s.cos0;
							velocity[0] += nForce*s.nVector[0]; 
							velocity[1] += nForce*s.nVector[1];
System.out.println("collision: v[0]"+velocity[0]+" v[1]"+velocity[1]);							
						}
					}
				}
			}
		}
		
		// Position Update
		position[0] += velocity[0];
		position[1]	+= velocity[1];
	}
}