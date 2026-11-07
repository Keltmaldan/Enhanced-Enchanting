package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModParticleTypes;

import java.util.Comparator;

public class GravityShot3Procedure {
	public static void execute(LevelAccessor world, double x, double y, double z, String inboundString) {
		if (inboundString == null)
			return;
		Entity arrow = null;
		double T = 0;
		double level = 0;
		double level_2 = 0;
		if (inboundString.contains("*")) {
			for (int _i1 = 0; _i1 < 4; _i1++) {
				level_2 = level_2 + 1;
				if (inboundString.startsWith(("" + level_2).replace(".0", ""))) {
					break;
				}
			}
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.enderman.teleport")), SoundSource.NEUTRAL, 1, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.enderman.teleport")), SoundSource.NEUTRAL, 1, 1, false);
			}
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if ((entityiterator.getStringUUID()).equals(inboundString.replace((level_2 + "*").replace(".0", ""), ""))) {
					arrow = entityiterator;
				}
			}
		}
		if (arrow == null) {
			return;
		}
		level = inboundString.contains("*") ? level_2 : arrow.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).gravity_radius;
		for (int _i1 = 0; _i1 < 628; _i1++) {
			T = T + 1;
			world.addParticle((SimpleParticleType) (EnhancedEnchantingModParticleTypes.GRAVITY_PULSE_PARTICLE.get()), (arrow.getX() + 1 * level * Math.cos(T)), (arrow.getY() + 1), (arrow.getZ() + 1 * level * Math.sin(T)),
					((0 - 1 * level * Math.cos(T)) / 3), 0, ((0 - 1 * level * Math.sin(T)) / 3));
		}
	}
}