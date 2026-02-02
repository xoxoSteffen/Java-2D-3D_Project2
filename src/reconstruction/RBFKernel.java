package reconstruction;
public class RBFKernel {

    // Gaussian RBF
    public static float phi(float r, float sigma) {
        return (float) Math.exp(-(r * r) / (sigma * sigma));
    }
}
