package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class SoulCatalystShaderProcedure {

    private static final Identifier SOUL_EFFECT = Identifier.fromNamespaceAndPath("enhanced_enchanting","soul_catalyst");

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        execute(Minecraft.getInstance().player);
    }

    public static void execute(Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        GameRenderer renderer = mc.gameRenderer;
        Identifier current = renderer.currentPostEffect();
        boolean hasSoulStrength = entity instanceof LivingEntity living && living.hasEffect(EnhancedEnchantingModMobEffects.SOUL_STRENGHT);

        if (hasSoulStrength) {
            if (current == null) {
                renderer.setPostEffect(SOUL_EFFECT);
            }

        } else {
            if (SOUL_EFFECT.equals(current)) {
                renderer.clearPostEffect();
            }
        }
    }
}