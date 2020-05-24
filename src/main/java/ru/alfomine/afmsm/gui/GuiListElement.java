package ru.alfomine.afmsm.gui;

import ru.alfomine.afmsm.MenuPlanet;

public class GuiListElement {
	
	int x;
	int y;
	MenuPlanet planet;
	boolean selected;
	
	GuiListElement(int x, int y, MenuPlanet planet) {
		this.x = x;
		this.y = y;
		this.planet = planet;
		this.selected = false;
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX < x + 188 && mouseY < y + 53;
	}
}
