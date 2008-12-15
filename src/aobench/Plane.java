package aobench;

public class Plane {

	public Vec3 p; // point on the plane
	public Vec3 n; // normal to the plane

	public Plane(Vec3 p, Vec3 n) {
		this.p = p;
		this.n = n;
	}

	public void intersect(Intersection isect, Ray ray) {
		// d = -(p . n)
		// t = -(ray.org . n + d) / (ray.dir . n)
		double d = -p.dot(n);
		double v = ray.dir.dot(n);

		if (Math.abs(v) < 1.0e-6)
			return; // the plane is parallel to the ray.

		double t = -(ray.org.dot(n) + d) / v;

		if ((t > 0) && (t < isect.t)) {
			isect.hit = true;
			isect.t = t;
			isect.n = n;

			Vec3 p = new Vec3(ray.org.x + t * ray.dir.x, ray.org.y + t
					* ray.dir.y, ray.org.z + t * ray.dir.z);
			isect.p = p;
		}
	}
}
