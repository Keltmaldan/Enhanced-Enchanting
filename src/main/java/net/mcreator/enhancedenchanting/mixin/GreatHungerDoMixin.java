package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.component.DataComponents;

@Mixin(LivingEntity.class)
public class GreatHungerDoMixin {
	@Inject(
		at = @At("RETURN"),
		method = "getUseItemRemainingTicks",
		cancellable = true
	)
	private void change(CallbackInfoReturnable<Integer> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity == null) return;
		Level level = entity.level();
		if (level == null) return;
		if (!entity.getUseItem().has(DataComponents.FOOD)) return;
		int lvl = entity.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(
			level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(
				ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:great_hunger"))
			)
		);
		if (lvl != 0)
			cir.setReturnValue(cir.getReturnValue().intValue() - lvl);
	}
}