package ru.alfomine.afmsm;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.util.ResourceLocation;

public class MenuPlanet {
	public ResourceLocation iconResource;
	public String name;
	
	public MenuPlanet(String warpId) {
		CelestialObject cel = CelestialObjectManager.get(true, warpId);
		
		if (cel == null) {
			iconResource = new ResourceLocation("minecraft", "stone");
			name = "UNDEFINED";
			
			return;
		}
		
		name = cel.getDisplayName();
		iconResource = cel.setRenderData.iterator().next().resourceLocation;
	}
}
