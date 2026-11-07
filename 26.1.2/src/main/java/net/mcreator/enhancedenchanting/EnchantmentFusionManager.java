package net.mcreator.enhancedenchanting;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class EnchantmentFusionManager extends SimpleJsonResourceReloadListener<EnchantmentFusion> {
    public EnchantmentFusionManager() {
        super(EnchantmentFusion.CODEC, FileToIdConverter.json("fusion"));
    }

    @Override
    protected void apply(Map<Identifier, EnchantmentFusion> entries,
                         ResourceManager manager, ProfilerFiller profiler) {
        Map<String, EnchantmentFusion> loaded = new HashMap<>();
        entries.forEach((id, fusion) -> {
            loaded.put(id.toString(), fusion);
            EnhancedEnchantingMod.LOGGER.info("Successfully loaded EnchantmentFusion: " + id);
        });
        EnchantmentFusion.fusions = Map.copyOf(loaded);
    }
}