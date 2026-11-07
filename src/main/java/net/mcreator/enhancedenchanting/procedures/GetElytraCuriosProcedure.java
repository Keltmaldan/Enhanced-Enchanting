package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.lang.reflect.Method;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import net.mcreator.enhancedenchanting.procedures.GetElytraProcedure;

public class GetElytraCuriosProcedure {
	public static ItemStack execute(Entity entity) {
		if (entity == null)
			return ItemStack.EMPTY;
		LivingEntity living = entity instanceof LivingEntity ? (LivingEntity) entity : null;
			return CuriosApi.getCuriosInventory(living).flatMap(inv -> inv.findFirstCurio(stack -> GetElytraProcedure.isElytra(stack, living))).map(SlotResult::stack)
			 .orElse(living.getItemBySlot(EquipmentSlot.CHEST));
	}
}