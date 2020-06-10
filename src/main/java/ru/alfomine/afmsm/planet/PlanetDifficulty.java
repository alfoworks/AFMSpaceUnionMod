package ru.alfomine.afmsm.planet;

import java.awt.*;

public enum PlanetDifficulty {
	EZ("Легко", new Color(50, 205, 50), new Color(40, 164, 40), new Color(30, 123, 30)),
	NORM("Нормально", new Color(255, 255, 0), new Color(204, 204, 0), new Color(153, 153, 0)),
	AOH("Сложно", new Color(230, 0, 0), new Color(204, 0, 0), new Color(153, 0, 0));
	
	public String name;
	public Color color;
	public Color colorHovered;
	public Color colorPressed;
	
	PlanetDifficulty(String name, Color color, Color colorHovered, Color colorPressed) {
		this.name = name;
		this.color = color;
		this.colorHovered = colorHovered;
		this.colorPressed = colorPressed;
	}
}
