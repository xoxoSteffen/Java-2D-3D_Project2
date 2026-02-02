package framework;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.FileReader;

public class Renderer {
	private Shader shader;

	private String vsFileName;
	private String fsFileName;

		
	public Renderer(String vs, String fs) {
		vsFileName = vs;
		fsFileName = fs;
	}

	private static String loadText(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));

		String result = "";
		String line;

		while ((line = br.readLine()) != null) {
			result += line + "\n";
		}

		br.close();

		return result;
	}

	public void init(WindowManager window) throws Exception {
		shader = new Shader();
		shader.createVertexShader(loadText(vsFileName));
		shader.createFragmentShader(loadText(fsFileName));
		shader.link();

		// transforms
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelViewMatrix");
		shader.createUniform("normalMatrix");

		// material
		shader.createUniform("ambient");
		shader.createUniform("diffuse");
		shader.createUniform("specular");
		shader.createUniform("ambientLight");
		shader.createUniform("shininess");

		// light attributes
		shader.createUniform("lColor");
		shader.createUniform("lPosition");
		shader.createUniform("lIntensity");
	}

	public Shader getShaderProgram() {
		return shader;
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(WindowManager window, Camera camera, MeshNode mesh, Vector3f ambientLight,
			PointLight pointLight) {
		clear();
		shader.bind();

		Matrix4f projectionMatrix = computeProjectionMatrix((float) Math.PI/4.0f, window.getWidth(), window.getHeight(), 0.1f, 100.0f);
		shader.setUniform("projectionMatrix", projectionMatrix);

		Matrix4f viewMatrix = getViewMatrix(camera);

		shader.setUniform("ambientLight", ambientLight);

		// update the light position
		PointLight currPointLight = new PointLight(pointLight);
		Vector3f lightPos = currPointLight.position;
		Vector4f aux = new Vector4f(lightPos, 1);
		aux.mul(viewMatrix);
		lightPos.x = aux.x;
		lightPos.y = aux.y;
		lightPos.z = aux.z;

		shader.setUniform("lPosition", lightPos);
		shader.setUniform("lColor", currPointLight.color);
		shader.setUniform("lIntensity", currPointLight.intensity);

		Matrix4f modelViewMatrix = getModelViewMatrix(mesh, viewMatrix);
		shader.setUniform("modelViewMatrix", modelViewMatrix);

		Matrix4f normalMatrix = new Matrix4f(modelViewMatrix);
		normalMatrix.invert().transpose();
		shader.setUniform("normalMatrix", normalMatrix);

		shader.setUniform("ambient", mesh.getMaterial().ambientColor);
		shader.setUniform("diffuse", mesh.getMaterial().diffuseColor);
		shader.setUniform("specular", mesh.getMaterial().specularColor);
		shader.setUniform("shininess", mesh.getMaterial().shininess);

		mesh.render();

		shader.unbind();
	}

	public void cleanup() {
		if (shader != null) {
			shader.cleanup();
		}
	}

	// compute the transforms (projection, model-view)
	private static Matrix4f computeProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return projectionMatrix;
	}

	private static Matrix4f getViewMatrix(Camera camera) {
		return camera.getCameraForModelView();
	}

	private static Matrix4f getModelViewMatrix(MeshNode mesh, Matrix4f viewMatrix) {
		Matrix4f modelViewMatrix = mesh.getModelTransform();
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(modelViewMatrix);
	}
}
