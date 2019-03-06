package racing;

public class Surface 
{	double[] 	position,			
				dimension,
				domain,
				range,
				nVector;
	double		cos0;

	public Surface(double[] position, double[] dimension) 
	{	this.position = position;
		this.dimension = dimension;

		// Domain
		domain = new double[]{position[0], position[0]+dimension[0]};
		if(dimension[0] < 0)
		{	domain = new double[]{position[0]+dimension[0], position[0]};
		}

		// Range
		range = new double[]{position[1], position[1]+dimension[1]};
		if(dimension[1] < 0)
		{	range = new double[]{position[1]+dimension[1], position[1]};
		}

		// Cos0
		double magnitude = Math.sqrt(Math.pow(dimension[0],2)+Math.pow(dimension[1],2));
		cos0 = dimension[0]/magnitude;

		// Normal Vector
		nVector = new double[]{dimension[1]/magnitude, -dimension[0]/magnitude};
	}
}
