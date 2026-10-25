package net.mcreator.enhancedenchanting;

import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import com.google.gson.JsonElement;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.HashMap;
import com.mojang.serialization.JsonOps;

public class EnchantmentFusionManager extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = new Gson();
	
	public EnchantmentFusionManager() {
		super(GSON, "fusion");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager manager, ProfilerFiller filler) {
		Map<String, EnchantmentFusion> newFusions = new HashMap<>();

		for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
			ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();
			String name = "INVALID NAME";
            try {
            	name = id.toString();
            	EnchantmentFusion fusion = EnchantmentFusion.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            	newFusions.put(name, fusion);
            	EnhancedEnchantingMod.LOGGER.info("Successfuly loaded EnchantmentFusion : " + name);
            } catch (Exception e) {
            	EnhancedEnchantingMod.LOGGER.warn("Impossible to register EnchantmentFusion : " + name);
            	e.printStackTrace();
            }
		}

		EnchantmentFusion.fusions = Map.copyOf(newFusions);
	}
}