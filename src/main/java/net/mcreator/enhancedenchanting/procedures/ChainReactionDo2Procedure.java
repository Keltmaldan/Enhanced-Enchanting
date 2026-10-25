package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ChainReactionDo2Procedure {
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
		double t = 0;
		if (Mth.nextDouble(RandomSource.create(), 1, 5) == 1) {
			if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_reaction_number_of_arrows != 0) {
				for (int _i1 = 0; _i1 < (int) immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_reaction_number_of_arrows; _i1++) {
					if (world instanceof ServerLevel projectileLevel) {
						Projectile _entityToSpawn = initArrowProjectile(new Arrow(EntityType.ARROW, projectileLevel), sourceentity, (float) (amount / 4), false, false, false, AbstractArrow.Pickup.DISALLOWED);
						_entityToSpawn.setPos(x, (y + entity.getBbHeight() / 2d), z);
						_entityToSpawn.shoot(Math.cos(t), 0, Math.sin(t), 1, 0);
						projectileLevel.addFreshEntity(_entityToSpawn);
					}
					t = t + 6.283185 / immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_reaction_number_of_arrows;
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