package net.mcreator.enhancedenchanting.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class BowItemMixin {

    @Inject(
        method = "getUseItemRemainingTicks",
        at = @At("RETURN"),
        cancellable = true
    )
    private void enhancedEnchanting$rapidFire(CallbackInfoReturnable<Integer> cir) {

        LivingEntity entity = (LivingEntity)(Object)this;
        ItemStack stack = entity.getUseItem();
        if (stack.isEmpty()) return;
        Level level = entity.level();

        Holder<Enchantment> rapidFire = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(ResourceLocation.fromNamespaceAndPath("enhanced_enchanting", "rapid_fire"))
                .orElse(null);

        if (rapidFire == null) return;
        int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(rapidFire, stack);
        if (enchantLevel > 0) {
            int original = cir.getReturnValue();
            double factor = 1.0 + Math.log(1 + enchantLevel) * 0.25;
			int modified = (int)(original - enchantLevel / 2 + 0.5);

            cir.setReturnValue(modified);
        }
    }
}