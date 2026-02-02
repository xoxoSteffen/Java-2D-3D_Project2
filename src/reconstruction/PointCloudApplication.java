package reconstruction;

import framework.*;
import geometry.TriangleMesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

public class PointCloudApplication {

    private WindowManager window;
    private MouseManager mouse;
    private Renderer renderer;

    private Camera camera;
    private Vector3f cameraStep;

    private Vector3f ambientLightColor;
    private PointLight pointLight;

    private MeshNode mesh;
    private String xyzFileName;

    public PointCloudApplication(String windowTitle, int width, int height,
            String xyzFileName, String vsFileName, String fsFileName) throws Exception {

        this.xyzFileName = xyzFileName;

        window = new WindowManager(windowTitle, width, height);
        mouse = new MouseManager();
        renderer = new Renderer(vsFileName, fsFileName);
        camera = new Camera();
        //camera.translate(new Vector3f(0.0f, 0.0f, 0.0f));
        cameraStep = new Vector3f(0.0f, 0.0f, 0.0f);

        // Light, Material parameter
        ambientLightColor = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColor = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, -2); // in camera space
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColor, lightPosition, lightIntensity);

        init();

    }

    // RBF + Grid + MC
    private void init() throws Exception {
        window.init();
        mouse.init(window);
        renderer.init(window);

        // Load point cloud
        PointCloud pc = XYZLoader.load(xyzFileName);
        pc.centerAndNormalize();

        // Generate RBF samples
        ArrayList<RBFSample> samples = RBFSampleGenerator.generate(pc, 0.01f);

        // Implicit Function
        ImplicitFunction f = new ImplicitFunction(samples, 0.02f);

        // Grid-Parameter
        int N = 50; // number of cells 
        float h = 0.024f; // res cell
        Vector3f origin = new Vector3f(-0.6f, -0.6f, -0.6f);

        // Grid create
        ScalarGrid grid = new ScalarGrid(N, N, N, h, origin);

        // fill grid with fx
        for (int k = 0; k < grid.nz; k++) {
            for (int j = 0; j < grid.ny; j++) {
                for (int i = 0; i < grid.nx; i++) {
                    Vector3f x = grid.position(i, j, k);
                    float v = f.evaluate(x);
                    grid.set(i, j, k, v);
                }
            }
        }

        // extracting mesh with mCubes
        TriangleMesh mcMesh = MarchingCubes.extract(grid);
        mcMesh.computeVertexNormal();

        mesh = mcMesh.getMeshNode(renderer.getShaderProgram());
        mesh.setMaterial(new Material());

        // debug output MC result
        // System.out.println("MC vertices = " + mcMesh.numberVertices);
        // System.out.println("MC faces = " + mcMesh.numberFaces);'
    }

    public void mainLoop() {
        while (!window.windowShouldClose()) {
            getInput();
            update();
            render();
        }
        cleanup();
    }

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

        if (window.isKeyPressed(GLFW_KEY_Z))
            cameraStep.z = -1;
        else if (window.isKeyPressed(GLFW_KEY_X))
            cameraStep.z = 1;

        mouse.input(window);
    }

    private void update() {
        // translate camera
        Vector3f transCam = new Vector3f(cameraStep.x * 0.0005f, cameraStep.y * 0.0005f,
                cameraStep.z * 0.0005f);
        camera.translate(transCam);

        // rotate model
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
            Vector3f axis = new Vector3f(up).mul(rotVec.x);
            axis.add(new Vector3f(right).mul(rotVec.y));
            axis.normalize();

            Vector3f dir = camera.getDirection();
            camera.translate(new Vector3f(dir).mul(distFromOrigin));
            camera.rotate(axis, len);
            camera.translate(new Vector3f(camera.getDirection()).mul(-distFromOrigin));
        }
    }

    private void render() {

        renderer.render(window, camera, mesh, ambientLightColor, pointLight);
        window.update();
    }

    private void cleanup() {
        renderer.cleanup();
        mesh.cleanup();
        
    }
}
