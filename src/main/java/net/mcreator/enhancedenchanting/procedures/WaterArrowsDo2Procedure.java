package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class WaterArrowsDo2Procedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("enhanced:arrows")))) {
			if (Blocks.WATER == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
				entity.push((entity.getDeltaMovement().x() * (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).water_arrows_level / 8)),
						(entity.getDeltaMovement().y() * (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).water_arrows_level / 8)),
						(entity.getDeltaMovement().z() * (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).water_arrows_level / 8)));
			}
		}
	}
}