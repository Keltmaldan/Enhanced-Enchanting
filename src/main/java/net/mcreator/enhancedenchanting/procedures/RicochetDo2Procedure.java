package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class RicochetDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
		execute(null, world, x, y, z, entity, immediatesourceentity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
		if (entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		if (immediatesourceentity instanceof Arrow && immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).ricochet) {
			if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).ricochet_level >= Mth.nextInt(RandomSource.create(), 1, 5)) {
				{
					final Vec3 _center = new Vec3((immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()));
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator instanceof LivingEntity) {
							if ((entityiterator.getStringUUID()).equals(entity.getStringUUID()) || (entityiterator.getStringUUID()).equals(sourceentity.getStringUUID())
									|| (entityiterator.getStringUUID()).equals(immediatesourceentity.getStringUUID())) {
								continue;
							}
							if (world instanceof ServerLevel projectileLevel) {
								Projectile _entityToSpawn = initArrowProjectile(new Arrow(EntityType.ARROW, projectileLevel), sourceentity, (float) (amount / 2), false, false, false, AbstractArrow.Pickup.DISALLOWED);
								_entityToSpawn.setPos(x, (y + entity.getBbHeight()), z);
								_entityToSpawn.shoot((entityiterator.getX() - x), ((entityiterator.getY() + entityiterator.getBbHeight()) - (y + entity.getBbHeight())), (entityiterator.getZ() - z),
										(float) (immediatesourceentity instanceof Projectile _projEnt ? _projEnt.getDeltaMovement().length() : 0), 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
							break;
						}
					}
				}
			}
		}
	}

	private static AbstractArrow initArrowProjectile(AbstractArrow entityToSpawn, Entity shooter, float damage, boolean silent, boolean fire, boolean particles, AbstractArrow.Pickup pickup) {
		entityToSpawn.setOwner(shooter);
		entityToSpawn.setBaseDamage(damage);
		if (silent)
			entityToSpawn.setSilent(true);
		if (fire)
			entityToSpawn.igniteForSeconds(100);
		if (particles)
			entityToSpawn.setCritArrow(true);
		entityToSpawn.pickup = pickup;
		return entityToSpawn;
	}
}