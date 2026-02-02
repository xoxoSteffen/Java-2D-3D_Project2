package debug;

import reconstruction.PointCloud;
import reconstruction.XYZLoader;
public class TestXYZ {
    public static void main(String[] args) {
        try {
            PointCloud pc = XYZLoader.load("xyz/bunny.xyz");
            System.out.println("Points loaded: " + pc.size());

            if (pc.size() > 0) {
                System.out.println("First point: " + pc.points.get(0));
                System.out.println("First normal: " + pc.normals.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
