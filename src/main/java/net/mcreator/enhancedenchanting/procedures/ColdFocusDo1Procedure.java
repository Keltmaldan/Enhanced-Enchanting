package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ColdFocusDo1Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getSource(), event.getEntity(), event.getSource().getDirectEntity(), event.getAmount());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity, Entity immediatesourceentity, double amount) {
		execute(null, damagesource, entity, immediatesourceentity, amount);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, Entity immediatesourceentity, double amount) {
		if (damagesource == null || entity == null || immediatesourceentity == null)
			return;
		Entity source = null;
		if (damagesource.getEntity() != null) {
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.cold_damages_upgrade = (damagesource.getEntity()).getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).cold_focus_power;
				_vars.markSyncDirty();
			}
			if (immediatesourceentity instanceof Snowball) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (damagesource.getEntity()).getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).cold_focus_power);
				}
			}
		}
		if (damagesource.is(DamageTypes.FREEZE)) {
			if (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).cold_damages_upgrade != 0) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount * (1 + entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).cold_damages_upgrade)));
				}
			}
		}
	}
}