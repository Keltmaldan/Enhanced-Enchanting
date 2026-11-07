package net.mcreator.enhancedenchanting.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.procedures.Souls4Procedure;
import net.mcreator.enhancedenchanting.procedures.Souls3Procedure;
import net.mcreator.enhancedenchanting.procedures.Souls2Procedure;
import net.mcreator.enhancedenchanting.procedures.Souls1Procedure;

@EventBusSubscriber(Dist.CLIENT)
public class SoulCatalystOverlayOverlay {
	private static final Identifier IMAGE_0 = Identifier.parse("enhanced_enchanting:textures/screens/soul.png");
	private static final Identifier IMAGE_1 = Identifier.parse("enhanced_enchanting:textures/screens/soul.png");
	private static final Identifier IMAGE_2 = Identifier.parse("enhanced_enchanting:textures/screens/soul.png");
	private static final Identifier IMAGE_3 = Identifier.parse("enhanced_enchanting:textures/screens/soul.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		if (true) {
			if (Souls1Procedure.execute(entity)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, w / 2 + 9, h / 2 + -4, 0, 0, 16, 16, 16, 16);
			}
			if (Souls2Procedure.execute(entity)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, w / 2 + 27, h / 2 + -4, 0, 0, 16, 16, 16, 16);
			}
			if (Souls3Procedure.execute(entity)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, w / 2 + 45, h / 2 + -4, 0, 0, 16, 16, 16, 16);
			}
			if (Souls4Procedure.execute(entity)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, w / 2 + 63, h / 2 + -4, 0, 0, 16, 16, 16, 16);
			}
		}
	}
}