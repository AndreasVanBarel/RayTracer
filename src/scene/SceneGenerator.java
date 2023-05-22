package scene;

import light.AreaLight;
import light.DirectionalLight;
import light.PointLight;
import main.FileReader;
import material.Material;
import material.MaterialBlinnPhong;
import material.MaterialGlossy;
import material.MaterialPhong;
import material.MaterialReflective;
import material.MaterialRefractive;
import material.MaterialTextured;
import material.MaterialTranslucent;
import material.MaterialWard;
import material.MaterialWardDuer;
import math.Color;

import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Transformation;
import math.Vector;
import shape.AABB;
import shape.AABox;
import shape.Cylinder;
import shape.Mesh;
import shape.Plane;
import shape.Shape;
import shape.Sphere;
import shape.TransformedShape;
import shape.Triangle;
import texture.ConstantTexture;
import texture.ImageTexture;
import texture.Texture;

/**
 * This class contains methods to generate or edit scenes. Methods to add objects from file imput are also here.
 * @author Andreas
 *
 */
public class SceneGenerator {
	private SceneGenerator(){
	}
	
	public static Scene generateScene(int i){
		switch (i) {
		case 1:  return generateScene1();
		case 2:  return generateScene2();
		case 3:  return generateScene3();
		case 4:  return generateScene4();
		case 5:  return generateScene5();
		case 6:  return generateScene6();
		case 7:  return generateScene7();
		case 8:  return generateScene8();
		case 9:  return generateScene9();
		case 10:  return generateScene10();
		case 11:  return generateScene11();
		case 12:  return generateScene12();
		case 13:  return generateScene13();
		case 14:  return generateScene14();
		case 15:  return generateScene15();
		case 16:  return generateScene16();
		case 17:  return generateScene17();
		case 18:  return generateScene18();
		case 19:  return generateScene19();
		case 20:  return generateScene20();
		case 21: return generateScene21();
		case 22: return generateScene22();
		default: System.out.println("UPDATE generateScene method in SceneGenerator."); return null;
		}
	}
	
