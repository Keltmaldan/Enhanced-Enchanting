package net.mcreator.enhancedenchanting.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.enhancedenchanting.world.inventory.ObsidianGrindStoneGUIMenu;
import net.mcreator.enhancedenchanting.procedures.TextOGSGUIModeProcedure;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class ObsidianGrindStoneGUIScreen extends AbstractContainerScreen<ObsidianGrindStoneGUIMenu> implements EnhancedEnchantingModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Checkbox Mode;
	private static final Identifier BACKGROUND = Identifier.parse("enhanced_enchanting:textures/screens/obsidian_grind_stone_gui.png");

	public ObsidianGrindStoneGUIScreen(ObsidianGrindStoneGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("Mode")) {
				if (Mode.selected() != logicState)
					Mode.onPress(null);
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, TextOGSGUIModeProcedure.execute(entity), 51, 13, -12829636, false);
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