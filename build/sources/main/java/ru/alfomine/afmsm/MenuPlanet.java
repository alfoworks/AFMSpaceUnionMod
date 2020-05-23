package ru.alfomine.afmsm;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

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
