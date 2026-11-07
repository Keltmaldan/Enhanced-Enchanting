package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GhostDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(Entity entity, Entity sourceentity, double amount) {
		execute(null, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity sourceentity, double amount) {
		if (entity == null || sourceentity == null)
			return;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(EnhancedEnchantingModMobEffects.GHOST_FORM)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(EnhancedEnchantingModMobEffects.GHOST_FORM);
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.INVISIBILITY);
			if (event instanceof ICancellableEvent _cancellable) {
				_cancellable.setCanceled(true);
			}
		}
		if (sourceentity instanceof LivingEntity _livEnt3 && _livEnt3.hasEffect(EnhancedEnchantingModMobEffects.GHOST_FORM)) {
			if (sourceentity instanceof LivingEntity _entity)
				_entity.removeEffect(EnhancedEnchantingModMobEffects.GHOST_FORM);
			if (event instanceof LivingIncomingDamageEvent _event) {
				_event.setAmount((float) (1.5 * amount));
			}
		}
	}
}