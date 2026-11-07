package net.mcreator.enhancedenchanting.init;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent.Pre;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModGameRules;
import java.util.Map;
import net.minecraft.util.RandomSource;
import java.util.HashMap;

@EventBusSubscriber
public class MixinVariableHandler {
	/*
		See L29 (red marker) for non-contextual variables
	*/

	private static final String removeLimit = "removeLimits";
	private static boolean removeEnchantLimit = false;
	@SubscribeEvent
	private static void onTick(Pre event) {
		removeEnchantLimit = event.getLevel().getLevelData().getGameRules().getBoolean(EnhancedEnchantingModGameRules.REMOVE_ENCHANTING_TABLE_LIMIT);
	}
	public static Boolean getRemoveEnchantLimit(Long key) throws IllegalAccessException {
		if (key.equals(map.get(removeLimit)))
			return removeEnchantLimit;
		throw new IllegalAccessException();
	}


	//keys are kept by the mixin
	private static final RandomSource random = RandomSource.create();
	private static Map<String, Long> map = new HashMap<>();
	public static long generateKey(String s) throws IllegalStateException {
		if (map.get(s) == null) {
			long key = random.nextLong();
			map.put(s, key);
			return key;
		}
		throw new IllegalStateException("A key already exists as :" + s);
	}
}