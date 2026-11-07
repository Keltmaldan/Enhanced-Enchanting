package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class BackstabberCanSeeLogicProcedure {
	public static boolean execute(Entity entity, Entity watcher, double angle) {
		if (entity == null || watcher == null)
			return false;
		Vec3 watcherToEntity = Vec3.ZERO;
		watcherToEntity = (((entity.position()).subtract((watcher.position()))).add((new Vec3(0, entity.getEyeHeight() - watcher.getEyeHeight(), 0)))).normalize();
		return ((watcher.getLookAngle()).normalize()).distanceTo(watcherToEntity) <= (((watcher.getLookAngle()).normalize()).scale((-1))).distanceTo(watcherToEntity) * (angle / 180);
	}
}