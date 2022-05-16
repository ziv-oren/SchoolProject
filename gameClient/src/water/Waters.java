package water;

import java.util.ArrayList;
import java.util.List;

public class Waters {
	
	private static final int SIZE = 1200;
	
	public  List<WaterTile> waterTiles = new ArrayList<WaterTile>();
	
	public Waters(float height) {
		for(int x = -1730; x <= 1170; x+= 120) {
			for(int z = -1730; z <= 1170; z+= 120) {
				waterTiles.add(new WaterTile(x,z,height));
			}
		}
		
	}

}
