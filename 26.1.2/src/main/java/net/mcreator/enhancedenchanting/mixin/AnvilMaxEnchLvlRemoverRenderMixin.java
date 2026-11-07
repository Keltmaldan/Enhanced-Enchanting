package net.mcreator.enhancedenchanting.mixin;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(AnvilScreen.class)
public abstract class AnvilMaxEnchLvlRemoverRenderMixin {
    @ModifyConstant(
        method = "extractLabels",
        constant = @Constant(intValue = 100),
        require = 0
    )
    private int enhancedEnchanting$hideTooExpensiveColor(int threshold) {
        return Integer.MAX_VALUE;
    }
}