	//eerste scene die gegeven was
	public static Scene generateScene1(){
	
	Transformation t1 = Transformation.createTranslation(0, 0, 14);
	Transformation t2 = Transformation.createTranslation(4, -4, 12);
	Transformation t3 = Transformation.createTranslation(-4, -4, 12);
	Transformation t4 = Transformation.createTranslation(4, 4, 12);
	Transformation t5 = Transformation.createTranslation(-4, 4, 12);
//	Point a = new Point(1,0,5);
//	Point b = new Point(0,1,5);
//	Point c = new Point(0,0,5);
	Point o = new Point(0,-3,0);
	Vector n = new Vector(0,1,0);

	List<Item> items = new ArrayList<Item>();
	items.add(new Item(new Sphere(t1, 5), Color.RED));
	items.add(new Item(new Sphere(t2, 4), Color.GREEN));
	items.add(new Item(new Sphere(t3, 4), Color.BLUE));
	items.add(new Item(new Sphere(t4, 4), Color.GREEN));
	items.add(new Item(new Sphere(t5, 4), Color.BLUE));
	//items.add(new Item(new Triangle(a,b,c), Color.Yellow));
	items.add(new Item(new Plane(o,n), Color.GRAY));

	Scene scene = new Scene(items);
	
	scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.WHITE,1));
	scene.addLight(new DirectionalLight(new Vector(0, 1, -1),Color.CYAN,0.5));
	return scene;
	}
	
	//een bol
	public static Scene generateScene2(){
		Scene scene = new Scene();
		Transformation t1 = Transformation.createTranslation(0, 0, 10);
		scene.addItem(new Item(new Sphere(t1, 5), Color.RED));
		//scene.addLight(new DirectionalLight(new Vector(1, 1, -1)));
		scene.addLight(new PointLight(new Point(0,2,4.5), Color.WHITE, 1));
		return scene;
	}
	
	//meer bollen
	public static Scene generateScene3(){
		
		Transformation t1 = Transformation.createTranslation(4, 0, 5);
		Transformation t2 = Transformation.createTranslation(4, -4, 10);
		Transformation t3 = Transformation.createTranslation(-4, -4, 9);
		Transformation t4 = Transformation.createTranslation(4, 4, 12);
		Transformation t5 = Transformation.createTranslation(-4, 4, 14);
		Point a = new Point(1,0,5);
		Point b = new Point(0,1,5);
		Point c = new Point(0,0,5);
		Point o = new Point(0,-3,0);
		Vector n = new Vector(0,1,0);

		List<Item> items = new ArrayList<Item>();
		TransformedShape tfs = new TransformedShape(Transformation.createScale(1, 2, 1), new Sphere(t1, 1));
		Item redSphere = new Item(tfs, Color.RED);
		items.add(redSphere);
		items.add(new Item(new Sphere(t2, 3), Color.WHITE));
		items.add(new Item(new Sphere(t3, 4), Color.BLUE));
		items.add(new Item(new Sphere(t4, 3), Color.GREEN));
		items.add(new Item(new Sphere(t5, 4), Color.YELLOW));
		items.add(new Item(new Triangle(a,b,c), Color.WHITE));
		items.add(new Item(new Plane(o,n), Color.GRAY));

		Scene scene = new Scene(items);
		
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.CYAN,0.5));
		scene.addLight(new DirectionalLight(new Vector(0, 1, -1),Color.MAGENTA,0.5));
		scene.addLight(new PointLight(new Point(0,20,20), Color.WHITE, 300));
		return scene;
	}

	// torus
	public static Scene generateScene4(){
		Scene scene = new Scene();
		Point o = new Point(0,-3,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));

		FileReader parser = new FileReader("src/resources/torus.obj");
		Mesh mesh = parser.generateMesh();
		Transformation t1 = Transformation.createTranslation(0, -2, 5);
		scene.addItem(new Item(new TransformedShape(t1, mesh), Color.RED));
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1)));
		scene.addLight(new PointLight(new Point(0,1.05185614,3.16516515), Color.WHITE, 5));
		return scene;
	}

	// box & cilinder
	public static Scene generateScene5(){
		Scene scene = new Scene();
		Point p1 = new Point(-1,-1,5);
		Point p2 = new Point(-3,-2,7);
		Point o = new Point(0,-2,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		scene.addItem(new Item(new AABox(p1, p2), Color.RED));
		Transformation tcyl = Transformation.createTranslation(3, -2, 8);
		scene.addItem(new Item(new TransformedShape(tcyl, new Cylinder(3,1)),Color.YELLOW));
		//scene.addLight(new DirectionalLight(new Vector(1, 1, -1)));
		scene.addLight(new PointLight(new Point(3,2.1,8), Color.WHITE, 10));
		return scene;
	}
	
	// nog een torus
	public static Scene generateScene6(){
		Scene scene = new Scene();
		FileReader parser = new FileReader("src/resources/torus.obj");
		Mesh mesh = parser.generateMesh();
		Transformation t1 = Transformation.createTranslation(0, -1.5, 5);
		scene.addItem(new Item(new TransformedShape(t1, mesh), Color.RED));
		scene.addLight(new PointLight(new Point(1,1,0), Color.WHITE, 20));
		return scene;
	}

	// scene met vanalles in
	public static Scene generateScene7(){
		
		Transformation t1 = Transformation.createTranslation(4, 0, 5);
		Transformation t2 = Transformation.createTranslation(4, -4, 10);
		Transformation t3 = Transformation.createTranslation(-4, -4, 9);
		Transformation t4 = Transformation.createTranslation(4, 4, 12);
		Transformation t5 = Transformation.createTranslation(-4, 4, 14);
		Point a = new Point(1,0,5);
		Point b = new Point(0,1,5);
		Point c = new Point(0,0,5);
		Point o = new Point(0,-3,0);
		Vector n = new Vector(0,1,0);
	
		List<Item> items = new ArrayList<Item>();
		TransformedShape tfs = new TransformedShape(Transformation.createScale(1, 2, 1), new Sphere(t1, 1));
		Item redSphere = new Item(tfs, Color.RED);
		items.add(redSphere);
		items.add(new Item(new Sphere(t2, 3), Color.WHITE));
		items.add(new Item(new TransformedShape(t3, new Cylinder(1,3)), Color.BLUE));
		items.add(new Item(new Sphere(t4, 3), Color.GREEN));
		items.add(new Item(new Sphere(t5, 4), Color.YELLOW));
		//items.add(new Item(new Triangle(a,b,c), Color.WHITE));
		items.add(new Item(new Plane(o,n), Color.GRAY));
		
		Scene scene = new Scene(items);
	
		FileReader parser = new FileReader("src/resources/monkey.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation tmon = Transformation.createTranslation(0, -1.0, 4);
		scene.addItem(new Item(new TransformedShape(tmon, aabb), Color.WHITE));
	
		Point p1 = new Point(-1,-2,5);
		Point p2 = new Point(1,-3,7);
		scene.addItem(new Item(new AABox(p1, p2), Color.RED));
		
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.CYAN,0.5));
		scene.addLight(new DirectionalLight(new Vector(0, 1, -1),Color.MAGENTA,0.5));
		scene.addLight(new PointLight(new Point(0,20,20), Color.WHITE, 300));
		scene.addLight(new PointLight(new Point(-5,1,5), Color.WHITE, 25));
		return scene;
	}
	
	//house
	public static Scene generateScene8(){
		Scene scene = new Scene();
		Point o = new Point(0,-0.2,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		FileReader parser = new FileReader("src/resources/house.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(25,0);
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 0.5); //voor dragon
		Transformation t1 = Transformation.createTranslation(0, -0.2, 1.2); //voor dragon
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 10);
		Transformation t2 = Transformation.createRotationY(-60);
		FileReader imageReader = new FileReader("src/resources/house_texture.jpg");
		Texture texture = new ImageTexture(imageReader.getImage());
		scene.addItem(new Item(new TransformedShape(t1.append(t2), aabb), texture));
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.WHITE,0.5));
		scene.addLight(new DirectionalLight(new Vector(-1, 1, -1),Color.WHITE,0.5));
		return scene;
	}
	
	/*Apple without normal map*/
	public static Scene generateScene9(){
		Scene scene = new Scene();
		Point o = new Point(0,-0.5,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		FileReader parser = new FileReader("src/resources/apple.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation t1 = Transformation.createTranslation(0, -0.5, 1);
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 10);
		Transformation t2 = Transformation.createRotationY(-75);
		Shape finalShape = new TransformedShape(t1.append(t2), aabb);
		FileReader imageReader = new FileReader("src/resources/apple_texture.jpg");
		Texture texture = new ImageTexture(imageReader.getImage());
		//texture = new ConstantTexture(Color.RED);
		Item apple = new Item(finalShape, texture);
		scene.addItem(apple);
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.WHITE,0.5));
		//scene.addLight(new DirectionalLight(new Vector(-1, 1, -1),Color.WHITE,0.5));
		return scene;
	}
	
	/*Bunny*/
	public static Scene generateScene10(){
		Scene scene = new Scene();
		Point o = new Point(0,-1.5,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		FileReader parser = new FileReader("src/resources/bunny.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation t1 = Transformation.createTranslation(0, -1.6, 6);
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 10);
		Transformation t2 = Transformation.createRotationY(-175);
		Shape finalShape = new TransformedShape(t1.append(t2), aabb);
		FileReader imageReader = new FileReader("src/resources/apple_texture.jpg");
		Texture texture = new ImageTexture(imageReader.getImage());
		//texture = new ConstantTexture(Color.RED);
		Item apple = new Item(finalShape, texture);
		scene.addItem(apple);
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.WHITE,0.5));
		//scene.addLight(new DirectionalLight(new Vector(-1, 1, -1),Color.WHITE,0.5));
		return scene;
	}
	
	/*White dragon with colored light sources*/
	public static Scene generateScene11(){
		Scene scene = new Scene();
		Point o = new Point(0,-1.5,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		FileReader parser = new FileReader("src/resources/dragon.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation t1 = Transformation.createTranslation(0, -0.2, 1);
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 10);
		Transformation t2 = Transformation.createRotationY(-75);
		Shape finalShape = new TransformedShape(t1.append(t2), aabb);
		Texture texture = new ConstantTexture(Color.WHITE);
		Material reflective = new MaterialReflective(Color.WHITE, Color.WHITE, 0.5);
		Item dragon = new Item(finalShape, reflective);
		scene.addItem(dragon);
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.RED,0.5));
		scene.addLight(new DirectionalLight(new Vector(-1, 1, -1),Color.GREEN,0.5));
		return scene;
	}
	
	/*Bunny with area light*/
	public static Scene generateScene12(){
		Scene scene = new Scene();
		Point o = new Point(0,-1.5,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		FileReader parser = new FileReader("src/resources/bunny.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation t1 = Transformation.createTranslation(-0.8, -1.6, 6);
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 10);
		Transformation t2 = Transformation.createRotationY(-175);
		Shape finalShape = new TransformedShape(t1.append(t2), aabb);
		Material material = new MaterialRefractive(new Color(1,0.9,0.9), 1.5); //new Color(1,0.5,0.5)
		//Item bunny = new Item(finalShape, Color.RED);
		Item bunny = new Item(finalShape, material);
		scene.addItem(bunny);
		//scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.WHITE,0.5));
		//scene.addLight(new DirectionalLight(new Vector(-1, 1, -1),Color.WHITE,0.5));
		//scene.addLight(new PointLight(new Point(0,30,50), Color.WHITE, 1000));
		//AreaLight areaLight = new AreaLight(new Point(10.5,14,23.5), new Point(13.5,14,26.5), Color.WHITE, 1000);
		AreaLight areaLight = new AreaLight(new Point(-2,12,23.5), new Point(5.5,12,26.5), Color.WHITE, 1000);
		scene.addLight(areaLight);
		scene.addItem(areaLight.getAssociatedLightItem());
		return scene;
	}
	
	/*Apple with normal map*/
	public static Scene generateScene13(){
		Scene scene = new Scene();
		Point o = new Point(0,-0.45,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		
		//get the apple's shape (with bump map)
		FileReader parser = new FileReader("src/resources/apple.obj");
		Mesh mesh = parser.generateMesh();
		FileReader normalMapReader = new FileReader("src/resources/apple_objectSpaceNormalMap.png");
		Texture normalMap = new ImageTexture(normalMapReader.getImage());
		//mesh.setNormalMap(normalMap);
		
		//set up acceleration structure
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		
		//place apple in scene and texture it
		Transformation t1 = Transformation.createTranslation(0.05, -0.45, 1.25);
		//Transformation t1 = Transformation.createTranslation(0, -0.2, 10);
		Transformation t2 = Transformation.createRotationY(-75);
		Transformation t3 = Transformation.createRotationZ(90);
		Shape finalShape = new TransformedShape(t1.append(t3).append(t2), aabb);
		FileReader textureReader = new FileReader("src/resources/apple_texture.jpg");
		Texture texture = new ImageTexture(textureReader.getImage());
		//texture = new ConstantTexture(Color.RED);
		Material material = new MaterialTextured(texture);
		Item apple = new Item(finalShape, material);
		scene.addItem(apple);
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.WHITE,0.5));
		//scene.addLight(new DirectionalLight(new Vector(-1, 1, -1),Color.WHITE,0.5));
		return scene;
	}
	
	// scene met spiegelende bollen
	public static Scene generateScene14(){
		
		Transformation t1 = Transformation.createTranslation(4, 0, 5);
		Transformation t2 = Transformation.createTranslation(4, -4, 10);
		Transformation t3 = Transformation.createTranslation(-4, -4, 9);
		Transformation t4 = Transformation.createTranslation(4, 4, 12);
		Transformation t5 = Transformation.createTranslation(-4, 4, 14);
		Point a = new Point(1,0,5);
		Point b = new Point(0,1,5);
		Point c = new Point(0,0,5);
		Point o = new Point(0,-3,0);
		Vector n = new Vector(0,1,0);

		Material reflective = new MaterialReflective(Color.GREEN, Color.WHITE, 0.5); //probeer zeker 0.01 0.1 0.5 en 0.9
		Material reflective2 = new MaterialReflective(Color.YELLOW, Color.WHITE, 0.5); //probeer zeker 0.01 0.1 0.5 en 0.9


		List<Item> items = new ArrayList<Item>();
		TransformedShape tfs = new TransformedShape(Transformation.createScale(1, 2, 1), new Sphere(t1, 1));
		Item redSphere = new Item(tfs, Color.RED);
		items.add(redSphere);
		items.add(new Item(new Sphere(t2, 3), Color.WHITE));
		items.add(new Item(new TransformedShape(t3, new Cylinder(1,3)), Color.BLUE));
		items.add(new Item(new Sphere(t4, 3), reflective));
		items.add(new Item(new Sphere(t5, 4), reflective2));
		//items.add(new Item(new Triangle(a,b,c), Color.WHITE));
		items.add(new Item(new Plane(o,n), Color.GRAY));

		
		Scene scene = new Scene(items);
	
		FileReader parser = new FileReader("src/resources/monkey.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation tmon = Transformation.createTranslation(0, -1.0, 4);
		scene.addItem(new Item(new TransformedShape(tmon, aabb), Color.WHITE));
	
		Point p1 = new Point(-1,-2,5);
		Point p2 = new Point(1,-3,7);
		scene.addItem(new Item(new AABox(p1, p2), Color.RED));
		
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.CYAN,0.5));
		scene.addLight(new DirectionalLight(new Vector(0, 1, -1),Color.MAGENTA,0.5));
		scene.addLight(new PointLight(new Point(0,20,20), Color.WHITE, 300));
		scene.addLight(new PointLight(new Point(-5,1,5), Color.WHITE, 25));
		return scene;
	}
	
	//Refractive spheres
	public static Scene generateScene15(){
		Transformation t1 = Transformation.createTranslation(4, 0, 5);
		Transformation t2 = Transformation.createTranslation(4, -4, 10);
		Transformation t3 = Transformation.createTranslation(-4, -4, 9);
		Transformation t4 = Transformation.createTranslation(4, 4, 12);
		Transformation t5 = Transformation.createTranslation(-4, 4, 14);
		Point o = new Point(0,-3,0);
		Vector n = new Vector(0,1,0);

		Material reflective = new MaterialReflective(Color.GREEN, Color.WHITE, 0.5); //probeer zeker 0.01 0.1 0.5 en 0.9
		Material reflective2 = new MaterialReflective(Color.YELLOW, Color.WHITE, 0.5); //probeer zeker 0.01 0.1 0.5 en 0.9
		Material refractive = new MaterialRefractive(Color.WHITE, 1.05);

		List<Item> items = new ArrayList<Item>();
		TransformedShape tfs = new TransformedShape(Transformation.createScale(1, 2, 1), new Sphere(t1, 1));
		Item redSphere = new Item(tfs, Color.RED); //Ellipsoid
		items.add(redSphere);
		items.add(new Item(new Sphere(t2, 3), Color.WHITE));
		items.add(new Item(new TransformedShape(t3, new Cylinder(1,3)), Color.BLUE));
		items.add(new Item(new Sphere(t4, 3), reflective));
		items.add(new Item(new Sphere(t5, 4), reflective2));
		items.add(new Item(new Plane(o,n), Color.GRAY));

		//Refractive thin lens
		Transformation t6 = Transformation.createTranslation(-1, 0.2, 2.5);
		Shape refrShape = new Sphere(t6.append(Transformation.createScale(1, 1, 0.02)) ,1);
		Item refrSphere = new Item(refrShape, refractive); //Ellipsoid
		//items.add(refrSphere);
		
		//Refractive ellipsoid (choose either lens or ellipsoid)
		refrShape = new TransformedShape(Transformation.createScale(1, 2, 1), new Sphere(t6, 1));
		refrSphere = new Item(refrShape, refractive); //Ellipsoid
		items.add(refrSphere);

		//Refractive sphere
		Transformation t7 = Transformation.createTranslation(1, -0.2, 3);
		
		items.add(new Item(new Sphere(t7, 1), refractive)); //Ellipsoid
		
		Scene scene = new Scene(items);
	
		FileReader parser = new FileReader("src/resources/monkey.obj");
		Mesh mesh = parser.generateMesh();
		AABB aabb = new AABB(mesh);
		aabb.split(5,0);
		Transformation tmon = Transformation.createTranslation(0, -1.0, 4);
		scene.addItem(new Item(new TransformedShape(tmon, aabb), Color.WHITE));
	
		Point p1 = new Point(-1,-2,5);
		Point p2 = new Point(1,-3,7);
		scene.addItem(new Item(new AABox(p1, p2), Color.RED));
		
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1),Color.CYAN,0.5));
		scene.addLight(new DirectionalLight(new Vector(0, 1, -1),Color.MAGENTA,0.5));
		scene.addLight(new PointLight(new Point(0,20,20), Color.WHITE, 300));
		scene.addLight(new PointLight(new Point(-5,1,5), Color.WHITE, 25));
		return scene;
	}
	
	//een bol van binnenin
	public static Scene generateScene16(){
		Scene scene = new Scene();
		Transformation t1 = Transformation.createTranslation(0, 0, 4.5);
		scene.addItem(new Item(new Sphere(t1, 5), Color.RED));
		//scene.addLight(new DirectionalLight(new Vector(1, 1, -1)));
		scene.addLight(new PointLight(new Point(0,1,3), Color.WHITE, 10));
		return scene;
	}
	
	//environment map op spiegelende/refracterende bol
	public static Scene generateScene17(){
		Scene scene = new Scene();
		Transformation t1 = Transformation.createTranslation(0, 0, 10);
		MaterialReflective material = new MaterialReflective(new Color(1.0,0.9,0.6), Color.WHITE, 0.5);
		//MaterialRefractive material = new MaterialRefractive(new Color(0.98,0.99,1), 1.45); //probeer 1.01, 1.1, 2.3, 5, 0.8
		//MaterialRefractive material = new MaterialRefractive(new Color(1,1,1), 1.5); //probeer 1.01, 1.1, 2.3, 5, 0.8
		FileReader envMapReader = new FileReader("src/resources/envMap4.jpg");
		Texture envMap = new ImageTexture(envMapReader.getImage());
		//material.setEnvMap(envMap);
		scene.addItem(new Item(new Sphere(t1, 5), material));
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1)));
		scene.setEnvMap(envMap);
		return scene;
	}

	//materials on sphere with environment map
	public static Scene generateScene18(){
		Scene scene = new Scene();
		Transformation t1 = Transformation.createTranslation(0, 0, 10);
		Material material = MaterialWardDuer.DELRIN;
		scene.addItem(new Item(new Sphere(t1, 5), material));
		scene.addLight(new DirectionalLight(new Vector(1, 1, -1), Color.WHITE, Math.PI));
		//scene.addLight(new PointLight(new Point(0,2,4.5), Color.WHITE, 1));
		FileReader envMapReader = new FileReader("src/resources/envMap4.jpg");
		Texture envMap = new ImageTexture(envMapReader.getImage());
		scene.setEnvMap(envMap);
		return scene;
	}
	
	//sierpinski by reflective spheres
	public static Scene generateScene19(){
		Scene scene = new Scene();
		//double distance = 5;
		double distance = 1.5;
		Transformation t = Transformation.createTranslation(0, 0, distance).append(Transformation.createRotationX(37.5)).append(Transformation.createRotationY(45));
		Transformation t1 = t.append(Transformation.createTranslation(-1, -1, -1));
		Transformation t2 = t.append(Transformation.createTranslation(-1, 1, 1));
		Transformation t3 = t.append(Transformation.createTranslation(1, -1, 1));
		Transformation t4 = t.append(Transformation.createTranslation(1, 1, -1));
		
		//good values: 0.1,0.1,1 or 0.15,0,1 or 0.15,0,1.1 or 0.015, 0, 1.3
		double vibrancy = 0.15;
		double white = 0.0;
		double reflection = 1.05;
		
		MaterialReflective m1 = new MaterialReflective(Color.RED.scale(vibrancy), Color.WHITE, reflection);
		MaterialReflective m2 = new MaterialReflective(Color.WHITE.scale(white), Color.WHITE, reflection);
		MaterialReflective m3 = new MaterialReflective(Color.BLUE.scale(vibrancy), Color.WHITE, reflection);
		MaterialReflective m4 = new MaterialReflective(Color.GREEN.scale(vibrancy), Color.WHITE, reflection);
		
		m1.ka_after = 1;
		m2.ka_after = 1;	
		m3.ka_after = 1;
		m4.ka_after = 1;
		
		m1.maxRecursionDepth = 50;
		m2.maxRecursionDepth = 50;
		m3.maxRecursionDepth = 50;
		m4.maxRecursionDepth = 50;

		scene.addItem(new Item(new Sphere(t1, Math.sqrt(2)), m1 ));
		scene.addItem(new Item(new Sphere(t2, Math.sqrt(2)), m2 ));
		scene.addItem(new Item(new Sphere(t3, Math.sqrt(2)), m3 ));
		scene.addItem(new Item(new Sphere(t4, Math.sqrt(2)), m4 ));

		//scene.addLight(new DirectionalLight(new Vector(1, 1, -10), Color.WHITE, Math.PI));
		//scene.addLight(new PointLight(new Point(0,0,4), Color.RED, 0.1));
		FileReader envMapReader = new FileReader("src/resources/envMap2.jpg");
		Texture envMap = new ImageTexture(envMapReader.getImage());
		//scene.setEnvMap(envMap); //comment this
		return scene;
	}
	
	//DOF with spheres
	public static Scene generateScene20(){
		Scene scene = new Scene();
		Point o = new Point(0,-5,0);
		Vector n = new Vector(0,1,0);
		Material floor = new MaterialReflective(new Color(0.5,0.6,0.7), Color.WHITE, 0.1);
		scene.addItem(new Item(new Plane(o,n), floor));
		
		Material red = new MaterialReflective(Color.RED, Color.WHITE, 0.5);
		Material green = new MaterialReflective(Color.GREEN, Color.WHITE, 0.5);
		Material blue = new MaterialReflective(Color.BLUE, Color.WHITE, 0.5);

		Transformation t_center = Transformation.createTranslation(0, -2, 20);
		Transformation t_left = Transformation.createTranslation(-5, -2, 9);
		Transformation t_right = Transformation.createTranslation(5, -2, 15);
		
		scene.addItem(new Item(new Sphere(t_center, 3), red));
		scene.addItem(new Item(new Sphere(t_left, 3), blue));
		scene.addItem(new Item(new Sphere(t_right, 3), green));

		scene.addLight(new DirectionalLight(new Vector(1, 1, -1), Color.WHITE, 1));
		//scene.setEnvMap(new ConstantTexture(new Color(0.2,0.1,0.3)));
		FileReader envMapReader = new FileReader("src/resources/envMap2.jpg");
		Texture envMap = new ImageTexture(envMapReader.getImage());
		scene.setEnvMap(envMap);
		
		//Transformation t3 = Transformation.createTranslation(-3.3, -5, 5.8);
		//scene.addItem(new Item(new TransformedShape(t3, new Cylinder(0.5,3)), Color.YELLOW));
		

		//	FileReader envMapReader = new FileReader("src/resources/envMap2.jpg");
		//	Texture envMap = new ImageTexture(envMapReader.getImage());
//		scene.setEnvMap(envMap);
		return scene;
	}
	
	//materials on sphere with cylinder
	public static Scene generateScene21(){
		Scene scene = new Scene();
		Point o = new Point(0,-5,0);
		Vector n = new Vector(0,1,0);
		scene.addItem(new Item(new Plane(o,n), Color.GRAY));
		
		Transformation t1 = Transformation.createTranslation(0, 0, 10);
		//MaterialGlossy material = new MaterialGlossy(new Color(1,0.8,0.6), new Color(1,0.9,0.9), 0.2, 0, 0.8, 1000);
		//MaterialPhong material = new MaterialPhong(new Color(1,0.8,0.6), new Color(1,0.9,0.9), 0.2, 0, 0.8, 1000);
		Material material = MaterialWardDuer.DELRIN;

		scene.addItem(new Item(new Sphere(t1, 5), material));
		
		Transformation t3 = Transformation.createTranslation(-3.3, -5, 5.8);
		scene.addItem(new Item(new TransformedShape(t3, new Cylinder(0.5,3)), Color.YELLOW));
		
		//AreaLight areaLight = new AreaLight(new Point(1,3,2), new Point(3,5,4), Color.WHITE, 500);
		AreaLight areaLight = new AreaLight(new Point(1,1,2), new Point(1.9,1.9,2.01), Color.WHITE, 30);
		scene.addLight(areaLight);
		scene.addItem(areaLight.getAssociatedLightItem());
		AreaLight areaLight2 = new AreaLight(new Point(-3,5.5,11), new Point(-2,9.5,11), Color.WHITE, 400);
		scene.addLight(areaLight2);
		scene.addItem(areaLight2.getAssociatedLightItem());

		//	FileReader envMapReader = new FileReader("src/resources/envMap2.jpg");
		//	Texture envMap = new ImageTexture(envMapReader.getImage());
//		scene.setEnvMap(envMap);
		return scene;
	}
	
	//sierpinski by reflective spheres
	public static Scene generateScene22(){
		Scene scene = new Scene();
		//double distance = 5;
		double distance = 1.5;
		Transformation t = Transformation.createTranslation(0, 0, distance).append(Transformation.createRotationX(37.5)).append(Transformation.createRotationY(45));
		Transformation t1 = t.append(Transformation.createTranslation(-1, -1, -1));
		Transformation t2 = t.append(Transformation.createTranslation(-1, 1, 1));
		Transformation t3 = t.append(Transformation.createTranslation(1, -1, 1));
		Transformation t4 = t.append(Transformation.createTranslation(1, 1, -1));
		
		//good values: 0.1,0.1,1 or 0.15,0,1 or 0.15,0,1.1 or 0.015, 0, 1.3
		double vibrancy = 0.02;
		double white = 0.16;
		double reflection = 1.00;
		
		MaterialReflective m1 = new MaterialReflective(Color.WHITE.scale(vibrancy), Color.WHITE, reflection);
		MaterialReflective m2 = new MaterialReflective(Color.WHITE.scale(white), Color.WHITE, reflection);
		MaterialReflective m3 = new MaterialReflective(Color.WHITE.scale(vibrancy), Color.WHITE, reflection);
		MaterialReflective m4 = new MaterialReflective(Color.WHITE.scale(vibrancy), Color.WHITE, reflection);
		
		m1.ka_after = 1;
		m2.ka_after = 1;	
		m3.ka_after = 1;
		m4.ka_after = 1;
		
		m1.maxRecursionDepth = 50;
		m2.maxRecursionDepth = 50;
		m3.maxRecursionDepth = 50;
		m4.maxRecursionDepth = 50;

		scene.addItem(new Item(new Sphere(t1, Math.sqrt(2)), m1 ));
		scene.addItem(new Item(new Sphere(t2, Math.sqrt(2)), m2 ));
		scene.addItem(new Item(new Sphere(t3, Math.sqrt(2)), m3 ));
		scene.addItem(new Item(new Sphere(t4, Math.sqrt(2)), m4 ));

		//scene.addLight(new DirectionalLight(new Vector(1, 1, -10), Color.WHITE, Math.PI));
		//scene.addLight(new PointLight(new Point(0,0,4), Color.RED, 0.1));
		FileReader envMapReader = new FileReader("src/resources/envMap2.jpg");
		Texture envMap = new ImageTexture(envMapReader.getImage());
		//scene.setEnvMap(envMap); //comment this
		scene.setEnvMap(new ConstantTexture(Color.BLACK));
		return scene;
	}

}
