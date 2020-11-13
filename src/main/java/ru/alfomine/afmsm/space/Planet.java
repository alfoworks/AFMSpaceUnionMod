package ru.alfomine.afmsm.space;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class Planet {

	public ResourceLocation iconResource;
	public String name;
	public String warpId;
	public PlanetDifficulty difficulty;
	public int size;
	
	public Planet(ResourceLocation iconResource, String name, String warpId, PlanetDifficulty difficulty, int size) {
		this.iconResource = iconResource;
		this.name = name;
		this.warpId = warpId;
		this.difficulty = difficulty;
		this.size = size;
	}

	public Planet(NBTTagCompound compound) {
		readFromNBT(compound);
	}

	public NBTBase writeToNBT(NBTTagCompound compound) {
		compound.setString("iconResource", iconResource.toString());
		compound.setString("name", name);
		compound.setString("warpId", warpId);
		compound.setInteger("difficulty", difficulty.ordinal());
		compound.setInteger("size", size);

		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		iconResource = new ResourceLocation(compound.getString("iconResource"));
		name = compound.getString("name");
		warpId = compound.getString("warpId");
		difficulty = PlanetDifficulty.values()[compound.getInteger("difficulty")];
		size = compound.getInteger("size");
	}
}
