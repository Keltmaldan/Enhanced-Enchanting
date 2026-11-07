
package net.mcreator.enhancedenchanting.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BouncyDoMixin {

    private static final ResourceKey<Enchantment> BOUNCY = ResourceKey.create(Registries.ENCHANTMENT,Identifier.fromNamespaceAndPath("enhanced_enchanting","bouncy"));

    @Inject(
        method = "updateEntityMovementAfterFallOn",
        at = @At("HEAD"),
        cancellable = true
    )
    private void enhancedEnchanting$bounce(BlockGetter level,Entity entity,CallbackInfo ci) {

        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);

        if (boots.isEmpty()) {
            return;
        }

        var registry = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        var enchantment = registry.get(BOUNCY).orElse(null);

        if (enchantment == null) {
            return;
        }

        if (boots.getEnchantmentLevel(enchantment) <= 0) {
            return;
        }

        Vec3 velocity = entity.getDeltaMovement();

        if (velocity.y >= 0.0D) {
            return;
        }

        entity.setDeltaMovement(velocity.x,-velocity.y,velocity.z);

        ci.cancel();
    }
}