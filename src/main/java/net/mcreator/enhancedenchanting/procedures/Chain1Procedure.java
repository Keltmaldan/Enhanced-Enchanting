package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModParticleTypes;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class Chain1Procedure {
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
		Entity entity2 = null;
		double t = 0;
		double xt = 0;
		double yt = 0;
		double zt = 0;
		double numberOlinks = 0;
		if (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_cooldown > 0) {
			for (Entity entityiterator : world.getEntities(entity,
					new AABB((x - 5 * entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level), (y - 5 * entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level),
							(z - 5 * entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level), (x + 5 * entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level),
							(y + 5 * entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level), (z + 5 * entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level)))) {
				if ((entityiterator.getStringUUID()).equals(entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chained_by)) {
					{
						EnhancedEnchantingModVariables.PlayerVariables _vars = entityiterator.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
						_vars.chain_level = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_level;
						_vars.markSyncDirty();
					}
					numberOlinks = Math.ceil(4 * (entityiterator.position()).distanceTo((entity.position())));
					{
						EnhancedEnchantingModVariables.PlayerVariables _vars = entityiterator.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
						_vars.chained_by = entity.getStringUUID();
						_vars.markSyncDirty();
					}
					entity2 = entityiterator;
					if (numberOlinks > 40) {
						entity.push(((((entityiterator.position()).subtract((entity.position()))).scale(1.0 / 4)).x()), ((((entityiterator.position()).subtract((entity.position()))).scale(1.0 / 4)).y()),
								((((entityiterator.position()).subtract((entity.position()))).scale(1.0 / 4)).z()));
					}
				}
			}
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.chained = entity2 != null;
				_vars.markSyncDirty();
			}
			t = 0;
			for (int _i1 = 0; _i1 < (int) numberOlinks; _i1++) {
				xt = entity2.getX() + t * ((x - entity2.getX()) / numberOlinks);
				yt = entity2.getY() + entity2.getBbHeight() / 2d + t * (((y + entity.getBbHeight() / 2d) - (entity2.getY() + entity2.getBbHeight() / 2d)) / numberOlinks);
				zt = entity2.getZ() + t * ((z - entity2.getZ()) / numberOlinks);
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (EnhancedEnchantingModParticleTypes.CHAIN_PARTICLE.get()), xt, yt, zt, 1, 0, 0, 0, 0);
				{
					final Vec3 _center = new Vec3(xt, yt, zt);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						{
							EnhancedEnchantingModVariables.PlayerVariables _vars = entityiterator.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
							_vars.stuck_by_chain = true;
							_vars.markSyncDirty();
						}
					}
				}
				t = t + 1;
			}
		}
	}
}