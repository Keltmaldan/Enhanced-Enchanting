package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class SeekingDo2Procedure {
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
		Entity target = null;
		double d = 0;
		double factor = 0;
		Vec3 vect = Vec3.ZERO;
		if (entity instanceof AbstractArrow) {
			if (!(entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).seeking).equals("\u00E7^null^\u00E7")) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator instanceof LivingEntity && !(entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).seeking).equals(entityiterator.getStringUUID())) {
							d = (entity.position()).distanceTo((entityiterator.position()));
							if (d > 1) {
								factor = (4 - d) / 4;
								vect = new Vec3((factor * (entityiterator.getX() - x)), (factor * ((entityiterator.getY() + 0.3) - y)), (factor * (entityiterator.getZ() - z)));
								vect = ((entity.getDeltaMovement()).scale(0.25)).add((vect.scale(0.75)));
								entity.setDeltaMovement(new Vec3((vect.x()), (vect.y()), (vect.z())));
							}
							break;
						}
					}
				}
			}
		}
	}
}