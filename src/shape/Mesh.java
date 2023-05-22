package shape;

import java.util.ArrayList;

import texture.Texture;
import main.Logger;
import main.ShadeRec;
import math.Point;
import math.Ray;
import math.UV;
import math.Vector;

public class Mesh extends Shape {

	private ArrayList<Point> vertices = new ArrayList<Point>(); //vertices
	private ArrayList<UV> uvcoords = new ArrayList<UV>(); //texture coordinates
	private ArrayList<Vector> normals = new ArrayList<Vector>(); //vertex normals
	private ArrayList<MeshTriangle> triangles = new ArrayList<MeshTriangle>(); //triangles
	
	private Texture normalMap = null;
	
	public Mesh(ArrayList<Point> vertices, ArrayList<UV> uvcoords, ArrayList<Vector> normals){
		this.vertices = vertices;
		this.uvcoords = uvcoords;
		this.normals = normals;
	}
	
	public void add(MeshTriangle mt){
		triangles.add(mt);
	}
	
	public void setNormalMap(Texture normalMap){
		this.normalMap = normalMap;
	}
	
	public boolean hasNormalMap(){
		return normalMap != null;
	}
	
	public Vector getNormalMap(UV uv){
		return normalMap.getNMap(uv);
	}
	
	@Override
	public void intersect(ShadeRec rec) {
		/** calculate object that is intersected first */
		double t = Double.POSITIVE_INFINITY;
		double temp;
		MeshTriangle firstTriangle = null;
		for (MeshTriangle mt : triangles){
			mt.intersectT(rec);
			temp = rec.time;
			if (temp < t){
				firstTriangle = mt;
				t = temp;
			}
		}
		if(firstTriangle == null){
			rec.hit = false;
		} else {
			firstTriangle.intersect(rec);
		}
	}

	@Override
	public void intersectT(ShadeRec rec) {
		double t = Double.POSITIVE_INFINITY;
		double temp;
		for (MeshTriangle mt : triangles){
			mt.intersectT(rec);
			temp = rec.time;
			if (temp < t){
				t = temp;
			}
		}
		rec.time = t;
	}
	
	public Point getVertex(int index){
		return vertices.get(index);
	}
	
	public UV getUV(int index){
		return uvcoords.get(index);
	}
	
	public Vector getNormal(int index){
		return normals.get(index);
	}
	
	public ArrayList<MeshTriangle> getTriangles(){
		return triangles;
	}
	
//	public void addToScene(Scene scene){
//		for (MeshTriangle triangle : triangles){
//			scene.addItem(triangle);
//		}
//	}

}
