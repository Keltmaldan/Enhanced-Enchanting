package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModParticleTypes;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class SnowballTickProcedure {
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
		double x_snowball = 0;
		double z_snowball = 0;
		double lvl = 0;
		lvl = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:snow_ball"))));
		if (lvl != 0) {
			if (0 >= entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).snowball_cooldown) {
				x_snowball = x + Math.cos(entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).snowball_angle);
				z_snowball = z + Math.sin(entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).snowball_angle);
				world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.SNOW_BALL_PARTICLE.get()), x_snowball, (y + 1.65), z_snowball, 0, 0, 0);
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator instanceof LivingEntity && !(entityiterator == entity) && entityiterator.isAlive()) {
							if (world instanceof ServerLevel projectileLevel) {
								Projectile _entityToSpawn = initProjectileProperties(new Snowball(EntityType.SNOWBALL, projectileLevel), entity, Vec3.ZERO);
								_entityToSpawn.setPos(x_snowball, (y + 1.65), z_snowball);
								_entityToSpawn.shoot((entityiterator.getX() - x_snowball), (entityiterator.getY() - y), (entityiterator.getZ() - z_snowball), 1, 0);
								projectileLevel.addFreshEntity(_entityToSpawn);
							}
							{
								EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
								_vars.snowball_cooldown = 120 - 20 * lvl;
								_vars.markSyncDirty();
							}
							break;
						}
					}
				}
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.snowball_angle = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).snowball_angle + 0.0654;
					_vars.markSyncDirty();
				}
			} else {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.snowball_cooldown = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).snowball_cooldown - 1;
					_vars.markSyncDirty();
				}
			}
		}
	}

	private static Projectile initProjectileProperties(Projectile entityToSpawn, Entity shooter, Vec3 acceleration) {
		entityToSpawn.setOwner(shooter);
		if (!Vec3.ZERO.equals(acceleration)) {
			entityToSpawn.setDeltaMovement(acceleration);
			entityToSpawn.hasImpulse = true;
		}
		return entityToSpawn;
	}
}