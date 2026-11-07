package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

@EventBusSubscriber
public class ImmortalityDoProcedure {

    private static final Identifier IMMORTALITY = Identifier.parse("enhanced_enchanting:immortality");
    private static final Identifier PHEONIX = Identifier.parse("enhanced_enchanting:pheonix");
    private static final Identifier CHICKEN_MASTER = Identifier.parse("enhanced_enchanting:chicken_master");

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.FEET,EquipmentSlot.LEGS,EquipmentSlot.CHEST,EquipmentSlot.HEAD
    };

    @SubscribeEvent
    public static void onLethalDamage(LivingDamageEvent.Pre event) {

        LivingEntity living = event.getEntity();

        if (!(living.level() instanceof ServerLevel level))
            return;

        float damage = event.getNewDamage();

        if (damage <= 0)
            return;

        float survivableHealth = living.getHealth() + living.getAbsorptionAmount();

        if (damage < survivableHealth)
            return;

        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var immortalityEnchant = registry.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, IMMORTALITY));
        var pheonixEnchant = registry.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, PHEONIX));
        var chickenEnchant = registry.getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, CHICKEN_MASTER));

        EquipmentSlot chosenSlot = null;
        ItemStack chosenStack = ItemStack.EMPTY;

        boolean pheonix = false;
        boolean chickenMaster = false;

        for (EquipmentSlot slot : ARMOR_SLOTS) {

            ItemStack stack = living.getItemBySlot(slot);

            if (stack.isEmpty())
                continue;

            boolean hasPheonix = stack.getEnchantmentLevel(pheonixEnchant) > 0;
            boolean hasChicken = stack.getEnchantmentLevel(chickenEnchant) > 0;
            boolean hasImmortality = stack.getEnchantmentLevel(immortalityEnchant) > 0;

            if (hasPheonix || hasChicken || hasImmortality) {

                chosenSlot = slot;
                chosenStack = stack.copy();

                pheonix = hasPheonix;
                chickenMaster = hasChicken;

                break;
            }
        }

        if (chosenSlot == null)
            return;

        event.setNewDamage(0.0F);
        living.setHealth(Math.min(2.0F, living.getMaxHealth()));
        living.setItemSlot(chosenSlot, ItemStack.EMPTY);

        if (living instanceof Player player)
            player.closeContainer();

        living.addEffect(new MobEffectInstance(MobEffects.REGENERATION,900, 1, false, true));
        living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,800, 0, false, true));

        if (pheonix || chickenMaster) {

            living.addEffect(new MobEffectInstance(EnhancedEnchantingModMobEffects.INVINCIBILITY,5, 0, true, false));
            living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,9000, 1, false, true));
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING,600, 0, false, true));
            living.addEffect(new MobEffectInstance(MobEffects.STRENGTH,250, 2, false, true));

            if (pheonix) {

                level.explode(null,living.getX(),living.getY(),living.getZ(),4.0F,Level.ExplosionInteraction.MOB);
                level.sendParticles(ParticleTypes.FLAME,living.getX(),living.getY(),living.getZ(),64, 3, 3, 3, 1);

            } else if (chickenMaster) {

                Entity attacker = event.getSource().getEntity();

                if (attacker != null) {

                    for (int i = 0; i < 4; i++) {

                        Chicken chicken = EntityType.CHICKEN.spawn(level,living.blockPosition(),EntitySpawnReason.MOB_SUMMONED);

                        if (chicken != null) {
                            chicken.getPersistentData().putString("explosive_target",attacker.getStringUUID());
                        }
                    }
                }
            }

        } else {
        	
            living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,100, 1, false, true));
            level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,living.getX(),living.getY() + 1,living.getZ(),20, 0.5, 0.5, 0.5, 0.1);
        }

        level.playSound(null,living.blockPosition(),SoundEvents.TOTEM_USE,SoundSource.PLAYERS,1.0F,1.0F);
    }
}