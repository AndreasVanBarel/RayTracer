package scene;

import java.util.ArrayList;
import java.util.List;

import texture.Texture;
import light.Light;
import main.ShadeRec;
import math.Color;
import math.Ray;

public class Scene {
	public List<Item> items = new ArrayList<Item>();
	public List<Light> lights = new ArrayList<Light>();
	private Texture envMap = null;
	
	public Scene(){
	}
	
	public Scene(List<Item> items){
		this.items = items;
	}
	
	public void addItem(Item item){
		items.add(item);
	}
	
	public void addLight(Light light){
		lights.add(light);
	}
	
	/**
	 * Genereert ShadeRecs voor elk object in de scene. De ShadeRec corresponderend met de eerste intersectie zal teruggegeven worden.
	 */
	public ShadeRec traceRay(Ray ray){
		/** calculate object that is intersected first */
		ShadeRec rec = new ShadeRec(ray);
		traceRay(rec);
		return rec;
	}
	
	public void traceRay(ShadeRec rec){
		/** calculate object that is intersected first */
		ShadeRec firstRec = new ShadeRec(rec);
		firstRec.time = Double.POSITIVE_INFINITY;
		for (Item item : items){
			item.intersect(rec);
			if (rec.hit && rec.time < firstRec.time){ //belangrijk rec.hit want we herbruiken dezelfde rec.
				firstRec = new ShadeRec(rec);
			}
		}
		rec.copyFrom(firstRec);	
		rec.scene = this;
	}
	
	/** checks whether there is an object hit by the ray for a t between 0 and the given tmax*/
	public boolean traceShadow(Ray ray, double tmax){
		double temp;
		ShadeRec rec = new ShadeRec(ray);
		for (Item item : items){
			if(!item.castsShadows()) continue;
			item.intersectT(rec);
			temp = rec.time;
			if (temp < tmax){
				return true;
			}
		}
		return false;
	}
	
	public void setEnvMap(Texture envMap){
		this.envMap = envMap;
	}
	
	/**use this to shade if rec has not hit anything*/
	public Color shade(ShadeRec rec){
		if(envMap != null){			
			return envMap.getEnvColor(rec.ray);
		} else {
			return Color.BLACK; //default background
		}
	}
}
