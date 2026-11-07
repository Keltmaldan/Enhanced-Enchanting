package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HeadShooterDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().getY(), event.getSource().getDirectEntity(), event.getAmount());
		}
	}

	public static void execute(double y, Entity immediatesourceentity, double amount) {
		execute(null, y, immediatesourceentity, amount);
	}

	private static void execute(@Nullable Event event, double y, Entity immediatesourceentity, double amount) {
		if (immediatesourceentity == null)
			return;
		if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).head_shot_strenght != 0) {
			if (0.15 >= Math.min(Math.abs((immediatesourceentity.getY() + immediatesourceentity.getDeltaMovement().y()) - (y + ((LivingIncomingDamageEvent) event).getEntity().getEyeHeight())),
					Math.abs(immediatesourceentity.getY() - (y + ((LivingIncomingDamageEvent) event).getEntity().getEyeHeight())))) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount * (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).head_shot_strenght + 1)));
				}
			}
		}
	}
}