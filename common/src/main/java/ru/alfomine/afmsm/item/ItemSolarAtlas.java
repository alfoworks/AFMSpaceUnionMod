package ru.alfomine.afmsm.item;

import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ru.alfomine.afmsm.client.gui.GuiSolarAtlas;

import java.lang.reflect.Field;

public class ItemSolarAtlas extends Item {
	
	public ItemSolarAtlas(String unlocalized) {
		setTranslationKey(unlocalized);
		setRegistryName(unlocalized);
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
	}
	
	@SuppressWarnings("NullableProblems")
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			try {
				Field field = CelestialObjectManager.class.getDeclaredField("CLIENT");
				field.setAccessible(true);
				Minecraft.getMinecraft().displayGuiScreen(new GuiSolarAtlas(((CelestialObjectManager) field.get(null)).celestialObjects));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
