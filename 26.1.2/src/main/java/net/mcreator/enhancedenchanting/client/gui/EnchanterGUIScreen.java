package net.mcreator.enhancedenchanting.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.enhancedenchanting.world.inventory.EnchanterGUIMenu;
import net.mcreator.enhancedenchanting.network.EnchanterGUIButtonMessage;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class EnchanterGUIScreen extends AbstractContainerScreen<EnchanterGUIMenu> implements EnhancedEnchantingModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_enchanter_top;
	private ExtendedSlider level;
	private static final Identifier BACKGROUND = Identifier.parse("enhanced_enchanting:textures/screens/enchanter_gui.png");

	public EnchanterGUIScreen(EnchanterGUIMenu container, Inventory inventory, Component text) {
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
		if (elementType == 2 && elementState instanceof Number n) {
			if (name.equals("level"))
				level.setValue(n.doubleValue());
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (mouseX > leftPos + 65 && mouseX < leftPos + 113 && mouseY > topPos + 57 && mouseY < topPos + 81) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.enhanced_enchanting.enchanter_gui.tooltip_remember_that_a_too_high_level_c"), mouseX, mouseY);
		}
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
	}

	@Override
	public void init() {
		super.init();
		imagebutton_enchanter_top = new ImageButton(this.leftPos + 80, this.topPos + 7, 16, 16,
				new WidgetSprites(Identifier.parse("enhanced_enchanting:textures/screens/enchanter_top__.png"), Identifier.parse("enhanced_enchanting:textures/screens/enchanter_top__.png")), e -> {
					int x = EnchanterGUIScreen.this.x;
					int y = EnchanterGUIScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new EnchanterGUIButtonMessage(0, x, y, z));
						EnchanterGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_enchanter_top);
		level = new ExtendedSlider(this.leftPos + 66, this.topPos + 60, 46, 20, Component.translatable("gui.enhanced_enchanting.enchanter_gui.level_prefix"), Component.translatable("gui.enhanced_enchanting.enchanter_gui.level_suffix"), 0, 64, 0, 1,
				0, true) {
			@Override
			protected void applyValue() {
				if (!menuStateUpdateActive)
					menu.sendMenuStateUpdate(entity, 2, "level", this.getValue(), false);
			}
		};
		this.addRenderableWidget(level);
		if (!menuStateUpdateActive)
			menu.sendMenuStateUpdate(entity, 2, "level", level.getValue(), false);
	}
}