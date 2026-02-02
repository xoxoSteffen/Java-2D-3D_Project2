package legacy;

//import reconstruction.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

import geometry.TriangleMesh;

public class OFFLoader {

	public static TriangleMesh loadMesh(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));

		String line;
		line = br.readLine();
		Scanner sc = new Scanner(line);

		// For now ignore comments '#', TODO later
		String tmp = sc.next();
		if (!tmp.equalsIgnoreCase("OFF")) {
			sc.close();
			br.close();
			System.out.println("Incorrect OFF file format");
			throw new Exception("Incorrect OFF file format");
		}
		sc.close();

		line = br.readLine();
		sc = new Scanner(line);
		int nv = sc.nextInt();
		int nf = sc.nextInt();
		sc.nextInt(); // can be zero so ignore it
		sc.close();

		TriangleMesh tmesh = new TriangleMesh(nv, nf);

		float x, y, z;

		for (int i = 0; i < tmesh.numberVertices; i++) {
			line = br.readLine();
			sc = new Scanner(line);

			x = sc.nextFloat();
			y = sc.nextFloat();
			z = sc.nextFloat();

			if (sc.hasNext()) {
				// TODO later handle color or normal information
			}

			tmesh.setVertex(i, x, y, z);

			sc.close();
		}

		// int fi, fj, fk;
		// for (int i = 0; i < tmesh.numberFaces; i++) {
		// line = br.readLine();
		// sc = new Scanner(line);

		// sc.nextInt(); // dummy we assume triangle meshes
		// fi = sc.nextInt();
		// fj = sc.nextInt();
		// fk = sc.nextInt();

		// tmesh.setFace(i, fi, fj, fk);
		// sc.close();
		// }

		br.close();

		System.out.println("Read mesh: " + fileName);
		//System.out.println("made of " + tmesh.numberVertices + " vertices and " + tmesh.numberFaces + " faces.");

		return tmesh;
	}
}
