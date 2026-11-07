package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.particles.ParticleTypes;

public class EnchantmentsUpgraderOnRandomClientDisplayTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double t = 0;
		for (int _i1 = 0; _i1 < 628; _i1++) {
			world.addParticle(ParticleTypes.REVERSE_PORTAL, (x + Math.cos(10 * t) - Math.sin(t) + 0.5), (y + 0.05), (z + Math.sin(10 * t) - Math.cos(t) + 0.5), 0, 0, 0);
			t = t + 0.01;
		}
	}
}