package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;


@Mixin(Block.class)
public abstract class BouncyDoMixin {
	@Nullable private static Vec3 movement = null;
	private static boolean shouldTrigger = false;
	@Inject(
		method = "updateEntityAfterFallOn",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void getMovement(BlockGetter level, Entity entity, CallbackInfo ci) {
		shouldTrigger = entity != null &&
				(entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)
				.getEnchantmentLevel(entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:bouncy")))) != 0;
		if (shouldTrigger)
			movement = entity.getDeltaMovement();
	}

	@Inject(
		method = "updateEntityAfterFallOn",
		at = @At("TAIL"),
		cancellable = true
	)
	private static void bounce(BlockGetter level, Entity entity, CallbackInfo ci) {
		if (shouldTrigger)
        	entity.setDeltaMovement(movement.x, -movement.y, movement.z);
	}
}