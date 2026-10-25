package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class SharedPainDo1Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, double amount) {
		execute(null, world, x, y, z, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, double amount) {
		if (entity == null || sourceentity == null)
			return;
		double t = 0;
		double damage_rest = 0;
		damage_rest = amount - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
		if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:shared_pain")))) != 0) {
			if (amount >= (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entity.isAlive() && entityiterator instanceof LivingEntity && !(entityiterator == entity || entityiterator == sourceentity)) {
							entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), (float) damage_rest);
							for (int _i1 = 0; _i1 < 20; _i1++) {
								if (world instanceof ServerLevel _level)
									_level.sendParticles(ParticleTypes.CRIT, (x + (entityiterator.getX() - x) * (t / 20)), (y + (entityiterator.getY() - y) * (t / 20) + (entityiterator.getBbHeight() + entity.getBbHeight()) / 2d),
											(z + (entityiterator.getZ() - z) * (t / 20)), 1, 0, 0, 0, 0);
								damage_rest = damage_rest - (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
								t = t + 1;
							}
							if (damage_rest <= 0) {
								break;
							}
						}
					}
				}
			}
		}
	}
}