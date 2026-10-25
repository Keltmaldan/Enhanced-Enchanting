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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModParticleTypes;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.world.item.enchantment.Enchantment;

@EventBusSubscriber
public class EntityTickProcedure {
	private static int getLevel(ItemStack stack, RegistryLookup<Enchantment> lookup, String id) {
		if (stack == null || id == null || id.isEmpty() || lookup == null)
			return 0;
		return stack.getEnchantmentLevel(lookup.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(id))));
	}
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		LivingEntity entity = event.getEntity() instanceof LivingEntity liv ? liv : null;
		if (entity == null)
			return;
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		Level world = entity.level();
		ItemStack mainHand = entity.getMainHandItem();
		ItemStack[] armor = new ItemStack[4];
		boolean disableFlashes = EnhancedEnchantingModVariables.MapVariables.get(world).disable_flashes;
		{
			int i = 0;
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
					armor[i] = entity.getItemBySlot(slot);
					i++;
				}
			}
		}
		RegistryLookup<Enchantment> lookup = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

		int fireAura = getLevel(armor[0], lookup, "enhanced_enchanting:fire_aura");
		if (fireAura != 0) {
			double range = Math.pow(Math.sqrt(fireAura / 1.5), 1 + 2d / 3);
			for (double T = 0; T < 6.3; T += 0.1) {
				if (!(world.getBlockState(BlockPos.containing(x + range * Math.cos(T), y, z + range * Math.sin(T)))).is(BlockTags.create(ResourceLocation.parse("neoforge:liquid")))) {
					if ((world.getBlockState(BlockPos.containing(x + range * Math.cos(T), y  - 1, z + range * Math.sin(T)))).is(BlockTags.create(ResourceLocation.parse("neoforge:soul")))) {
						world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.SOUL_FIRE_AURA_PARTICLE.get()), (x + range * Math.cos(T)), (y + 0.1), (z + range * Math.sin(T)), 0, 0, 0);
					} else if ((world.getBlockState(BlockPos.containing(x + range * Math.cos(T), y - 1, z + range * Math.sin(T)))).is(BlockTags.create(ResourceLocation.parse("neoforge:copper")))) {
						world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.COPPER_FIRE_AURA_PARTICLE.get()), (x + range * Math.cos(T)), (y + 0.1), (z + range * Math.sin(T)), 0, 0, 0);
					} else {
						world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.FIRE_AURA_PARTICLE.get()), (x + range * Math.cos(T)), (y + 0.1), (z + range * Math.sin(T)), 0, 0, 0);
					}
				}
			}
			for (Entity entityiterator : world.getEntities(entity, new AABB((x - range), (y - 0.5), (z - range), (x + range), (y + 0.5), (z + range)))) {
				if (entityiterator instanceof LivingEntity && !(world.getBlockState(BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()))).is(BlockTags.create(ResourceLocation.parse("neoforge:liquid")))) {
					entityiterator.igniteForSeconds(3);
					{
						EnhancedEnchantingModVariables.PlayerVariables _vars = entityiterator.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
						_vars.fire_damages_upgrade = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).fire_focus_power;
						_vars.markSyncDirty();
					}
				}
			}
		}
		int fearless = getLevel(mainHand, lookup, "enhanced_enchanting:fearless");
		if (fearless != 0) {
			if (!world.isClientSide())
				entity.addEffect(new MobEffectInstance(EnhancedEnchantingModMobEffects.FEARLESS_EFFECT, 60, fearless));
		}

		int gravityPulse = getLevel(armor[0], lookup, "enhanced_enchanting:gravity_pulse");
		if (gravityPulse != 0) {
			if (1 <= entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_pulse_time) {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_pulse_time = 0;
					_vars.markSyncDirty();
				}
				double range = Math.sqrt(gravityPulse);
				for (double T = 0; T < 6.3; T += 0.1) {
					world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.GRAVITY_PULSE_PARTICLE.get()),
							(x + gravityPulse * Math.cos(T)),
							(y + 1),
							(z + gravityPulse * Math.sin(T)),
							(!disableFlashes
									? (0 - gravityPulse * Math.cos(T)) / 3
									: 0),
							0,
							(!disableFlashes
									? (0 - gravityPulse * Math.sin(T)) / 3
									: 0));
				}
				for (Entity entityiterator : world.getEntities(entity, new AABB((x - range), (y - range), (z - range), (x + range), (y + range), (z + range)))) {
					if (Math.sqrt(Math.pow(entityiterator.getZ() - z, 2) + Math.pow(entityiterator.getX() - x, 2)) <= gravityPulse) {
						if (entityiterator instanceof LivingEntity) {
							entityiterator.push(((x - entityiterator.getX()) / 2), ((y - entityiterator.getY()) / 2), ((z - entityiterator.getZ()) / 2));
						}
					}
				}
			} else {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_pulse_time = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_pulse_time + 0.05;
					_vars.markSyncDirty();
				}
			}
		}
		int backPushing = getLevel(armor[0], lookup, "enhanced_enchanting:back_pushing");
		if (backPushing != 0) {
			if (1 <= entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_pulse_time) {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_pulse_time = 0;
					_vars.markSyncDirty();
				}
				double range = Math.sqrt(backPushing);
				for (double T = 0; T < 6.3; T += 0.1) {
					world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.BACK_PUSHING_PARTICLE.get()), (x + 3 * (!disableFlashes ? (range * Math.cos(T)) / 3 : 0)), (y + 1),
							(z + 3 * (!disableFlashes ? (range * Math.sin(T)) / 3 : 0)), (!disableFlashes ? (range * Math.cos(T)) / 3 : 0),
							0, (!disableFlashes ? (range * Math.sin(T)) / 3 : 0));
				}
				for (Entity entityiterator : world.getEntities(entity, new AABB((x - range), (y - range), (z - range), (x + range), (y + range), (z + range)))) {
					if (Math.sqrt(Math.pow(entityiterator.getZ() - z, 2) + Math.pow(entityiterator.getX() - x, 2)) <= backPushing) {
						double theta = 0 + Math.acos((entityiterator.getX() - x) / Math.sqrt(Math.pow(entityiterator.getZ() - z, 2) + Math.pow(entityiterator.getX() - x, 2)));
						double x_near = x + backPushing * Math.cos(theta);
						double z_near = z + backPushing * Math.sin(theta);
						if (entityiterator instanceof LivingEntity) {
							world.addParticle(ParticleTypes.CRIT, x_near, y, z_near, 0, 0, 0);
							entityiterator.push(
									(x <= entityiterator.getX()
											? 3 * (entityiterator.getX() - x_near) * ((entityiterator.getX() - x_near) / Math.abs(entityiterator.getX() - x_near))
											: (-0.5) * (entityiterator.getX() - x_near) * ((entityiterator.getX() - x_near) / Math.abs(entityiterator.getX() - x_near))),
									0.15,
									(z <= entityiterator.getZ()
											? 3 * (entityiterator.getZ() - z_near) * ((entityiterator.getZ() - z_near) / Math.abs(entityiterator.getZ() - z_near))
											: (-0.5) * (entityiterator.getZ() - z_near) * ((entityiterator.getZ() - z_near) / Math.abs(entityiterator.getZ() - z_near))));
						}
					}
				}
			} else {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.gravity_pulse_time = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_pulse_time + 0.1;
					_vars.markSyncDirty();
				}
			}
		}
		if (getLevel(armor[3], lookup, "enhanced_enchanting:sticky") != 0) {
			if (world.getBlockState(BlockPos.containing(x, y + 0.25 + entity.getBbHeight(), z)).canOcclude()) {
				if (!((BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y + 0.25 + entity.getBbHeight(), z))).getBlock()).toString().toLowerCase()).contains("wall")
						|| (BuiltInRegistries.BLOCK.getKey((world.getBlockState(BlockPos.containing(x, y + 0.25 + entity.getBbHeight(), z))).getBlock()).toString().toLowerCase()).contains("fence"))) {
					if (!(entity.onGround() || entity.isShiftKeyDown())) {
						entity.push(0, 1, 0);
					}
				}
			}
		}
	}


}