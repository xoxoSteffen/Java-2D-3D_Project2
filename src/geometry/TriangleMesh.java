package geometry;
import org.joml.Vector3f;

import framework.MeshNode;
import framework.Shader;

public class TriangleMesh {
	public int numberVertices = 0;
	int numberFaces = 0;

	Vector3f[] point = null;
	Vector3f[] normal = null;
	int[][] face = null;

	public TriangleMesh(int nv, int nf) {
		numberVertices = nv;
		numberFaces = nf;
		point = new Vector3f[numberVertices];
		face = new int[numberFaces][3];
	}

	public void setVertex(int i, float x, float y, float z) {
		point[i] = new Vector3f(x, y, z);
	}

	public void setFace(int i, int fi, int fj, int fk) {
		face[i][0] = fi;
		face[i][1] = fj;
		face[i][2] = fk;
	}

	public void centerMesh() {
		Vector3f c = new Vector3f(0.0f, 0.0f, 0.0f);
		for (int i = 0; i < point.length; i++) {
			c.add(point[i]);
		}
		c.mul(1.0f / point.length);

		for (int i = 0; i < point.length; i++) {
			point[i].sub(c);
		}
	}

	private void computeBBox(Vector3f lc, Vector3f uc) {
		lc.x = Float.MAX_VALUE;
		lc.y = Float.MAX_VALUE;
		lc.z = Float.MAX_VALUE;

		uc.x = -Float.MAX_VALUE;
		uc.y = -Float.MAX_VALUE;
		uc.z = -Float.MAX_VALUE;

		for (int i = 0; i < numberVertices; ++i) {
			Vector3f curr = point[i];
			float x = curr.x;
			float y = curr.y;
			float z = curr.z;

			lc.x = Math.min(lc.x, x);
			lc.y = Math.min(lc.y, y);
			lc.z = Math.min(lc.z, z);

			uc.x = Math.max(uc.x, x);
			uc.y = Math.max(uc.y, y);
			uc.z = Math.max(uc.z, z);
		}
	}

	private float computeBBoxDiagonalLength() {
		Vector3f lowerCorner = new Vector3f();
		Vector3f upperCorner = new Vector3f();
		computeBBox(lowerCorner, upperCorner);
		System.out.println(lowerCorner);
		System.out.println(upperCorner);
		float len = lowerCorner.distance(upperCorner);
		System.out.println(len);
		return len;
	}

	// Normalize the model  that it fits in a bounding box
	// with unit diagonal
	public void normalizeMesh() {
		float len = computeBBoxDiagonalLength();
		if (len == 0.0f) {
			System.out.println("BBox with zero length ...");
			System.exit(1);
		}

		for (int i = 0; i < numberVertices; ++i) {
			point[i].mul(1.0f / len);
		}
	}

	public void computeVertexNormal() {
		normal = new Vector3f[numberVertices];
		for (int i = 0; i < numberVertices; ++i) {
			normal[i] = new Vector3f();
		}

		for (int i = 0; i < numberFaces; ++i) {
			int v0 = face[i][0];
			int v1 = face[i][1];
			int v2 = face[i][2];

			Vector3f p0 = point[v0];
			Vector3f p1 = point[v1];
			Vector3f p2 = point[v2];

			Vector3f p0p1 = new Vector3f(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
			Vector3f p0p2 = new Vector3f(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
			Vector3f ni = p0p1.cross(p0p2);

			normal[v0].add(ni);
			normal[v1].add(ni);
			normal[v2].add(ni);
		}

		for (int i = 0; i < numberVertices; ++i) {
			normal[i].normalize();
		}
	}

	public MeshNode getMeshNode(Shader program) throws Exception {
		float[] coords = new float[3 * numberVertices];
		for (int i = 0; i < numberVertices; i++) {
			coords[3 * i] = point[i].x;
			coords[3 * i + 1] = point[i].y;
			coords[3 * i + 2] = point[i].z;
		}

		float[] normals = new float[3 * numberVertices];
		for (int i = 0; i < numberVertices; i++) {
			normals[3 * i] = normal[i].x;
			normals[3 * i + 1] = normal[i].y;
			normals[3 * i + 2] = normal[i].z;
		}

		// assume triangle mesh
		int[] idx = new int[3 * numberFaces];
		for (int i = 0; i < numberFaces; i++) {
			idx[3 * i] = face[i][0];
			idx[3 * i + 1] = face[i][1];
			idx[3 * i + 2] = face[i][2];
		}

		return new MeshNode(program, coords, normals, idx);
	}
}
