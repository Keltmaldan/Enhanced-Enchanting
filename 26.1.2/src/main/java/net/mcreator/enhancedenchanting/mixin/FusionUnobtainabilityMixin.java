package net.mcreator.enhancedenchanting.mixin;

import net.mcreator.enhancedenchanting.EnchantmentFusion;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(EnchantmentHelper.class)
public abstract class FusionUnobtainabilityMixin {
    @Inject(method = "selectEnchantment", at = @At("RETURN"), cancellable = true)
    private static void enhancedEnchanting$filterFusionResults(CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        Set<String> fusionResults = new HashSet<>();
        for (EnchantmentFusion fusion : EnchantmentFusion.fusions.values()) {
            fusionResults.add(fusion.result());
        }
        List<EnchantmentInstance> filtered = new ArrayList<>(cir.getReturnValue());
        filtered.removeIf(e -> e.enchantment().unwrapKey()
            .map(key -> fusionResults.contains(key.identifier().toString())).orElse(false));
        cir.setReturnValue(filtered);
    }
}