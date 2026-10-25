package net.mcreator.enhancedenchanting.mixin;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;

@Mixin(Minecraft.class)
public abstract class OmniscientDoMixin {
	@Inject(
		method = "shouldEntityAppearGlowing",
		at = @At("RETURN"),
		cancellable = true
	)
	private static void change(CallbackInfoReturnable<Boolean> cir) {
		Player entity = Minecraft.getInstance().player;
		Level world = entity.level();
		if ((entity.getItemBySlot(EquipmentSlot.HEAD))
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:omniscient")))) != 0)
			cir.setReturnValue(true);
	}
}