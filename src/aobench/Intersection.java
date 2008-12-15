package aobench;

public class Intersection {

	public double t;
	public Vec3 p; // hit point
	public Vec3 n; // normal
	public boolean hit;

	public Intersection() {
		hit = false;
		t = 1.0e+30;
		n = new Vec3(0.0, 0.0, 0.0);
	}
}
