package net.mcreator.enhancedenchanting.mixin;

import net.mcreator.enhancedenchanting.EnchantmentModificationReader;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.chat.Component;
import java.util.Map;

@Mixin(Enchantment.class)
public abstract class MaxLevelModifierFromConfigMixin {
	@Shadow public Component description;
	private String name() {
		return (description == null) ? "" : description.toString().replace("translation{key='", "")
			.replace("', args=[]}", "")
			.replace("enchantment.", "")
			.replace(".", ":");
	}
	private void change(CallbackInfoReturnable<Integer> cir, Map<String, Integer> map) {
		if (map.keySet().contains(this.name()))
			cir.setReturnValue(map.get(this.name()));
		else if (map.keySet().contains("all"))
			cir.setReturnValue(map.get("all"));
	}
	@Inject(
		method = "getMaxLevel",
		at = @At("RETURN"),
		cancellable = true
	)
	private void maxlvl(CallbackInfoReturnable<Integer> cir) {
		change(cir, EnchantmentModificationReader.maxLevel);
	}
	@Inject(
		method = "getWeight",
		at = @At("RETURN"),
		cancellable = true
	)
	private void weight(CallbackInfoReturnable<Integer> cir) {
		change(cir, EnchantmentModificationReader.weight);
	}
	@Inject(
		method = "getAnvilCost",
		at = @At("RETURN"),
		cancellable = true
	)
	private void anvilcost(CallbackInfoReturnable<Integer> cir) {
		change(cir, EnchantmentModificationReader.anvilCost);
	}
}