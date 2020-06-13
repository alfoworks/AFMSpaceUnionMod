package ru.alfomine.afmsm.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.allformine.afmuf.alert.AlertContext;
import ru.allformine.afmuf.alert.AlertMod;
import ru.allformine.afmuf.net.discord.Webhook;

public class ServerUtils {
    public static void giveItemTo(EntityPlayer player, ItemStack stack) {
        Webhook.sendSecureAlert(new AlertContext(player.getName(), "Decoy packet #1", null, null, AlertMod.AFMSM, "Использование пакета-приманки в AFMSM. Этот мод задекомпилили и решили, что тут есть пакетхак)0"));
    }
}
