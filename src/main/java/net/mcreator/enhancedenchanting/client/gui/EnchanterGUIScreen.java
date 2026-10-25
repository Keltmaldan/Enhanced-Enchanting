package net.mcreator.enhancedenchanting.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.enhancedenchanting.world.inventory.EnchanterGUIMenu;
import net.mcreator.enhancedenchanting.network.EnchanterGUIButtonMessage;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModScreens;

import com.mojang.blaze3d.systems.RenderSystem;

public class EnchanterGUIScreen extends AbstractContainerScreen<EnchanterGUIMenu> implements EnhancedEnchantingModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_enchanter_top;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("enhanced_enchanting:textures/screens/enchanter_gui.png");
	private ExtendedSlider level;

	public EnchanterGUIScreen(EnchanterGUIMenu container, Inventory inventory, Component text) {
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
		if (elementType == 2 && elementState instanceof Number n) {
			if (name.equals("level"))
				level.setValue(n.doubleValue());
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + 65 && mouseX < leftPos + 113 && mouseY > topPos + 57 && mouseY < topPos + 81) {
			guiGraphics.renderTooltip(font, Component.translatable("gui.enhanced_enchanting.enchanter_gui.tooltip_remember_that_a_too_high_level_c"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (!customTooltipShown)
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
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		return (this.getFocused() != null && this.isDragging() && button == 0) ? this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY) : super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		imagebutton_enchanter_top = new ImageButton(this.leftPos + 80, this.topPos + 7, 16, 16,
				new WidgetSprites(ResourceLocation.parse("enhanced_enchanting:textures/screens/enchanter_top__.png"), ResourceLocation.parse("enhanced_enchanting:textures/screens/enchanter_top__.png")), e -> {
					int x = EnchanterGUIScreen.this.x;
					int y = EnchanterGUIScreen.this.y;
					if (true) {
						PacketDistributor.sendToServer(new EnchanterGUIButtonMessage(0, x, y, z));
						EnchanterGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
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