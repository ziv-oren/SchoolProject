package textures;

public class TerrainTexturePack {
	
	private TerrainTexture darkTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexture getDarkTexture() {
		return darkTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}

	public TerrainTexturePack(TerrainTexture darkTexture, TerrainTexture rTexture, TerrainTexture gTexture,TerrainTexture bTexture) {
		this.darkTexture = darkTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	

}
