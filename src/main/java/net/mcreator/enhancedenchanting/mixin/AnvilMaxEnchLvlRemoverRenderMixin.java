package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

@Mixin(value = AnvilScreen.class, priority = 1)
public class AnvilMaxEnchLvlRemoverRenderMixin {
	@ModifyConstant(
		method = "renderLabels",
		constant = @Constant(intValue = 40),
		require = 0 //prevent conflicts
	)
	private int change(int value) {
		return Integer.MAX_VALUE;
	}
}