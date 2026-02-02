package framework;
import org.joml.Vector3f;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;

public class Camera {
	// camera in world coordinates
	private final Matrix4f transform;

	public Camera() {
		transform = new Matrix4f();
		Vector3f eye = new Vector3f(0.0f, 0.0f, 1.0f);
		Vector3f center = new Vector3f(0.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		transform.lookAt(eye, center, up);
		transform.invert();
	}

	public Camera(Vector3f eye, Vector3f center, Vector3f up) {
		transform = new Matrix4f();
		transform.lookAt(eye, center, up);
		transform.invert();
	}

	public Matrix4f getTransform() {
		return transform;
	}

	public Matrix4f getCameraForModelView() {
		Matrix4f dst = new Matrix4f();
		return transform.invert(dst);
	}

	public void translate(Vector3f dir) {
		Matrix4f tmp = new Matrix4f();
		tmp.setTranslation(dir);
		tmp.mul(transform);
		transform.set(tmp);
	}

	public void rotate(Vector3f axis, float angle) {
		AxisAngle4f aa = new AxisAngle4f(angle, axis);
		Matrix4f tmp = new Matrix4f();
		tmp.set(aa);
		tmp.mul(transform);
		transform.set(tmp);
	}

	public Vector3f getUp() {
		Vector3f y = new Vector3f(0.0f, 1.0f, 0.0f);
		return y.mulDirection(transform);
	}

	public Vector3f getRight() {
		Vector3f x = new Vector3f(1.0f, 0.0f, 0.0f);
		return x.mulDirection(transform);
	}

	public Vector3f getDirection() {
		Vector3f minus_z = new Vector3f(0.0f, 0.0f, -1.0f);
		return minus_z.mulDirection(transform);
	}

	public float getDistFromOrigin() {
		// get position
		Vector3f orig = new Vector3f(0.0f, 0.0f, 0.0f);
		Vector3f newOrig = orig.mulPosition(transform);

		return (float) Math.sqrt(newOrig.x * newOrig.x + newOrig.y * newOrig.y + newOrig.z * newOrig.z);
	}
}
