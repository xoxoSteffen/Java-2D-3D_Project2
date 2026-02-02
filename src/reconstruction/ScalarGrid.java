package reconstruction;
import org.joml.Vector3f;

public class ScalarGrid {

    public final int nx, ny, nz;
    public final float h;
    public final Vector3f origin;
    public final float[] values;

    public ScalarGrid(int nx, int ny, int nz, float h, Vector3f origin) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.h = h;
        this.origin = origin;
        this.values = new float[nx * ny * nz];
    }

    public int index(int i, int j, int k) {
        return i + nx * (j + ny * k);
    }

    public Vector3f position(int i, int j, int k) {
        return new Vector3f(
                origin.x + i * h,
                origin.y + j * h,
                origin.z + k * h
        );
    }

    public void set(int i, int j, int k, float v) {
        values[index(i, j, k)] = v;
    }

    public float get(int i, int j, int k) {
        return values[index(i, j, k)];
    }
}
