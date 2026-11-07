package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

@Mixin(Component.class)
public interface LevelWriterMixin {

    @Inject(
        method = "translatable",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void change(String original, CallbackInfoReturnable<MutableComponent> cir) {
    	if (original.startsWith("enchantment.level.")) {
    		final String newStr = original.replace("enchantment.level.", "");
    		final int lvl = Integer.parseInt(newStr);
    		if (lvl > 74 && lvl != 100)
    			cir.setReturnValue(MutableComponent.create(new TranslatableContents("§4" + newStr, null, TranslatableContents.NO_ARGS)));
    	}
    }
}