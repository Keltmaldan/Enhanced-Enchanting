package net.mcreator.enhancedenchanting.clientmixins;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.NeutralMob;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public abstract class OmniscientDo2Mixin {
	@Inject(
		method = "getTeamColor",
		at = @At("RETURN"),
		cancellable = true
	)
	private void change(CallbackInfoReturnable<Integer> cir) {
		final LivingEntity living = ((Object)this instanceof LivingEntity l) ? l : null;
		if (cir.getReturnValue() != 0xFFFFFF)
			return;
		if (living == null)
			return;
		if (living.level() == null || !living.level().isClientSide())
			return;
		if ((Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD))
			.getEnchantmentLevel(living.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:omniscient")))) == 0)
			return;
		if (living.hasEffect(MobEffects.GLOWING))
			return;
		if (living instanceof Monster)
			cir.setReturnValue(0xff0000);
		else if (living instanceof Player || living instanceof NeutralMob)
			cir.setReturnValue(0xffff00);
		else
			cir.setReturnValue(0x00ff00);
	}
}