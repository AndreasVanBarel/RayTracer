package shape;

import main.ShadeRec;
import math.Ray;
import math.Transformation;

public class TransformedShape extends Shape {

	public Transformation tf;
	public Shape base;
	
	public TransformedShape(Transformation tf, Shape base){
		this.tf = tf;
		this.base = base;
	}
	
	@Override
	public void intersect(ShadeRec rec) {		
		Ray rayOriginal = rec.ray;
		Ray ray2 = tf.transformInverse(rayOriginal);
		rec.ray = ray2;
		base.intersect(rec);
		if (rec.hit){
			rec.hitpoint = tf.transform(rec.hitpoint);
			rec.normalvector = tf.getInverseTransformationMatrix().transpose().transform(rec.normalvector);
		}
		rec.ray = rayOriginal;
	}

	@Override
	public void intersectT(ShadeRec rec) {
		Ray original = rec.ray;
		rec.ray = tf.transformInverse(original);
		base.intersectT(rec);
		rec.ray = original;
	}
	
	public void setTransformation(Transformation tf){
		this.tf = tf;
	}
	
	public void appendTransformation(Transformation tf){
		this.tf = this.tf.append(tf);
	}

	//@Override
	public AABB getAABB() {
		// TODO Auto-generated method stub
		return null;
	}

}
