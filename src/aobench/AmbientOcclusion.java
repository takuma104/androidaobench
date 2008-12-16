package aobench;

public class AmbientOcclusion {

	public static final int NAO_SAMPLES = 8;

	private Sphere[] spheres;
	private Plane plane;

	public AmbientOcclusion() {
		initScene();
	}

	private void initScene() {
		spheres = new Sphere[3];
		spheres[0] = new Sphere(new Vec3(-2.0, 0.0, -3.5), 0.5);
		spheres[1] = new Sphere(new Vec3(-0.5, 0.0, -3.0), 0.5);
		spheres[2] = new Sphere(new Vec3(1.0, 0.0, -2.2), 0.5);
		plane = new Plane(new Vec3(0.0, -0.5, 0.0), new Vec3(0.0, 1.0, 0.0));
	}

	private int mkColor(double r, double g, double b) {
		int ri = (int) (r * 255.5);
		if (ri < 0)
			ri = 0;
		if (ri > 255)
			ri = 255;

		int gi = (int) (g * 255.5);
		if (gi < 0)
			gi = 0;
		if (gi > 255)
			gi = 255;

		int bi = (int) (b * 255.5);
		if (bi < 0)
			bi = 0;
		if (bi > 255)
			bi = 255;

		return 0xff000000 | (ri << 16) | (gi << 8) | bi;
	}

	private void orthoBasis(Vec3[] basis, Vec3 n) {
		basis[2] = new Vec3(n.x, n.y, n.z);
		basis[1] = new Vec3(0.0, 0.0, 0.0);

		if ((n.x < 0.6) && (n.x > -0.6)) {
			basis[1].x = 1.0;
		} else if ((n.y < 0.6) && (n.y > -0.6)) {
			basis[1].y = 1.0;
		} else if ((n.z < 0.6) && (n.z > -0.6)) {
			basis[1].z = 1.0;
		} else {
			basis[1].x = 1.0;
		}

		basis[0] = basis[1].cross(basis[2]);
		basis[0].normalize();

		basis[1] = basis[2].cross(basis[0]);
		basis[0].normalize();
	}

	private Vec3 computeAO(Intersection isect) {
		int i, j;

		int ntheta = NAO_SAMPLES;
		int nphi = NAO_SAMPLES;
		double eps = 0.0001;

		// Slightly move ray org towards ray dir to avoid numerical probrem.
		Vec3 p = new Vec3(isect.p.x + eps * isect.n.x, isect.p.y + eps
				* isect.n.y, isect.p.z + eps * isect.n.z);

		// Calculate orthogonal basis.
		Vec3[] basis = new Vec3[3];
		orthoBasis(basis, isect.n);

		double occlusion = 0.0;

		for (j = 0; j < ntheta; j++) {
			for (i = 0; i < nphi; i++) {

				// Pick a random ray direction with importance sampling.
				// p = cos(theta) / PI
				double r = Math.random();
				double phi = 2.0 * Math.PI * Math.random();

				double x = Math.cos(phi) * Math.sqrt(1.0 - r);
				double y = Math.sin(phi) * Math.sqrt(1.0 - r);
				double z = Math.sqrt(r);

				// local -> global
				double rx = x * basis[0].x + y * basis[1].x + z * basis[2].x;
				double ry = x * basis[0].y + y * basis[1].y + z * basis[2].y;
				double rz = x * basis[0].z + y * basis[1].z + z * basis[2].z;

				Vec3 raydir = new Vec3(rx, ry, rz);

				Ray ray = new Ray(p, raydir);

				Intersection occIsect = new Intersection();
				spheres[0].intersect(occIsect, ray);
				spheres[0].intersect(occIsect, ray);
				spheres[0].intersect(occIsect, ray);
				plane.intersect(occIsect, ray);

				if (occIsect.hit)
					occlusion += 1.0;

			}
		}

		// [0.0, 1.0]
		occlusion = (ntheta * nphi - occlusion) / (ntheta * nphi);

		return new Vec3(occlusion, occlusion, occlusion);
	}

	public int[] render(int w, int h, int nsubsamples) {
		int[] pixels = new int[w * h];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				Vec3 fcol = new Vec3(0.0, 0.0, 0.0);

				// subsampling
				for (int v = 0; v < nsubsamples; v++) {
					for (int u = 0; u < nsubsamples; u++) {
						double px = (i + (u / (double) (nsubsamples)) - (w / 2.0))
								/ (w / 2.0);
						double py = (j + (v / (double) (nsubsamples)) - (h / 2.0))
								/ (h / 2.0);
						py = -py; // flip Y

						// trace(py);

						// double t = 10000.0;
						Vec3 eye = new Vec3(px, py, -1.0);
						eye.normalize();

						Ray ray = new Ray(new Vec3(0.0, 0.0, 0.0), new Vec3(
								eye.x, eye.y, eye.z));

						Intersection isect = new Intersection();
						spheres[0].intersect(isect, ray);
						spheres[0].intersect(isect, ray);
						spheres[0].intersect(isect, ray);
						plane.intersect(isect, ray);

						if (isect.hit) {

							Vec3 col = computeAO(isect);

							fcol.x += col.x;
							fcol.y += col.y;
							fcol.z += col.z;
						}
					}
				}

				fcol.x /= (double) (nsubsamples * nsubsamples);
				fcol.y /= (double) (nsubsamples * nsubsamples);
				fcol.z /= (double) (nsubsamples * nsubsamples);

				pixels[j * w + i] = mkColor(fcol.x, fcol.y, fcol.z);
			}
		}

		return pixels;
	}

}
