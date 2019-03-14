package racing;

public class Camera 
{	// Camera Variables
	private static double[] 	position,
								velocity;
	private static double		accel;
	private static Physics		subject;
	private static double[] 	sDomain,
			 					sRange;
	
	// Camera Constructor
	public Camera(Physics subject, double[] sDomain, double[] sRange) 
	{	this(new double[]{0, 0}, 0.6, subject, sDomain, sRange);
	}
	public Camera(double[] position, double accel, Physics subject, double[] sDomain, double[] sRange) 
	{	this.position = position;
			 velocity = new double[2];
		this.accel = accel;
		this.subject = subject;
		this.sDomain = sDomain;
		this.sRange =  sRange;
	}
	
	// Camera Update
	public void update() 
	{	// Scroll Left
		if(subject.position[0]+position[0] <= sDomain[0])
		{	velocity[0] += accel;	
//System.out.println("MovingL cV:"+velocity[0]+" sV:"+subject.velocity[0]);		
		}
		// Scroll Right
		else if(subject.position[0]+position[0] >= sDomain[1])
		{	velocity[0] -= accel;
//System.out.println("MovingR cV:"+velocity[0]+" sV:"+subject.velocity[0]);		
		}
		// Follow
		else if(velocity[0]*subject.velocity[0] < 0)
		{	velocity[0] = -subject.velocity[0];
//System.out.println("Following cV:"+velocity[0]+" sV:"+subject.velocity[0]);		
		}
		// Stop
		else
		{	velocity[0] = 0;
//System.out.println("HCentered cV:"+velocity[0]+" sV:"+subject.velocity[0]);		
		}
		position[0] += velocity[0]; 
		
		// Scroll Up
		if(subject.position[1]+position[1] <= sRange[0])
		{	velocity[1] += accel;
//System.out.println("MovingU cV:"+velocity[1]+" sV:"+subject.velocity[1]);		
		}
		// Scroll Down
		else if(subject.position[1]+position[1] >= sRange[1])
		{	velocity[1] -= accel; 
//System.out.println("MovingD cV:"+velocity[1]+" sV:"+subject.velocity[1]);		
		}
		// Follow
		else if(velocity[1]*subject.velocity[1] < 0)
		{	velocity[1] = -subject.velocity[1];
//System.out.println("Following cV:"+velocity[1]+" sV:"+subject.velocity[1]);		
		}
		// Stop
		else
		{	velocity[1] = 0;
//System.out.println("VCentered cV:"+velocity[1]+" sV:"+subject.velocity[1]);		
		}
		position[1] += velocity[1];
	}
	
	// Getter
	public double[] getPosition() 
	{	return position;
	}
}
