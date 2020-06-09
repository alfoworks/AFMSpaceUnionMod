package ru.alfomine.afmsm.client;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class PlanetData {
	
	public ResourceLocation iconResource;
	public String name;
	public Difficulty difficulty;
	public int sizeX;
	public int sizeZ;
	
	public PlanetData(String warpId, int difficulty) {
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
	}
	
	public enum Difficulty {
		EZ("Легко", new Color(50, 205, 50), new Color(40, 164, 40), new Color(30, 123, 30)),
		NORM("Нормально", new Color(255, 255, 0), new Color(204, 204, 0), new Color(153, 153, 0)),
		AOH("Сложно", new Color(230, 0, 0), new Color(204, 0, 0), new Color(153, 0, 0));
		
		public String name;
		public Color color;
		public Color colorHovered;
		public Color colorPressed;
		
		Difficulty(String name, Color color, Color colorHovered, Color colorPressed) {
			this.name = name;
			this.color = color;
			this.colorHovered = colorHovered;
			this.colorPressed = colorPressed;
		}
	}
}
