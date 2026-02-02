package reconstruction;
import java.util.ArrayList;
import org.joml.Vector3f;

public class PointCloud {
    public final ArrayList<Vector3f> points = new ArrayList<>();
    public final ArrayList<Vector3f> normals = new ArrayList<>();

    public int size() {
        return points.size();
    }

    public void add(Vector3f p, Vector3f n) {
        points.add(p);
        normals.add(n);
    }

    public void centerAndNormalize() {
    if (points.isEmpty()) return;

    // Center 
    Vector3f c = new Vector3f(0, 0, 0);
    for (Vector3f p : points) c.add(p);
    c.mul(1.0f / points.size());

    for (Vector3f p : points) p.sub(c);

    // Normalize 
    Vector3f lc = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
    Vector3f uc = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

    for (Vector3f p : points) {
        lc.x = Math.min(lc.x, p.x); lc.y = Math.min(lc.y, p.y); lc.z = Math.min(lc.z, p.z);
        uc.x = Math.max(uc.x, p.x); uc.y = Math.max(uc.y, p.y); uc.z = Math.max(uc.z, p.z);
    }

    float len = lc.distance(uc);
    if (len < 1e-12f) return;

    float inv = 1.0f / len;
    for (Vector3f p : points) p.mul(inv);
}

}
