package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import javax.annotation.Nullable;

import java.util.UUID;

@EventBusSubscriber
public class ExplisveChickenDo2Procedure {
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
		if (entity instanceof Player)
			return;
		if (!world.isClientSide()) {
			if ((entity.getPersistentData().getString("explosive_target")).equals("")) {
				return;
			}
			target = world instanceof ServerLevel _level3 ? getEntityFromUUID(_level3, (entity.getPersistentData().getString("explosive_target"))) : null;
			if (target != null) {
				if (entity instanceof Mob _entity)
					_entity.getNavigation().moveTo((target.getX()), (target.getY()), (target.getZ()), 1.1);
				if ((target.position()).distanceTo((entity.position())) <= 2) {
					EnhancedEnchantingMod.queueServerWork(20, () -> {
						if (entity != null) {
							if (world instanceof Level _level && !_level.isClientSide())
								_level.explode(null, x, y, z, (float) 1.5, Level.ExplosionInteraction.MOB);
							if (!entity.level().isClientSide())
								entity.discard();
						}
					});
				}
			} else {
				entity.getPersistentData().putString("explosive_target", "");
			}
		}
	}

	private static Entity getEntityFromUUID(ServerLevel level, String uuid) {
		try {
			return level.getEntity(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}