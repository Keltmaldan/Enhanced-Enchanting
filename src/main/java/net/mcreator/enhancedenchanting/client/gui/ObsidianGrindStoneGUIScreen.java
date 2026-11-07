package net.mcreator.enhancedenchanting.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.enhancedenchanting.world.inventory.ObsidianGrindStoneGUIMenu;
import net.mcreator.enhancedenchanting.procedures.TextOGSGUIModeProcedure;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModScreens;

import com.mojang.blaze3d.systems.RenderSystem;

public class ObsidianGrindStoneGUIScreen extends AbstractContainerScreen<ObsidianGrindStoneGUIMenu> implements EnhancedEnchantingModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Checkbox Mode;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("enhanced_enchanting:textures/screens/obsidian_grind_stone_gui.png");

	public ObsidianGrindStoneGUIScreen(ObsidianGrindStoneGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("Mode")) {
				if (Mode.selected() != logicState)
					Mode.onPress();
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, TextOGSGUIModeProcedure.execute(entity), 51, 13, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		Mode = Checkbox.builder(Component.translatable("gui.enhanced_enchanting.obsidian_grind_stone_gui.Mode"), this.font).pos(this.leftPos + 77, this.topPos + 23).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "Mode", value, false);
		}).build();
		this.addRenderableWidget(Mode);
	}
}