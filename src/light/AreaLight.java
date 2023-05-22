package light;

import main.ShadeRec;
import material.Material;
import material.MaterialLight;
import math.Color;
import math.Point;
import math.Utility;
import math.Vector;

import java.util.Random;

import scene.Item;
import shape.AABox;

public class AreaLight extends PointLight {

	private Point pmin;
	private Point pmax;
	private static int nbSamples = 5; //default value, kan onstatic gemaakt worden voor verschillende effecten per lichtbron.
	
	public AreaLight(Point pmin, Point pmax, Color color, double L_intensity){
		super(pmin.add(pmax.toVector3D()).scale(0.5), color, L_intensity);
		this.pmin = pmin;
		this.pmax = pmax;
	}
	
	@Override
	public boolean isAreaLight(){
		return true;
	}

	@Override
	public void cycleSample(){
		Random rand = new Random();
		Vector diff = pmax.subtract(pmin);
		Point s = new Point(pmin.x + diff.x*rand.nextDouble(), pmin.y + diff.y*rand.nextDouble(), pmin.z + diff.z*rand.nextDouble());
		position = s;
	}
	
	@Override
	public int nbSamples(){
		return nbSamples;
	}
	
	public Item getAssociatedLightItem(){
		//eigenlijk hangt deze schaling af van het oppervlak! (we nemen aan dat 1 coordinaat klein verschil heeft)
		Vector diff = pmax.subtract(pmin);
		double z1 = Utility.max(diff.x, diff.y);
		double z2 = Utility.max(diff.x, diff.z);
		if(z1 == z2){z2 = Utility.max(diff.y, diff.z);}
		double surface = z1*z2*2;
		// I know, the following is quick & dirty
		Item item = new Item(new AABox(pmin, pmax), new MaterialLight(this.color.scale(L/(surface*4*Math.PI)))); //Verander scaling om licht feller te maken in reflecties/refracties
		item.setCastsShadows(false);
		return item;
	}

}
