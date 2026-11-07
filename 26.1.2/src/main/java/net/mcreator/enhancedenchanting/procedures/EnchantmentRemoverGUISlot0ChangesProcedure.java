package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.Entity;

public class EnchantmentRemoverGUISlot0ChangesProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		EnchantmentRemoverSlotClearProcedure.execute(entity);
		EnchantmentRemoverBooksGeneratorProcedure.execute(entity);
	}
}