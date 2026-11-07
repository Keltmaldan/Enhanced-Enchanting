package net.mcreator.enhancedenchanting;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.level.ItemLike;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.Map;

@EventBusSubscriber(modid = "enhanced_enchanting")
public final class EnhancedEnchantingEnchantability {

    private static final Map<String, Integer> EXTRA_ITEM_VALUES = Map.of();

    @SubscribeEvent
    public static void modifyDefaultComponents(
        ModifyDefaultComponentsEvent event
    ) {
        add(event, Items.SHIELD, 9);
        add(event, Items.ELYTRA, 12);
        add(event, Items.SHEARS, 10);
        add(event, Items.FLINT_AND_STEEL, 8);

        if (!EXTRA_ITEM_VALUES.isEmpty()) {
            event.modifyMatching(
                (item, components) ->
                    EXTRA_ITEM_VALUES.containsKey(
                        String.valueOf(
                            BuiltInRegistries.ITEM.getKey(item)
                        )
                    ),
                (components, context, item) -> {
                    Integer value = EXTRA_ITEM_VALUES.get(
                        String.valueOf(
                            BuiltInRegistries.ITEM.getKey(item)
                        )
                    );

                    if (value != null && value > 0) {
                        components.set(
                            DataComponents.ENCHANTABLE,
                            new Enchantable(value)
                        );
                    }
                }
            );
        }
    }

    private static void add(
        ModifyDefaultComponentsEvent event,
        ItemLike item,
        int value
    ) {
        event.modify(
            item,
            (components, context, target) ->
                components.set(
                    DataComponents.ENCHANTABLE,
                    new Enchantable(value)
                )
        );
    }

    private EnhancedEnchantingEnchantability() {
    }
}