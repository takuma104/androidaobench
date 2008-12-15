package aobench;

public class Vec3 {
	public double x, y, z;

	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public void normalize() {
		double dist = this.length();

		double invdist = 1.0;
		if (Math.abs(dist) > 1.0e-6) {
			invdist = 1.0 / dist;
		}

		this.x *= invdist;
		this.y *= invdist;
		this.z *= invdist;
	}

	public Vec3 add(Vec3 b) {
		return new Vec3(this.x + b.x, this.y + b.y, this.z + b.z);
	}

	public Vec3 sub(Vec3 b) {
		return new Vec3(this.x - b.x, this.y - b.y, this.z - b.z);
	}

	public Vec3 cross(Vec3 b) {
		double s = this.y * b.z - this.z * b.y;
		double t = this.z * b.x - this.x * b.z;
		double q = this.x * b.y - this.y * b.x;

		return new Vec3(s, t, q);
	}

	public double dot(Vec3 b) {
		return this.x * b.x + this.y * b.y + this.z * b.z;
	}
}
