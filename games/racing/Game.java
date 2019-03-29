package racing;
	
import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Game extends Application 
{	private static 
	final double[] Resolution = {1280, 720};
	
	long last;  
	int frames, 
		averageFrames = 0, 
		i = 0;

	// Game Start
	@Override public void start(Stage stage) 
	{	// Stage Set Up
		Pane root = new Pane();
		Scene scene = new Scene(root, Resolution[0]-10, Resolution[1]-10);
		stage.setScene(scene);
		stage.setResizable(false);;
		stage.show();
		
		// Track
		int	length = 100000;
		Track.newTrack(length);
		
		// Players
		ArrayList<Racer> player = new ArrayList<>();
		double[] position = {Resolution[0]/2, Resolution[1]/2};
		String	profile = "CPU";
		player.add(new Racer(position, profile));
		
		// Camera
		Physics	 subject = player.get(0);
		double[] sDomain = {Resolution[0]*0.2,Resolution[0]*0.5},
				 sRange = {Resolution[1]*0.4,Resolution[1]*0.8};
		Camera   camera = new Camera(subject, sDomain, sRange);
		
		// Game Pane 62 FPS
		Pane GamePane = new Pane();
		GamePane.getChildren().add(Track.getBackground());
		GamePane.getChildren().add(Track.getMidground());
		//GamePane.getChildren().add(Track.getForeground());
		//GamePane.getChildren().add(Track.getPlatforms());
		GamePane.getChildren().add(player.get(0).getRacer());
		root.getChildren().add(GamePane);
		
		// Key Inputs
		ArrayList<String> keyInputs = new ArrayList<>();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{	@Override public void handle(KeyEvent e) 
			{	if(!keyInputs.contains(e.getCode().toString()))
				{	keyInputs.add(e.getCode().toString());
				}
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>()
		{	@Override public void handle(KeyEvent e) 
			{	if(keyInputs.contains(e.getCode().toString()))
				{	keyInputs.remove(e.getCode().toString());
				}
			}
		});
		
		// Game Loop
		AnimationTimer gameLoop = new AnimationTimer()
		{	@Override public void handle(long now) 
			{	// Player Update
				for(int i = 0; i < player.size(); i++)
				{	player.get(i).update(keyInputs);
				}
				
				// Camera Update
				camera.update();
				Track.translate(camera.getPosition());
				for(int i = 0; i < player.size(); i++)
				{	player.get(i).translate(camera.getPosition());
				}
				
				// Frames/Second
				if(last == 0)
				{	last = now;
				}
				
				frames++;				
				if((now-last)/1000000000 >= 1)
				{	last = now;
					i++;
					averageFrames += frames;
//System.out.println(" Frame:"+frames+" Average:"+averageFrames/i);
					frames = 0;
				}
			}
		};
		gameLoop.start();
	}
	
	// Main
	public static void main(String[] args) 
	{	launch(args);
	}
	
	// Getters
	static double[] getResolution()
	{	return Resolution;
	}
}
