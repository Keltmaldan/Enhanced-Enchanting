package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class FrozenAuraDoProcedure {
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
		double level_value = 0;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:frozen_aura")))) != 0) {
			level_value = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)
					.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:frozen_aura"))));
			sx = 0 - level_value;
			for (int _i1 = 0; _i1 < (int) (2 * level_value); _i1++) {
				sy = 0 - level_value;
				for (int _i2 = 0; _i2 < (int) (2 * level_value); _i2++) {
					sz = 0 - level_value;
					for (int _i3 = 0; _i3 < (int) (2 * level_value); _i3++) {
						if (1 == Mth.nextInt(RandomSource.create(), 1, 32)) {
							world.addParticle(ParticleTypes.SNOWFLAKE, (x + sx), (y + sy), (z + sz), 0, 0, 0);
						}
						sz = sz + 1;
					}
					sy = sy + 1;
				}
				sx = sx + 1;
			}
			for (Entity entityiterator : world.getEntities(entity, new AABB((x - level_value), (y - level_value), (z - level_value), (x + level_value), (y + level_value), (z + level_value)))) {
				entityiterator.setTicksFrozen((int) ((entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).cold_focus_power + 1) * 3 + entityiterator.getTicksFrozen()));
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entityiterator.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.cold_damages_upgrade = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).cold_focus_power;
					_vars.markSyncDirty();
				}
			}
		}
	}
}