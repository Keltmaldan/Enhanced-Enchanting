package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PoisonFocusDo1Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getSource(), event.getEntity(), event.getAmount());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity, double amount) {
		execute(null, damagesource, entity, amount);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, double amount) {
		if (damagesource == null || entity == null)
			return;
		Entity source = null;
		if (damagesource.getEntity() != null) {
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.poisonous_damages_upgrade = (damagesource.getEntity()).getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).poison_focus_do;
				_vars.markSyncDirty();
			}
		}
		if (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).poisonous_damages_upgrade != 0) {
			if (!(damagesource.is(DamageTypes.ON_FIRE) || damagesource.is(DamageTypes.IN_FIRE)) && 1 == amount && damagesource.getEntity() == null && entity instanceof LivingEntity _livEnt5 && _livEnt5.hasEffect(MobEffects.POISON)) {
				if (event instanceof LivingIncomingDamageEvent _event) {
					_event.setAmount((float) (amount * (1 + entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).poisonous_damages_upgrade)));
				}
			}
		}
	}
}