package framework;
import org.joml.Vector4f;

public class Material {
	public Vector4f ambientColor;
	public Vector4f diffuseColor;
	public Vector4f specularColor;
	public float shininess;

	private static final Vector4f DEFAULT = new Vector4f(1.0f, 0.8f, 1.0f, 1.0f);	//color 

	public Material() {
		ambientColor = DEFAULT;
		diffuseColor = DEFAULT;
		specularColor = DEFAULT;
		shininess = 10.0f;
	}

	public Material(Vector4f color) {
		this(color, color, color);
	}

	public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor) {
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		shininess = 10.0f;
	}

	public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, float shininess) {
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.shininess = shininess;
	}
}
