package framework;
import org.joml.Vector3f;

public class PointLight {
	public Vector3f color;
	public Vector3f position;
	public float intensity;

	public PointLight(Vector3f color, Vector3f position, float intensity) {
		this.color = color;
		this.position = position;
		this.intensity = intensity;
	}

	public PointLight(PointLight pointLight) {
		this(new Vector3f(pointLight.color), new Vector3f(pointLight.position), pointLight.intensity);
	}
}
