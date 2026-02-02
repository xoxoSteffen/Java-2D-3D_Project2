package reconstruction;
import org.joml.Vector3f;

public class RBFSample {
    public Vector3f position;
    public float value;

    public RBFSample(Vector3f position, float value) {
        this.position = position;
        this.value = value;
    }
}
