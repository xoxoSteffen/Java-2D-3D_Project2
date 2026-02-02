package framework;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;
import org.joml.Matrix4f;


public class MeshNode {
	private int vao;
	private List<Integer> vboList;
	private int vertexCount;

	private Material material;
	private Matrix4f modelTransform = new Matrix4f();

	
	public MeshNode(Shader program, float[] positions, float[] normals, int[] indices) throws Exception {
		FloatBuffer positionsBuffer = null;
		FloatBuffer normalsBuffer = null;
		IntBuffer indicesBuffer = null;
		
		
		try {
			vertexCount = indices.length;
			vboList = new ArrayList<Integer>();

			vao = glGenVertexArrays();
			glBindVertexArray(vao);

			// vertex coordinates
			int vbo = glGenBuffers();
			vboList.add(vbo);
			positionsBuffer = MemoryUtil.memAllocFloat(positions.length);
			positionsBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(program.getLocation("vPosition"), 3, GL_FLOAT, false, 0, 0);

			// vertex normals
			vbo = glGenBuffers();
			vboList.add(vbo);
			normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			normalsBuffer.put(normals).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(program.getLocation("vNormal"), 3, GL_FLOAT, false, 0, 0);

			// indices
			vbo = glGenBuffers();
			vboList.add(vbo);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);

		} finally {

			if (positionsBuffer != null) {
				MemoryUtil.memFree(positionsBuffer);
			}

			if (normalsBuffer != null) {
				MemoryUtil.memFree(normalsBuffer);
			}

			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public void render() {
		glBindVertexArray(getVao());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// disable everything
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	
	// Model transformation
	public Matrix4f getModelTransform() {
		return modelTransform;
	}
	
	public void scale(float scale) {
		Matrix4f tmp = new Matrix4f();
		tmp.scale(scale);
		modelTransform.mul(tmp);
	}

	public void rotate(float x, float y, float z) {
		Matrix4f tmp = new Matrix4f();
		tmp.rotateXYZ(x, y, z);
		modelTransform.mul(tmp);
	}

	public void translate(float x, float y, float z) {
		Matrix4f tmp = new Matrix4f();
		tmp.translate(x, y, z);
		modelTransform.mul(tmp);		
	}

	public int getVao() {
		return vao;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void cleanup() {
		glDisableVertexAttribArray(0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vbo : vboList) {
			glDeleteBuffers(vbo);
		}

		glBindVertexArray(0);
		glDeleteVertexArrays(vao);
	}
}
