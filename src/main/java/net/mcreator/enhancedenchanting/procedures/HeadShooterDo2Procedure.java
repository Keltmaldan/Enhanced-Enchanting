package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModParticleTypes;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HeadShooterDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getY(), event.getEntity(), event.getSource().getDirectEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double y, Entity entity, Entity immediatesourceentity, double amount) {
		execute(null, world, y, entity, immediatesourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double y, Entity entity, Entity immediatesourceentity, double amount) {
		if (entity == null || immediatesourceentity == null)
			return;
		if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).head_shot_strenght != 0) {
			if (0.15 >= Math.min(Math.abs((immediatesourceentity.getY() + immediatesourceentity.getDeltaMovement().y()) - (y + entity.getEyeHeight())), Math.abs(immediatesourceentity.getY() - (y + entity.getEyeHeight())))) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount * (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).head_shot_strenght + 1)));
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (EnhancedEnchantingModParticleTypes.HEAD_SHOT.get()), (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), 1, 0, 0, 0, 0);
			}
		}
	}
}