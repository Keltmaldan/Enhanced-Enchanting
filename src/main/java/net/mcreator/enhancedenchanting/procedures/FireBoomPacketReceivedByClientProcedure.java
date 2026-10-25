package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import java.util.UUID;

public class FireBoomPacketReceivedByClientProcedure {
	public static void execute(LevelAccessor world, String inboundString) {
		if (inboundString == null)
			return;
		Entity entity = null;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		entity = world instanceof ServerLevel _level0 ? getEntityFromUUID(_level0, inboundString) : null;
		if (entity != null) {
			sx = -5;
			for (int _i1 = 0; _i1 < 10; _i1++) {
				sy = -5;
				for (int _i2 = 0; _i2 < 10; _i2++) {
					sz = -5;
					for (int _i3 = 0; _i3 < 10; _i3++) {
						if (10 >= (new Vec3(sx, sy, sz)).length()) {
							world.addParticle(ParticleTypes.SMALL_FLAME, (entity.getX()), (entity.getY()), (entity.getZ()), sx, sy, sz);
						}
						sz = sz + 1;
					}
					sy = sy + 1;
				}
				sx = sx + 1;
			}
		}
	}

	private static Entity getEntityFromUUID(ServerLevel level, String uuid) {
		try {
			return level.getEntity(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}