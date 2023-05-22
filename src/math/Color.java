package math;

public class Color {
	public final double red;
	public final double green;
	public final double blue;
		
	public Color(double red, double green, double blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public static final Color WHITE = new Color(1,1,1);
	public static final Color BLACK = new Color(0,0,0);
	public static final Color GRAY = new Color(0.5,0.5,0.5);
	public static final Color RED = new Color(1,0,0);
	public static final Color GREEN = new Color(0,1,0);
	public static final Color BLUE = new Color(0,0,1);
	public static final Color YELLOW = new Color(1,1,0);
	public static final Color CYAN = new Color(0,1,1);
	public static final Color MAGENTA = new Color(1,0,1);
	
	//Tone Mapping Operators
	private double getScalingfactor(){
		double max = Utility.max(Utility.max(red, green), blue);
		return Utility.min(1,1/max);
	}
	
	public int redToneMapped(){
		return (int) Math.round(red*255*getScalingfactor());
	}
	
	public int greenToneMapped(){
		return (int) Math.round(green*255*getScalingfactor());
	}
	
	public int blueToneMapped(){
		return (int) Math.round(blue*255*getScalingfactor());
	}
	
	//Color operations
	public Color add(Color color){
		return new Color(this.red + color.red, this.green + color.green, this.blue + color.blue);
	}
	
	public Color multiply(Color color){
		return new Color(this.red * color.red, this.green * color.green, this.blue * color.blue);
	}
	
	public Color scale(double scalar){
		return new Color(this.red * scalar, this.green * scalar, this.blue * scalar);
	}
	
	public String toString(){
		return "red: " + red + ", green: " + green + ", blue: " + blue;
	}
	
	public Color exp(double a){
		return new Color(Math.pow(red, a), Math.pow(green, a), Math.pow(blue, a));
	}
}
