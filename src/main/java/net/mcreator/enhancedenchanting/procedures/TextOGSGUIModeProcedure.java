package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class TextOGSGUIModeProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		return (entity instanceof Player _entity0 && _entity0.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0) && _menu0.getMenuState(1, "Mode", false) ? "Remove Legendary" : "Remove Curses";
	}
}