package net.mcreator.enhancedenchanting.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.enhancedenchanting.world.inventory.EnchantmentRemoverGUIMenu;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class EnchantmentRemoverGUIScreen extends AbstractContainerScreen<EnchantmentRemoverGUIMenu> implements EnhancedEnchantingModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox searchbar;
	private static final Identifier BACKGROUND = Identifier.parse("enhanced_enchanting:textures/screens/enchantment_remover_gui.png");

	public EnchantmentRemoverGUIScreen(EnchantmentRemoverGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 178);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 0 && elementState instanceof String stringState) {
			if (name.equals("searchbar"))
				searchbar.setValue(stringState);
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		searchbar.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
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
		if (searchbar.isFocused())
			return searchbar.keyPressed(event);
		return super.keyPressed(event);
	}

	@Override
	public void resize(int width, int height) {
		String searchbarValue = searchbar.getValue();
		super.resize(width, height);
		searchbar.setValue(searchbarValue);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		searchbar = new EditBox(this.font, this.leftPos + 96, this.topPos + 4, 72, 20, Component.translatable("gui.enhanced_enchanting.enchantment_remover_gui.searchbar"));
		searchbar.setMaxLength(8192);
		searchbar.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "searchbar", content, false);
		});
		searchbar.setHint(Component.translatable("gui.enhanced_enchanting.enchantment_remover_gui.searchbar"));
		this.addWidget(this.searchbar);
	}
}