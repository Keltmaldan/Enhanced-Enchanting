package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class WindArrowDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		execute(null, entity, immediatesourceentity, sourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		if (entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		Vec3 vector = Vec3.ZERO;
		if (immediatesourceentity instanceof Arrow) {
			if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).wind_arrow_level != 0) {
				if (!(sourceentity == entity)) {
					vector = (new Vec3(0, (Math.min(5, immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).wind_arrow_level) * 3), 0))
							.add((((sourceentity.position()).subtract((entity.position()))).scale((Math.min(5, immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).wind_arrow_level) / 8))));
					entity.push((vector.x()), (vector.y()), (vector.z()));
				}
			}
		}
	}
}