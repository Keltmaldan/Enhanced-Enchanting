package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.enhancedenchanting.EnchantmentFusion;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceKey;
import java.util.ArrayList;

@Mixin(EnchantmentHelper.class)
public abstract class FusionUnobtainabilityMixin {
	@Inject(
		method = "selectEnchantment",
		at = @At("RETURN"),
		cancellable = true
	)
	private static void change(CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
		List<String> list = new ArrayList<>();
		for (EnchantmentFusion fusion : EnchantmentFusion.fusions.values()) {
			list.add(fusion.result());
		}
		List<EnchantmentInstance> returning = cir.getReturnValue();
		List<EnchantmentInstance> removing = Lists.newArrayList();
		for(EnchantmentInstance ei : returning) {
			ResourceKey<Enchantment> key = ei.enchantment.unwrapKey().orElse(null);
			if (key != null && list.contains(key.location().toString()))
    			removing.add(ei);
		}
		returning.removeAll(removing);
		cir.setReturnValue(returning);
	}
}