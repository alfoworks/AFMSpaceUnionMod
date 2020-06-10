package ru.alfomine.afmsm.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ru.alfomine.afmsm.network.AFMSMPacketHandler;
import ru.alfomine.afmsm.network.message.MessagePlanetaryGui;

import java.util.ArrayList;

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
		if (worldIn.isRemote) {
			return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		}
		
		AFMSMPacketHandler.INSTANCE.sendTo(new MessagePlanetaryGui(new ArrayList<>(), 0, 0), (EntityPlayerMP) playerIn);
		
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
