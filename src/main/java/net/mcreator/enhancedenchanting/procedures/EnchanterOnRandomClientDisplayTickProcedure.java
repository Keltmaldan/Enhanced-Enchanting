package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;

public class EnchanterOnRandomClientDisplayTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		ItemStack item = ItemStack.EMPTY;
		double xt = 0;
		double yt = 0;
		double zt = 0;
		double h = 0;
		double time = 0;
		int horizontalRadiusSphere = (int) 3 - 1;
		int verticalRadiusSphere = (int) 3 - 1;
		int yIterationsSphere = verticalRadiusSphere;
		for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
			for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
				for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
					double distanceSq = (xi * xi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere) + (i * i) / (double) (verticalRadiusSphere * verticalRadiusSphere)
							+ (zi * zi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere);
					if (distanceSq <= 1.0) {
						world.addParticle(ParticleTypes.ENCHANT, x + xi, y + i, z + zi, (x - x + xi + 0.5), (y - y + i + 0.5), (z - z + zi + 0.5));
					}
				}
			}
		}
		while (time < 360) {
			time = time + 1;
			world.addParticle(ParticleTypes.TOTEM_OF_UNDYING, (x + Math.cos(time) + 0.5), (y + 0.01), (z + Math.sin(time) + 0.5), 0, 0, 0);
		}
	}
}