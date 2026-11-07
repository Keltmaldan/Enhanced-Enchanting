package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

import javax.annotation.Nullable;

@EventBusSubscriber
public class BlockCatalystDo3Procedure {
	@SubscribeEvent
	public static void onBlockBreaking(PlayerEvent.BreakSpeed event) {
		if (event.getPosition().isEmpty())
			return;
		execute(event, event.getEntity().level(), event.getEntity(), event.getNewSpeed());
	}

	public static void execute(LevelAccessor world, Entity entity, double breakSpeed) {
		execute(null, world, entity, breakSpeed);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, double breakSpeed) {
		if (entity == null)
			return;
		ItemStack item_ = ItemStack.EMPTY;
		item_ = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
		if (item_.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:block_catalyst")))) != 0) {
			if (event instanceof PlayerEvent.BreakSpeed _speed5)
				_speed5.setNewSpeed((float) (breakSpeed * (1 + item_.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("block_mined") * 0.005
						* item_.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:block_catalyst")))))));
		}
	}
}