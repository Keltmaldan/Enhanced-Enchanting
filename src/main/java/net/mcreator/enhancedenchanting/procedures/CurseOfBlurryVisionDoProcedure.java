package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;

@EventBusSubscriber(value = Dist.CLIENT)
public class CurseOfBlurryVisionDoProcedure {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		execute(event, mc.level, mc.player);
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String name = "";
		int lvl = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)
				.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:curse_of_blurry_vision"))));
		GameRenderer renderer = Minecraft.getInstance().gameRenderer;
		if (renderer == null)
			return;
		if (lvl != 0) {
			name = ("enhanced_enchanting:shaders/blur_"
					+ Math.min(5,
							lvl)
					+ ".json").replace(".0", "");
			if (!(Minecraft.getInstance().gameRenderer.currentEffect() == null ? false : renderer.currentEffect().getName().equals(name))) {
				renderer.loadEffect(ResourceLocation.parse((name).toLowerCase()));
			}
		} else if (renderer.currentEffect() == null ? false : renderer.currentEffect().getName().contains("blur_")) {
			renderer.shutdownEffect();
		}
	}
}