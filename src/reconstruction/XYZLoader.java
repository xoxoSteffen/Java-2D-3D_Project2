package reconstruction;
import java.io.BufferedReader;
import java.io.FileReader;

import org.joml.Vector3f;

public class XYZLoader {

    public static PointCloud load(String fileName) throws Exception {
        PointCloud pc = new PointCloud();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("#")) continue;

                String[] t = line.split("\\s+");
                if (t.length < 6) continue; // skip bad lines

                float x  = Float.parseFloat(t[0]);
                float y  = Float.parseFloat(t[1]);
                float z  = Float.parseFloat(t[2]);
                float nx = Float.parseFloat(t[3]);
                float ny = Float.parseFloat(t[4]);
                float nz = Float.parseFloat(t[5]);

                pc.add(new Vector3f(x, y, z), new Vector3f(nx, ny, nz));
            }
        }

        System.out.println("Read point cloud: " + fileName);
        System.out.println("made of " + pc.size() + " points.");
        return pc;
    }
}
