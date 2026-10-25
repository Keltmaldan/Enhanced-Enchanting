/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.enhancedenchanting.network.FireTrailEnchantmentUseMessage;
import net.mcreator.enhancedenchanting.network.BomberEnchantmentKeybindMessage;

@EventBusSubscriber(Dist.CLIENT)
public class EnhancedEnchantingModKeyMappings {
	public static final KeyMapping FIRE_TRAIL_ENCHANTMENT_USE = new KeyMapping("key.enhanced_enchanting.fire_trail_enchantment_use", GLFW.GLFW_KEY_G, "key.category.enhanced_enchanting.movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new FireTrailEnchantmentUseMessage(0, 0));
				FireTrailEnchantmentUseMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FIRE_TRAIL_ENCHANTMENT_USE_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FIRE_TRAIL_ENCHANTMENT_USE_LASTPRESS);
				PacketDistributor.sendToServer(new FireTrailEnchantmentUseMessage(1, dt));
				FireTrailEnchantmentUseMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping BOMBER_ENCHANTMENT_KEYBIND = new KeyMapping("key.enhanced_enchanting.bomber_enchantment_keybind", GLFW.GLFW_KEY_G, "key.category.enhanced_enchanting.inventory") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new BomberEnchantmentKeybindMessage(0, 0));
				BomberEnchantmentKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	private static long FIRE_TRAIL_ENCHANTMENT_USE_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(FIRE_TRAIL_ENCHANTMENT_USE);
		event.register(BOMBER_ENCHANTMENT_KEYBIND);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				FIRE_TRAIL_ENCHANTMENT_USE.consumeClick();
				BOMBER_ENCHANTMENT_KEYBIND.consumeClick();
			}
		}
	}
}