package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

public class LapisLazuliCauldronEntityFallsOnTheBlockinitProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (!(world instanceof ServerLevel) || !(entity instanceof ItemEntity itemEntity)) return;
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;
        String previous = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("lapis_enchant", "");
        if (!previous.isEmpty()) return;
        LapisLazuliCauldronEntityFallsOnTheBlockProcedure.execute(world, x, y, z, itemEntity);
    }
}