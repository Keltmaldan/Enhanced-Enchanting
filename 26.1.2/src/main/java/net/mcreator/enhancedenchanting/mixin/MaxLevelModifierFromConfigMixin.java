package net.mcreator.enhancedenchanting.mixin;

import net.mcreator.enhancedenchanting.EnchantmentModificationReader;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

@Mixin(Enchantment.class)
public abstract class MaxLevelModifierFromConfigMixin {
    // Shadow the record accessor rather than its private final record field.
    @Shadow public abstract Component description();

    private String enhancedEnchanting$enchantmentId() {
        if (!(description().getContents() instanceof TranslatableContents translation)) return "";
        String key = translation.getKey();
        if (!key.startsWith("enchantment.")) return "";
        String id = key.substring("enchantment.".length());
        int dot = id.indexOf('.');
        return dot < 0 ? "" : id.substring(0, dot) + ":" + id.substring(dot + 1);
    }

    private void enhancedEnchanting$override(CallbackInfoReturnable<Integer> cir, Map<String, Integer> overrides) {
        if (overrides == null || overrides.isEmpty()) return;
        Integer value = overrides.get(enhancedEnchanting$enchantmentId());
        if (value == null) value = overrides.get("all");
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void enhancedEnchanting$maxLevel(CallbackInfoReturnable<Integer> cir) {
        enhancedEnchanting$override(cir, EnchantmentModificationReader.maxLevel);
    }
    @Inject(method = "getWeight", at = @At("RETURN"), cancellable = true)
    private void enhancedEnchanting$weight(CallbackInfoReturnable<Integer> cir) {
        enhancedEnchanting$override(cir, EnchantmentModificationReader.weight);
    }
    @Inject(method = "getAnvilCost", at = @At("RETURN"), cancellable = true)
    private void enhancedEnchanting$anvilCost(CallbackInfoReturnable<Integer> cir) {
        enhancedEnchanting$override(cir, EnchantmentModificationReader.anvilCost);
    }
}