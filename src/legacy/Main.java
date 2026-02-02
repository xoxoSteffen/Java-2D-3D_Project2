package legacy;

public class Main {
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java Main meshFileName vsFileName fsFileName");
			System.out.println("where: ");
			System.out.println("meshFileName: input triangle mesh file name (OFF file format)");
			System.out.println("vsFileName: input vertex shader file name");
			System.out.println("fsFileName: input fragment shader file name");
			System.exit(1);
		}
		
		try {
			// args[0]: mesh file name
			// args[1]: vertex shader file name
			// args[2]: fragment shader file name
			Application app = new Application("Mesh Viewer", 640, 480, args[0], args[1], args[2]);
			app.mainLoop();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		} 
	}
}
