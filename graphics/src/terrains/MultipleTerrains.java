package terrains;

import entities.Entity;

public class MultipleTerrains {
	
	private final int TERRAIN_SIZE = 800;
	
	private Terrain[] terrains = new Terrain[4];
	
	public MultipleTerrains(Terrain t1, Terrain t2, Terrain t3, Terrain t4) {
		this.terrains[0] = t1;
		this.terrains[1] = t2;
		this.terrains[2] = t3;
		this.terrains[3] = t4;
	}
	
	public Terrain determainTerrain(Entity entity) {
		float x = entity.getPosition().x;
		float z = entity.getPosition().z;
		if (x<=800 && x>=0 && z<=0 && z>=-800) {
			return this.terrains[0];
		}
		if (x>=-800 && x<=0 && z<=0 && z>=-800) {
			return this.terrains[1];
		}
		if (x>=-800 && x<=0 && z>=0 && z<=800) {
			return this.terrains[2];
		}
		return this.terrains[3];
	}

}
