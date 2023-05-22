package scene;
import shape.*;
import texture.Texture;
import main.ShadeRec;
import material.Material;
import material.MaterialPhong;
import material.MaterialTextured;
import math.*;

/**
 * Een item heeft een shape, en verschillende parameters. Informatie over kleur of textuur is hierin terug te vinden.
 * @author Andreas
 *
 */
public class Item {
	public Shape shape;
	public Material material;
	private boolean castsShadows = true;

	/** Standard constructor; provide a Shape and a Material to define the object*/
	public Item(Shape shape, Material material){
		this.shape = shape;
		this.material = material;
	}

	/** The color provided generates the material as a MaterialNormal with this color.*/
	public Item(Shape shape, Color color){
		this(shape, new MaterialPhong(color,color));
	}
	
	/** The texture provided generates the material as a MaterialTextured with this texture.*/
	public Item(Shape shape, Texture texture){
		this(shape, new MaterialTextured(texture));
	}
	
	public void intersectT(ShadeRec rec){
		shape.intersectT(rec);
	}
	
	/**
	 * Nodig uit rec: ray
	 * Correct na de oproep: hit, hitpoint, normalvector, ray(unchanged), uv(if applicable), time, material, item
	 */
	public void intersect(ShadeRec rec){
		shape.intersect(rec);
		rec.material = material;
		rec.item = this;
	}
	
	public double getEps(){
		return 0.0000001; //misschien best terugbrengen tot de shapes zelf
	}
	
	/**
	 * Returns whether this item should cast shadows
	 */
	public boolean castsShadows(){
		return castsShadows;
	}
	public void setCastsShadows(boolean bool){
		castsShadows = bool;
	}
}
