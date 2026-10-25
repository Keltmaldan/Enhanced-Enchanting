package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class EnchantmentRemoverGUISlot0ChangesProcedure {
	public static void execute(LevelAccessor world, Entity entity, double slot) {
		if (entity == null)
			return;
		EnchantmentRemoverSlotClearProcedure.execute(entity);
		EnchantmentRemoverBooksGeneratorProcedure.execute(world, entity, slot);
	}
}