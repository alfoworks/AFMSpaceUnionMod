package ru.alfomine.afmsm.space;

import cr0s.warpdrive.data.CelestialObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.LinkedHashSet;

public class Planet {

	public ResourceLocation iconResource;
	public String name;
	public String warpId;
	public PlanetDifficulty difficulty;
	public int size;
	public LinkedHashSet<PlanetRenderData> renderDatas;
	
	public Planet(LinkedHashSet<CelestialObject.RenderData> renderDatas, String name, String warpId, PlanetDifficulty difficulty, int size) {
		this.renderDatas = new LinkedHashSet<>();

		for (CelestialObject.RenderData warpRenderData : renderDatas) {
			this.renderDatas.add(new PlanetRenderData(warpRenderData));
		}

		for (PlanetRenderData renderData : this.renderDatas) {
			if (renderData.resourceLocation != null) {
				this.iconResource = renderData.resourceLocation;
				break;
			}
		}

		if (iconResource == null) {
			this.iconResource = new ResourceLocation("anus");
		}

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

		NBTTagList nbtTagListRenderData = new NBTTagList();

		for (PlanetRenderData renderData : renderDatas) {
			nbtTagListRenderData.appendTag(renderData.writeToNBT(new NBTTagCompound()));
		}

		compound.setTag("renderData", nbtTagListRenderData);

		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		iconResource = new ResourceLocation(compound.getString("iconResource"));
		name = compound.getString("name");
		warpId = compound.getString("warpId");
		difficulty = PlanetDifficulty.values()[compound.getInteger("difficulty")];
		size = compound.getInteger("size");

		NBTTagList tagListRenderData = compound.getTagList("renderData", Constants.NBT.TAG_COMPOUND);

		int countRender = tagListRenderData.tagCount();
		renderDatas = new LinkedHashSet<>(countRender);

		for (int indexRenderData = 0; indexRenderData < countRender; indexRenderData++) {
			NBTTagCompound tagCompoundRenderData = tagListRenderData.getCompoundTagAt(indexRenderData);
			renderDatas.add(new PlanetRenderData(tagCompoundRenderData));
		}
	}
}
