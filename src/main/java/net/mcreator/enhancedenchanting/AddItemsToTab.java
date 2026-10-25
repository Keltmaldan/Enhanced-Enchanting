package net.mcreator.enhancedenchanting;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModItems;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModTabs;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.chat.Component;
import java.util.HashMap;
import net.minecraft.core.Holder;
import java.util.ArrayList;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.util.RandomSource;


@EventBusSubscriber
public abstract class AddItemsToTab {
	private static boolean loadedCategories = false;
	private static RandomSource random = RandomSource.create();

	private static ItemStack empty() {
		ItemStack EMPTY = new ItemStack(EnhancedEnchantingModItems.EMPTY_STACK.value(), 1);
		EMPTY.set(DataComponents.CUSTOM_NAME, Component.literal(Long.toString(random.nextLong())));
		return EMPTY;
	}

	private static enum Category {
		WEAPONS(0, "Weapons", "minecraft:enchantable/weapon", "minecraft:enchantable/sword", "minecraft:enchantable/fire_aspect", "minecraft:enchantable/mace", "minecraft:shield_buster_tag", "minecraft:enchantable/sharp_weapon", "minecraft:enchantable/trident"),
		RANGED(1, "Ranged Weapons", "minecraft:enchantable/distance", "minecraft:enchantable/bow", "minecraft:enchantable/crossbow", "enhanced_enchanting:velocity_tag"),
		TOOLS(2, "Tools", "minecraft:enchantable/mining", "minecraft:pickaxes", "minecraft:axes", "minecraft:hoes", "minecraft:shovels", "minecraft:enchantable/mining_loot"),
		HELMET(3, "Helmet", "minecraft:head_armor", "minecraft:enchantable/head_armor"),
		CHEST(4, "Chestplate", "minecraft:enchantable/chest_armor"),
		LEGS(5, "Leggings", "minecraft:enchantable/leg_armor"),
		BOOTS(6, "Boots", "minecraft:enchantable/foot_armor"),
		ARMOR(7, "Armor", "minecraft:enchantable/armor", "minecraft:enchantable/equippable"),
		OTHER(8, "Other", ""),
		ELYTRAS(9, "Elytras", "enhanced:elytras"),
		SHIELD(10, "Shield", "minecraft:shield");

		public HashMap<Holder<Enchantment>, ArrayList<ItemStack>> list = new HashMap<>();
		private final String name;
		public final String[] tags;
		private final int number;
		private Category(int i, String n, String ... t) {
			name = n;
			tags = t;
			number = i;
		}
		public int getEndSpaces() {
			int r = 0;
			for (Holder<Enchantment> enchant : list.keySet()) {
				r += list.get(enchant).size();
			}
			return 9 - (r % 9);
		}
		public static Category get(int i) throws IllegalArgumentException {
			if (i > 10 || i < 0)
				throw new IllegalArgumentException("Invalid category :" + i);
			return Category.values()[i];
		}
		public boolean isEnchantIn(Holder<Enchantment> enchant) {
			if (this.number == 8) {	
				for (Category cat : Category.values()) {
					if (cat.number == 8)
						continue;
					if (cat.isEnchantIn(enchant))
						return false;
				}
				return true;
			}
			HolderSet<Item> supportedItems = enchant.value().definition().supportedItems();
			if (supportedItems instanceof HolderSet.Named<Item> named) {
    			TagKey<Item> tagKey = named.key();
    			for (String tag : tags) {
    			if (tagKey.location().toString().equals(tag))
        			return true;
    			}
			} return false;
		}
		public void add(Holder<Enchantment> enchant) {
			ArrayList<ItemStack> l = new ArrayList<>();
			for (int i = 1; i <= enchant.value().getMaxLevel(); i++) {
            	ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
                ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                mutable.set(enchant, i);
                stack.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
                l.add(stack);
            }
            list.put(enchant, l);
		}
		public void display(BuildCreativeModeTabContentsEvent event) {
			// ligne avec le panneau
			for (int i = 0; i < 9; i++) {
				if (i == 4) {
					ItemStack stack = new ItemStack(Items.OAK_SIGN);
					stack.set(DataComponents.CUSTOM_NAME, Component.literal(this.name));
					event.accept(stack);
				} else event.accept(empty());
			}
			// livres
			for (Holder<Enchantment> enchant : list.keySet()) {
				for (ItemStack stack : list.get(enchant)) {
					event.accept(stack);
				}
			}
			//espaces de fin
			for (int i = 0; i < getEndSpaces(); i++) {
				event.accept(empty());
			}
		}
	}


	private static final DeferredRegister<CreativeModeTab> REGISTRY = EnhancedEnchantingModTabs.REGISTRY;
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ENHANCED_ENCHANTING_OP_TAB = REGISTRY.register("enhanced_enchanting_op_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.enhanced_enchanting.enhanced_enchanting_op_tab")).icon(() -> EnhancedEnchantingModItems.ENCHANTMENTS_UPGRADER.toStack()).displayItems((parameters, tabData) -> {}).withSearchBar().build());

	@SubscribeEvent
	public static void start(BuildCreativeModeTabContentsEvent event) { // avant LevelEvent.Load
		if (loadedCategories)
			return;
		loadedCategories = true;
		
		RegistryLookup<Enchantment> enchantments = event.getParameters().holders().lookupOrThrow(Registries.ENCHANTMENT);
		for (Reference<Enchantment> holder : enchantments.listElements().toList()) {
                ResourceLocation id = holder.key().location();
                if (id.getNamespace().equals(EnhancedEnchantingMod.MODID))
                	for (Category cat : Category.values()) {
                		if (cat.isEnchantIn(holder))
                			cat.add(holder);
                	}
            }
	}

	@SubscribeEvent
	public static void addItems(BuildCreativeModeTabContentsEvent event) {
		RegistryLookup<Enchantment> enchantments = event.getParameters().holders().lookupOrThrow(Registries.ENCHANTMENT);
		if (event.getTab().equals(EnhancedEnchantingModTabs.ENHANCED_ENCHANTING_TAB.value())) {
			// espaces du début
			for (int i = 0; i < 4; i++) {
				event.accept(empty());
			}
			// catégories
            for (Category cat : Category.values()) {
            	cat.display(event);
            }
		} else if (event.getTab().equals(ENHANCED_ENCHANTING_OP_TAB.value())) {
			ItemEnchantments.Mutable allEnch = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
			for (Reference<Enchantment> holder : enchantments.listElements().toList()) {
                ResourceLocation id = holder.key().location();
                ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
                ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                mutable.set(holder, 255);
                allEnch.set(holder, 255);
                stack.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
                if (holder.value().getMaxLevel() == 1)
					continue;
                event.accept(stack);
            }
            ItemStack all = new ItemStack(Items.ENCHANTED_BOOK);
			all.set(DataComponents.CUSTOM_NAME, Component.literal("All Enchantments"));
			all.set(DataComponents.STORED_ENCHANTMENTS, allEnch.toImmutable());
            event.accept(all);
		}
	}
}