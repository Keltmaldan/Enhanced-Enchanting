package net.mcreator.enhancedenchanting.procedure;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.core.RegistryAccess;

@EventBusSubscriber
public abstract class MirrorDo {
	private static final ResourceKey<Enchantment> mirror = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:mirror"));
	@SubscribeEvent
	public static void onBlock(LivingShieldBlockEvent event) {
		Entity attacker = event.getDamageSource().getEntity();
		if (attacker == null) return;
		ItemStack shield = event.getEntity().getUseItem();
		RegistryAccess access = attacker.level().registryAccess();
		int level = shield.getEnchantmentLevel(access.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(mirror));
		if (level != 0)
			attacker.hurt(new DamageSources(access).magic(), 1f -  (float) Math.pow(0.8, (double) level));
	}
}