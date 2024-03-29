package backend;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Imports .obj files and creates meshes from the data
 */
public final class ObjImporter {
	// Disable default constructor
	private ObjImporter() {

	}

	/**
	 * Creates a mesh using a scanner pointing to .obj data
	 * @param url path to the .obj file
	 * @return The created mesh
	 */
	public static Mesh importObj(URL url) throws IOException {
		Scanner s = new Scanner(url.openStream());
		String name = "UntitledMesh";

		List<Triangle> tris = new ArrayList<>();
		List<Vector> pts = new ArrayList<>();
		List<Vector> uvPts = new ArrayList<>();

		while (s.hasNext()) {
			String line = s.nextLine();
			if (line.length() == 0)
				continue;
			switch (line.substring(0, 2)) {
				case "o " ->  name = line.substring(2);
				case "v " -> {
					String[] ptStr = line.substring(2).split(" ");
					double x = Double.parseDouble(ptStr[0]);
					double y = Double.parseDouble(ptStr[1]);
					double z = Double.parseDouble(ptStr[2]);
					pts.add(new Vector(x, y, z));
				}
				case "vt" -> {
					String[] ptStr = line.substring(3).split(" ");
					double x = Double.parseDouble(ptStr[0]);
					double y = 1.0 - Double.parseDouble(ptStr[1]);
					uvPts.add(new Vector(x, y, 0));
				}
				case "f " -> {
					List<Vector> triPts = new ArrayList<>(3);
					List<Vector> triUvPts = new ArrayList<>(3);

					for (String pt : line.substring(2).split(" ")) {
						triPts.add(pts.get(Integer.parseInt(pt.substring(0, pt.indexOf('/'))) - 1));
						triUvPts.add(uvPts.get(Integer.parseInt(pt.substring(pt.indexOf('/') + 1)) - 1));
					}

					tris.add(new Triangle(triPts, triUvPts));
				}
			}
		}
		Mesh m = new Mesh(tris);
		m.setName(name);
		return m;
	}
}
