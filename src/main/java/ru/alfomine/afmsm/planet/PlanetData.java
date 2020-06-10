package ru.alfomine.afmsm.planet;

import net.minecraft.util.ResourceLocation;

public class PlanetData {
	
	public ResourceLocation iconResource;
	public String name;
	public PlanetDifficulty difficulty;
	public int size;
	
	public PlanetData(ResourceLocation iconResource, String name, PlanetDifficulty difficulty, int size) {
		this.iconResource = iconResource;
		this.name = name;
		this.difficulty = difficulty;
		this.size = size;
		
		/*
		CelestialObject cel = CelestialObjectManager.get(true, warpId);
		
		this.difficulty = Difficulty.values()[difficulty];
		
		if (cel == null) {
			iconResource = new ResourceLocation("minecraft", "stone");
			name = "UNDEFINED";
			sizeX = 0;
			sizeZ = 0;
			
			return;
		}
		
		name = cel.getDisplayName();
		iconResource = cel.setRenderData.iterator().next().resourceLocation;
		sizeX = cel.borderRadiusX;
		sizeZ = cel.borderRadiusZ;
		 */
	}
}
