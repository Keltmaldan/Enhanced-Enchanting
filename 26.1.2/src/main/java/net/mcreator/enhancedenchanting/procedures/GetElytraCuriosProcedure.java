package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import net.neoforged.fml.ModList;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class GetElytraCuriosProcedure {

    public static ItemStack execute(Entity entity) {

        if (!(entity instanceof LivingEntity living)) {
            return ItemStack.EMPTY;
        }

        ItemStack chest = living.getItemBySlot(EquipmentSlot.CHEST);

        if (!ModList.get().isLoaded("curios")) {
            return chest;
        }

        return CuriosIntegration.find(living, chest);
    }

    private static final class CuriosIntegration {

        private static ItemStack find(LivingEntity living,ItemStack fallback) {
            return CuriosApi.getCuriosInventory(living).flatMap(inv -> inv.findFirstCurio(stack -> GetElytraProcedure.isElytra(stack, living))).map(SlotResult::stack).orElse(fallback);
        }
    }
}