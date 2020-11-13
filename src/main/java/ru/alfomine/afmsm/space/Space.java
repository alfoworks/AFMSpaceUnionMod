package ru.alfomine.afmsm.space;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class Space {
    public List<Planet> planets;
    public Planet mainPlanet;
    public int spaceSize;

    public Space(List<Planet> planets, Planet mainPlanet, int spaceSize) {
        this.planets = planets;
        this.mainPlanet = mainPlanet;
        this.spaceSize = spaceSize;
    }

    public Space(NBTTagCompound compound) {
        readFromNBT(compound);
    }

    public NBTBase writeToNBT(NBTTagCompound compound) {
        NBTTagList planetTags = new NBTTagList();

        for (Planet planet : planets) {
            planetTags.appendTag(planet.writeToNBT(new NBTTagCompound()));
        }

        compound.setTag("planets", planetTags);
        compound.setTag("mainPlanet", mainPlanet.writeToNBT(new NBTTagCompound()));
        compound.setInteger("spaceSize", spaceSize);

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        planets = new ArrayList<>();
        NBTTagList nbtPlanets = compound.getTagList("planets", Constants.NBT.TAG_COMPOUND);
        int count = nbtPlanets.tagCount();

        for (int i = 0; i < count; i++) {
            planets.add(new Planet(nbtPlanets.getCompoundTagAt(i)));
        }

        mainPlanet = new Planet(compound.getCompoundTag("mainPlanet"));
        spaceSize = compound.getInteger("mainPlanet");
    }
}
