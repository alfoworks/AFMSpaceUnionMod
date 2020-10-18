package ru.alfomine.afmsm.world;

import cr0s.warpdrive.render.RenderBlank;
import cr0s.warpdrive.render.RenderSpaceSky;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.alfomine.afmsm.init.ModWorlds;

import java.util.List;

public class SpawnWorldProvider extends WorldProvider implements IGalacticraftWorldProvider {
    private final CelestialBody celestialBody = new CelestialBody("SpawnWorld") {
        @Override
        public int getID() {
            return ModWorlds.SPAWN_WORLD_DIM_TYPE.getId();
        }

        @Override
        public String getUnlocalizedNamePrefix() {
            return "_spawnworld";
        }
    };

    /* Важная хуита */

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        setSkyRenderer(RenderSpaceSky.getInstance());
        setCloudRenderer(RenderBlank.getInstance());

        return new Vec3d(1, 0, 0);
    }

    @Override
    protected void generateLightBrightnessTable() {
        for (int i = 0; i <= 15; ++i) {
            lightBrightnessTable[i] = 0xFF000000;
        }
    }

    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override
    public boolean isSkyColored() {
        return true;
    }

    @Override
    public boolean isDaytime() {
        return true;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3) {
        return 0F;
    }

    /* ========= */

    @Override
    protected void init() {
        super.init();
        this.biomeProvider = new BiomeProviderSingle(Biomes.VOID);
    }

    @Override
    public CelestialBody getCelestialBody() {
        return celestialBody;
    }

    @Override
    public boolean shouldDisablePrecipitation() {
        return false;
    }

    @Override
    public boolean shouldCorrodeArmor() {
        return false;
    }

    @Override
    public int getDungeonSpacing() {
        return 0;
    }

    @Override
    public ResourceLocation getDungeonChestType() {
        return RoomTreasure.MOONCHEST;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        return 1.0F;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new SpawnWorldChunkGenerator(world);
    }

    @Override
    public double getHorizon() {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel() {
        return 96;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2) {
        return true;
    }

    //Overriding so that beds do not explode on Asteroids
    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public float getGravity() {
        return 0.072F;
    }

    @Override
    public float getFallDamageModifier() {
        return 0F;
    }

    @Override
    public boolean hasNoAtmosphere() {
        return false;
    }

    @Override
    public float getSoundVolReductionAmount() {
        return 0;
    }

    @Override
    public boolean hasBreathableAtmosphere() {
        return false;
    }

    @Override
    public boolean netherPortalsOperational() {
        return false;
    }

    @Override
    public boolean isGasPresent(EnumAtmosphericGas enumAtmosphericGas) {
        return enumAtmosphericGas == EnumAtmosphericGas.OXYGEN;
    }

    @Override
    public float getThermalLevelModifier() {
        return 0;
    }

    @Override
    public float getWindLevel() {
        return 0;
    }

    @Override
    public float getSolarSize() {
        return 0;
    }

    @Override
    public int getActualHeight() {
        return 256;
    }

    @Override
    public DimensionType getDimensionType() {
        return ModWorlds.SPAWN_WORLD_DIM_TYPE;
    }

    @Override
    public float getArrowGravity() {
        return 0.002F;
    }

    @Override
    public double getMeteorFrequency() {
        return 0;
    }

    @Override
    public double getFuelUsageMultiplier() {
        return 0;
    }

    @Override
    public boolean canSpaceshipTierPass(int i) {
        return false;
    }

    @Override
    public List<Block> getSurfaceBlocks() {
        return null;
    }
}

