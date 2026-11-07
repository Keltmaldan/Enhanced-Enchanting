package net.mcreator.enhancedenchanting.clientmixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.Minecraft;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;

@Mixin(Minecraft.class)
public abstract class OmniscientDoMixin {
	@Shadow LocalPlayer player;
	@Shadow ClientLevel level;
	@Inject(
		method = "shouldEntityAppearGlowing",
		at = @At("RETURN"),
		cancellable = true
	)
	private void change(CallbackInfoReturnable<Boolean> cir) {
		if (this.level == null || !this.level.isClientSide() || this.player == null)
			return;
		if ((this.player.getItemBySlot(EquipmentSlot.HEAD))
				.getEnchantmentLevel(this.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:omniscient")))) != 0)
			cir.setReturnValue(true);
	}
}