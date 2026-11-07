package net.mcreator.enhancedenchanting.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Component.class)
public interface LevelWriterMixin {
    @Inject(
        method = "translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void enhancedEnchanting$levelColor(String key, CallbackInfoReturnable<MutableComponent> cir) {
        final String prefix = "enchantment.level.";
        if (!key.startsWith(prefix)) return;
        try {
            int level = Integer.parseInt(key.substring(prefix.length()));
            if (level > 74 && level != 100) {
                cir.setReturnValue(Component.literal(Integer.toString(level)).withStyle(ChatFormatting.DARK_RED));
            }
        } catch (NumberFormatException ignored) {
        }
    }
}