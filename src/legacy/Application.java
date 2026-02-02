package legacy;
import framework.*;
import geometry.TriangleMesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class Application {
	private WindowManager window;
	private MouseManager mouse;
	private Renderer renderer;
	private MeshNode mesh;
	private Camera camera;
	private Vector3f cameraStep;
	private Vector3f ambientLightColor;
	private PointLight pointLight;

	
	public Application(String windowTitle, int width, int height, String meshFileName, String vsFileName,
			String fsFileName) throws Exception {
		window = new WindowManager(windowTitle, width, height);
		mouse = new MouseManager();

		renderer = new Renderer(vsFileName, fsFileName);
		camera = new Camera();
		cameraStep = new Vector3f(0.0f, 0.0f, 0.0f);

		ambientLightColor = new Vector3f(0.3f, 0.3f, 0.3f);
		Vector3f lightColor = new Vector3f(1, 1, 1);
		Vector3f lightPosition = new Vector3f(0, 0, 1); // in camera space
		float lightIntensity = 1.0f;
		pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
		
		init(meshFileName);
	}

	private void init(String meshFileName) throws Exception {
		window.init();
		mouse.init(window);
		renderer.init(window);

		TriangleMesh triMesh = OFFLoader.loadMesh(meshFileName);
		triMesh.centerMesh();
		triMesh.normalizeMesh();
		triMesh.computeVertexNormal();

		mesh = triMesh.getMeshNode(renderer.getShaderProgram());
		mesh.setMaterial(new Material());
	}

	public void mainLoop() {
		while (!window.windowShouldClose()) {
			getInput();
			update();
			render();
		}
	}

	// get input from the keyboard and the mouse
	private void getInput() {
		cameraStep.set(0, 0, 0);

		if (window.isKeyPressed(GLFW_KEY_LEFT))
			cameraStep.x = -1;
		else if (window.isKeyPressed(GLFW_KEY_RIGHT))
			cameraStep.x = 1;

		if (window.isKeyPressed(GLFW_KEY_DOWN))
			cameraStep.y = -1;
		else if (window.isKeyPressed(GLFW_KEY_UP))
			cameraStep.y = 1;
		
		if (window.isKeyPressed(GLFW_KEY_PAGE_UP))
			cameraStep.z = -1;
		else if (window.isKeyPressed(GLFW_KEY_PAGE_DOWN))
			cameraStep.z = 1;


		mouse.input(window);
	}

	private void update() {
		// translate camera
		Vector3f transCam = new Vector3f(cameraStep.x * 0.0005f, cameraStep.y * 0.0005f,
				cameraStep.z * 0.0005f);
		camera.translate(transCam);

		// rotate model (pseudo-trackball)
		if (mouse.isLeftButtonPressed()) {
			Vector2f rotVec = mouse.getDisplacement();
			float distFromOrigin = camera.getDistFromOrigin();
			if (Math.abs(distFromOrigin) < 1e-5f)
				return;

			float len = (float) Math.sqrt(rotVec.x * rotVec.x + rotVec.y * rotVec.y) / (120.0f * distFromOrigin);
			if (Math.abs(len) < 1e-5f)
				return;

			Vector3f up = camera.getUp();
			Vector3f right = camera.getRight();
			Vector3f axis = up.mul(rotVec.x);
			axis.add(right.mul(rotVec.y));
			axis.normalize();
			Vector3f dir = camera.getDirection();
			Vector3f dir1 = new Vector3f(dir);
			camera.translate(dir1.mul(distFromOrigin));
			camera.rotate(axis, len);
			Vector3f dir2 = new Vector3f(camera.getDirection());
			camera.translate(dir2.mul(-distFromOrigin));
		}
	}

	private void render() {
		renderer.render(window, camera, mesh, ambientLightColor, pointLight);
		window.update();
	}

	public void cleanup() {
		renderer.cleanup();
		mesh.cleanup();
	}
}
