package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import shape.Mesh;
import shape.MeshTriangle;
import shape.Triangle;
import math.Point;
import math.UV;
import math.Vector;

/** Assumes UTF-8 encoding. JDK 7+. */
public class FileReader {

	//TOGGLE LOGGING
	public static final boolean LOGGING = false;
	
	/**
   Constructor.
   @param aFileName full name of an existing, readable file.
	 */
	public FileReader(String aFileName){
		String path = new File(aFileName).getAbsolutePath();
		fFilePath = Paths.get(path);
	}


	/** Template method that calls {@link #processLine(String)}.  */
	public final Mesh generateMesh() {
		vertices = new ArrayList<Point>();
		uvcoords = new ArrayList<UV>();
		normals = new ArrayList<Vector>();
		mesh = new Mesh(vertices, uvcoords, normals);
		log("opening file " + fFilePath);
		try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
			while (scanner.hasNextLine()){
				processLine(scanner.nextLine());
			}      
		} catch(Exception e){
			log("Failed to open or process the file: " + e);
		}
		return mesh;
	}

	private ArrayList<Point> vertices; //vertices
	private ArrayList<UV> uvcoords; //texture coordinates
	private ArrayList<Vector> normals; //vertex normals
	private Mesh mesh;
	
	protected void processLineSlow(String aLine){
		//use a second Scanner to parse the content of each line 
		Scanner scanner = new Scanner(aLine);
		//scanner.useDelimiter("=");
		if (scanner.hasNext()){
			String com = scanner.next(); //het command
			if (com.equals("v")){
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double z = scanner.nextDouble();
				log("vertex: " + x + ", " + y + ", " + z);
				vertices.add(new Point(x,y,z));
			} else if(com.equals("vt")){
				double u = scanner.nextDouble();
				double v = scanner.nextDouble();
				log("uv: " + u + ", " + v);
				uvcoords.add(new UV(u,v));
			} else if(com.equals("vn")){
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double z = scanner.nextDouble();
				log("normal: " + x + ", " + y + ", " + z);
				normals.add(new Vector(x,y,z));
			} else if(com.equals("f")){
				//f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
				Scanner subScanner = new Scanner(scanner.next());
				subScanner.useDelimiter("/");
				int v1 = subScanner.nextInt();
				int vt1 = subScanner.nextInt();
				int vn1 = subScanner.nextInt();
				subScanner.close();
				subScanner = new Scanner(scanner.next());
				subScanner.useDelimiter("/");
				int v2 = subScanner.nextInt();
				int vt2 = subScanner.nextInt();
				int vn2 = subScanner.nextInt();
				subScanner.close();
				subScanner = new Scanner(scanner.next());
				subScanner.useDelimiter("/");
				int v3 = subScanner.nextInt();
				int vt3 = subScanner.nextInt();
				int vn3 = subScanner.nextInt();
				subScanner.close();
				mesh.add(new MeshTriangle(mesh, v1-1, v2-1, v3-1, vn1-1, vn2-1, vn3-1, vt1-1, vt2-1, vt3-1));
				log("triangle: " +v1+"/"+vt1+"/"+vn1+" "+v2+"/"+vt2+"/"+vn2+" "+v3+"/"+vt3+"/"+vn3);
			} else {
				log("invalid line: + aLine. Ignored");
			}
			//assumes the line has a certain structure
		}
		else {
			log("Empty or invalid line. Unable to process.");
		}
		scanner.close();
	}
	
	protected void processLine(String aLine){
		String[] split = aLine.split("\\s+|/");
		String com = null;
		if(split.length > 0){
			try{
				com = split[0];
				if (com.equals("v")){
					double x = Double.parseDouble(split[1]);
					double y = Double.parseDouble(split[2]);
					double z = Double.parseDouble(split[3]);
					log("vertex: " + x + ", " + y + ", " + z);
					vertices.add(new Point(x,y,z));
				} else if(com.equals("vt")){
					double u = Double.parseDouble(split[1]);
					double v = Double.parseDouble(split[2]);
					log("uv: " + u + ", " + v);
					uvcoords.add(new UV(u,v));
				} else if(com.equals("vn")){
					double x = Double.parseDouble(split[1]);
					double y = Double.parseDouble(split[2]);
					double z = Double.parseDouble(split[3]);
					log("normal: " + x + ", " + y + ", " + z);
					normals.add(new Vector(x,y,z));
				} else if(com.equals("f")){
					//f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3

					int v1 = Integer.parseInt(split[1]);
					int vt1 = Integer.parseInt(split[2]);
					int vn1 = Integer.parseInt(split[3]);

					int v2 = Integer.parseInt(split[4]);
					int vt2 = Integer.parseInt(split[5]);
					int vn2 = Integer.parseInt(split[6]);

					int v3 = Integer.parseInt(split[7]);
					int vt3 = Integer.parseInt(split[8]);
					int vn3 = Integer.parseInt(split[9]);

					mesh.add(new MeshTriangle(mesh, v1-1, v2-1, v3-1, vn1-1, vn2-1, vn3-1, vt1-1, vt2-1, vt3-1));
					log("triangle: " +v1+"/"+vt1+"/"+vn1+" "+v2+"/"+vt2+"/"+vn2+" "+v3+"/"+vt3+"/"+vn3);
				} else {
					log("invalid line: + aLine. Ignored");
				}
			} catch (Exception e){
				log("Exception while parsing: " + e);
			}

		} else {
			log("Empty or invalid line. Unable to process.");
		}
	}
	
	public BufferedImage getImage(){
			File file = fFilePath.toFile();
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				log("failed to read the file " + e);
			}
			return image;
	}

	// PRIVATE 
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;  

	private static void log(Object aObject){
		if(LOGGING)
			System.out.println(String.valueOf(aObject));
	}
} 