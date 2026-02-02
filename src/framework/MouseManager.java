package framework;
import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseManager {
	private final Vector2d previous;
	private final Vector2d current;
	private final Vector2f disp;
	private boolean inWindow = false;
	private boolean leftButtonPressed = false;
	private boolean rightButtonPressed = false;

	public MouseManager() {
		previous = new Vector2d(-1, -1);
		current = new Vector2d(0, 0);
		disp = new Vector2f(0.0f, 0.0f);
	}

	public void init(WindowManager window) {
		glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
			leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1) && (action == GLFW_PRESS);
			rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2) && (action == GLFW_PRESS);
		});

		glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
			current.x = xpos;
			current.y = ypos;
		});
		
		glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
			inWindow = entered;
		});
	}

	public Vector2f getDisplacement() {
		return disp;
	}

	public void input(WindowManager window) {
		disp.x = 0;
		disp.y = 0;

		if (previous.x > 0 && previous.y > 0 && inWindow) {
			disp.x = (float) (previous.x - current.x);
			disp.y = (float) (previous.y - current.y);
		}

		previous.x = current.x;
		previous.y = current.y;
	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}
}
