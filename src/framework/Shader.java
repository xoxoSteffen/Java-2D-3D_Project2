package framework;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

public class Shader {
	private int program;
	private int vertexShader;
	private int fragmentShader;
	
	private Map<String, Integer> uniforms;

	
	public Shader() throws Exception {
		program = glCreateProgram();
		
		if (program == 0) 
			throw new Exception("Failed to create Shader");
		
		uniforms = new HashMap<>();
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(program, uniformName);
		if (uniformLocation >= 0) {
			uniforms.put(uniformName, uniformLocation);
		}
	}

	public void setUniform(String uniformName, Matrix4f value) {
		if (uniforms.get(uniformName) == null)
			return;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
		}
	}

	public int getLocation(String name) throws Exception {
		int loc = glGetAttribLocation(program, name);
		
		if (loc < 0)
			throw new Exception("Invalid attribute name");
		
		return loc;
	}

	public void setUniform(String uniformName, int value) {
		if (uniforms.get(uniformName) == null)
			return;

		glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value) {
		if (uniforms.get(uniformName) == null)
			return;

		glUniform1f(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value) {
		if (uniforms.get(uniformName) == null)
			return;

		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, Vector4f value) {
		if (uniforms.get(uniformName) == null)
			return;

		glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}

	public void createVertexShader(String shaderCode) throws Exception {
		vertexShader = createShader(shaderCode, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShader = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		
		if (shaderId == 0) 
			throw new Exception("Error creating shader. Type: " + shaderType);

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));

		glAttachShader(program, shaderId);

		return shaderId;
	}

	public void link() throws Exception {
		glLinkProgram(program);
		
		if (glGetProgrami(program, GL_LINK_STATUS) == 0)
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(program, 1024));

		if (vertexShader != 0)
			glDetachShader(program, vertexShader);
		
		if (fragmentShader != 0)
			glDetachShader(program, fragmentShader);

		glValidateProgram(program);
		
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0)
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(program, 1024));
	}

	public void bind() {
		glUseProgram(program);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if (program != 0) {
			glDeleteProgram(program);
		}
	}
}
