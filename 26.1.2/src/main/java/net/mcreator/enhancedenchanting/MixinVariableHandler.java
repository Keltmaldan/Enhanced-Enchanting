package net.mcreator.enhancedenchanting.init;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class MixinVariableHandler {

    private static final String REMOVE_LIMIT = "removeLimits";

    private static volatile boolean removeEnchantLimit = false;

    private static final RandomSource RANDOM = RandomSource.create();

    private static final Map<String, Long> map = new HashMap<>();

    @SubscribeEvent
    public static void onTick(LevelTickEvent.Pre event) {

        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        removeEnchantLimit = level.getGameRules().get(EnhancedEnchantingModGameRules.REMOVE_ENCHANTING_TABLE_LIMIT.get());
    }

    public static Boolean getRemoveEnchantLimit(Long key)
            throws IllegalAccessException {

        Long expectedKey = map.get(REMOVE_LIMIT);

        if (key != null && key.equals(expectedKey)) {
            return removeEnchantLimit;
        }

        throw new IllegalAccessException("Invalid access key for removeEnchantLimit");
    }

    public static long generateKey(String name)
            throws IllegalStateException {

        if (map.containsKey(name)) {
            throw new IllegalStateException("A key already exists as: " + name);
        }

        long key = RANDOM.nextLong();
        map.put(name, key);
        return key;
    }
}