package main;

import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import math.Color;
import math.Point;
import math.Ray;
import math.Vector;
import sampling.Sample;
import scene.Scene;
import scene.SceneGenerator;
import camera.Camera;
import camera.CameraGenerator;
import camera.PerspectiveCamera;

/**
 * Entry point of your renderer.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Renderer {
	/**
	 * Entry point of your renderer.
	 * 
	 * @param arguments
	 *            command line arguments.
	 */
	public static void main(String[] arguments) {
		int width = 640;
		int height = 640;

		// parse the command line arguments
		for (int i = 0; i < arguments.length; ++i) {
			if (arguments[i].startsWith("-")) {
				try {
					if (arguments[i].equals("-width"))
						width = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-height"))
						height = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-help")) {
						System.out.println("usage: "
								+ "[-width  width of the image] "
								+ "[-height  height of the image]");
						return;
					} else {
						System.err.format("unknown flag \"%s\" encountered!\n",
								arguments[i]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.format("could not find a value for "
							+ "flag \"%s\"\n!", arguments[i]);
				}
			} else
				System.err.format("unknown value \"%s\" encountered! "
						+ "This will be skipped!\n", arguments[i]);
		}

		// validate the input
		if (width <= 0)
			throw new IllegalArgumentException("the given width cannot be "
					+ "smaller than or equal to zero!");
		if (height <= 0)
			throw new IllegalArgumentException("the given height cannot be "
					+ "smaller than or equal to zero!");

		// initialize the camera
		//PerspectiveCamera camera = new PerspectiveCamera(width, height,
				//new Point(0,0,0), new Vector(0, 0, 1), new Vector(0, 1, 0), 90);

		// initialize the graphical user interface
		ImagePanel panel = new ImagePanel(width, height);
		RenderFrame frame = new RenderFrame("Sphere", panel);
		
		// ===== CHANGE SCENE HERE =====
		// initialize the scene and the camera
		int scene_id = 22; 
		Scene scene = SceneGenerator.generateScene(scene_id);
		Camera camera = CameraGenerator.generateCamera(scene_id, width, height);
		
		// initialize the progress reporter
		ProgressReporter reporter = new ProgressReporter("Rendering", 40, width
				* height, false);
		reporter.addProgressListener(frame);

		// == render the scene ==
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {				
				Color pixelColor = new Color(0, 0, 0);

				for (int s = 0; s < AA; ++s) {
					Color tempColor;
					Sample sample;
					if(AA>1){sample = Sample.getRandomSample(x + 0.5, y + 0.5);} else {sample = new Sample(x + 0.5, y + 0.5);}
					Ray ray = camera.generateRay(sample);
					ShadeRec rec = scene.traceRay(ray);
					tempColor = rec.shade();
					//tempColor = rec.shadeNormal(rec);
					pixelColor = pixelColor.add(tempColor);
				}
				pixelColor = pixelColor.scale(1.0/AA);
				
				panel.set(x, y, 255, pixelColor.redToneMapped(), pixelColor.greenToneMapped(), pixelColor.blueToneMapped());
			}
			//reporter.update(height);
		}
		reporter.done();

		// save the output
		try {
			ImageIO.write(panel.getImage(), "png", new File("output.png"));
		} catch (IOException e) {
			System.err.println("The file could not be written.");
		}
	}
	
	public static final int AA = 10;
}
