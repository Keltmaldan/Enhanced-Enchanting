package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GravityShotDo1Procedure {
	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		execute(event, event.getLevel(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Entity shooter = null;
		if (!(entity instanceof AbstractArrow))
			return;
		if (entity.level().isClientSide())
			return;
		shooter = ((Projectile) entity).getOwner();
		if (!(shooter instanceof LivingEntity))
			return;
		if ((shooter instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(Identifier.parse("minecraft:enchantable/distance")))) {
			if ((shooter instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
					.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:gravity_shot")))) != 0) {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_radius = (shooter instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:gravity_shot"))));
					_vars.markSyncDirty();
				}
			} else {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_radius = 0;
					_vars.markSyncDirty();
				}
			}
		} else if ((shooter instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).is(ItemTags.create(Identifier.parse("minecraft:enchantable/distance")))) {
			if ((shooter instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)
					.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:gravity_shot")))) != 0) {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_radius = (shooter instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:gravity_shot"))));
					_vars.markSyncDirty();
				}
			} else {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_radius = 0;
					_vars.markSyncDirty();
				}
			}
		}
	}
}