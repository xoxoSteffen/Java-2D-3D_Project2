package reconstruction;
import java.util.ArrayList;
import org.joml.Vector3f;

public class RBFSampleGenerator {

    public static ArrayList<RBFSample> generate(PointCloud pc, float epsilon) {
        ArrayList<RBFSample> samples = new ArrayList<>();

        for (int i = 0; i < pc.size(); i++) {
            Vector3f p = pc.points.get(i);
            Vector3f n = new Vector3f(pc.normals.get(i)).normalize();

            // Surface sample
            samples.add(new RBFSample(new Vector3f(p), 0.0f));

            // Outside sample
            Vector3f pOut = new Vector3f(p).add(new Vector3f(n).mul(epsilon));
            samples.add(new RBFSample(pOut, +1.0f));

            // Inside sample
            Vector3f pIn = new Vector3f(p).sub(new Vector3f(n).mul(epsilon));
            samples.add(new RBFSample(pIn, -1.0f));
        }

        return samples;
    }
}
