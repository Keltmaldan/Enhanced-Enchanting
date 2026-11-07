package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import net.neoforged.fml.ModList;

public class GetElytraProcedure {

    public static ItemStack execute(Entity entity) {

        if (!(entity instanceof LivingEntity living)) {
            return ItemStack.EMPTY;
        }

        if (ModList.get().isLoaded("curios")) {
            return GetElytraCuriosProcedure.execute(living);
        }

        return living.getItemBySlot(EquipmentSlot.CHEST);
    }
    
    public static boolean isElytra(ItemStack stack,LivingEntity living) {
        return stack != null
            && !stack.isEmpty()
            && stack.has(DataComponents.GLIDER);
    }
}