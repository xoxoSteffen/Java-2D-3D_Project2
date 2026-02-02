package reconstruction;
import java.util.List;
import org.joml.Vector3f;

public class ImplicitFunction {

    private final List<RBFSample> samples;
    private final float sigma;

    public ImplicitFunction(List<RBFSample> samples, float sigma) {
        this.samples = samples;
        this.sigma = sigma;
    }

    public float evaluate(Vector3f x) {
        float sum = 0.0f;

        float cutoff = 3.0f * sigma; // 3*sigma is almost 0 gaus
        float cutoff2 = cutoff * cutoff;

        for (RBFSample s : samples) {
            float dx = x.x - s.position.x;
            float dy = x.y - s.position.y;
            float dz = x.z - s.position.z;
            float r2 = dx * dx + dy * dy + dz * dz;

            if (r2 > cutoff2)
                continue;

            float r = (float) Math.sqrt(r2);
            sum += s.value * RBFKernel.phi(r, sigma);
        }
        return sum;
    }

}
