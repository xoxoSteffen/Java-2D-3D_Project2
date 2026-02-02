package debug;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;

public class PointCloudNode {

    private final int vaoId;
    private final int vboPosId;
    private final int numPoints;

    private final int vboNrmId;

    private float pointSize = 3.0f;

    public PointCloudNode(float[] coords, float[] normals) {
        this.numPoints = coords.length / 3;

        // VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Positions VBO
        vboPosId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboPosId);

        FloatBuffer fb = MemoryUtil.memAllocFloat(coords.length);
        fb.put(coords).flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        MemoryUtil.memFree(fb);

        // Normals VBO attribute 
        vboNrmId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboNrmId);

        FloatBuffer nb = MemoryUtil.memAllocFloat(normals.length);
        nb.put(normals).flip();
        glBufferData(GL_ARRAY_BUFFER, nb, GL_STATIC_DRAW);
        MemoryUtil.memFree(nb);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Attribute 0 = position 
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void setPointSize(float size) {
        this.pointSize = size;
    }

    public void render() {
        glPointSize(pointSize);
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_POINTS, 0, numPoints);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboPosId);

        glBindVertexArray(0);
        glDeleteBuffers(vboNrmId);

        glDeleteVertexArrays(vaoId);
    }
}
