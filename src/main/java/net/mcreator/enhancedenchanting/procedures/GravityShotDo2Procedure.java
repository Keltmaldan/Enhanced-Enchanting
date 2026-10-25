package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.GravityShotPacketMessage;
import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class GravityShotDo2Procedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getDirectEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity) {
		execute(null, world, x, y, z, entity, immediatesourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity) {
		if (entity == null || immediatesourceentity == null)
			return;
		Vec3 vector = Vec3.ZERO;
		if (immediatesourceentity instanceof Arrow) {
			if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_radius != 0) {
				{
					final Vec3 _center = new Vec3(x, y, z);
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_radius / 2d), e -> true).stream()
							.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						vector = (entity.position()).subtract((entityiterator.position()));
						if (immediatesourceentity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_radius >= vector.length()) {
							entityiterator.push((vector.x()), (vector.y()), (vector.z()));
						}
					}
				}
				if (!world.isClientSide())
					PacketDistributor.sendToAllPlayers(new GravityShotPacketMessage(("" + immediatesourceentity.getStringUUID())));
			}
		}
	}
}