package aobench;

public class Sphere {

	public Vec3 center;
	public double radius;

	public Sphere(Vec3 center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public void intersect(Intersection isect, Ray ray) {
		// rs = ray.org - sphere.center
		Vec3 rs = ray.org.sub(this.center);
		double B = rs.dot(ray.dir);
		double C = rs.dot(rs) - (this.radius * this.radius);
		double D = B * B - C;

		if (D > 0.0) {
			double t = -B - Math.sqrt(D);
			if ((t > 0.0) && (t < isect.t)) {
				isect.t = t;
				isect.hit = true;

				// calculate normal.
				Vec3 p = new Vec3(ray.org.x + ray.dir.x * t, ray.org.y
						+ ray.dir.y * t, ray.org.z + ray.dir.z * t);
				Vec3 n = p.sub(center);
				n.normalize();
				isect.n = n;
				isect.p = p;
			}
		}
	}
}
