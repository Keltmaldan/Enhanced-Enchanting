package net.mcreator.enhancedenchanting.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class OmniscientDoMixin {
    private static final ResourceKey<Enchantment> OMNISCIENT = ResourceKey.create(
        Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:omniscient"));

    @Inject(method = "shouldEntityAppearGlowing", at = @At("RETURN"), cancellable = true)
    private void enhancedEnchanting$omniscient(Entity target, CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = (Minecraft)(Object)this;
        Player player = minecraft.player;
        if (player == null || player.level() == null || target == null) return;
        var enchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
            .get(OMNISCIENT).orElse(null);
        if (enchantment != null && player.getItemBySlot(EquipmentSlot.HEAD)
                .getEnchantmentLevel(enchantment) > 0) {
            cir.setReturnValue(true);
        }
    }
}