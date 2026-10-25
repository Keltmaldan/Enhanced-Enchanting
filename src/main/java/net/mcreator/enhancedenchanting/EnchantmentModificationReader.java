package net.mcreator.enhancedenchanting;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;

//I consider an enchantment as a String in this file
@EventBusSubscriber
public class EnchantmentModificationReader {
	private static final byte[] lastUpdate = {2,3,2,0}; //last update of the config system
	private static final byte[] disable = {127,127,127,127}; //write it in .test to avoid the loading of the config files
	private static final Path mainPath = FMLPaths.CONFIGDIR.get().resolve("enhanced_enchanting");
	private static final Path testPath = mainPath.resolve(".test");
	private static final Path removePath =mainPath.resolve("remove.txt");
	private static final Path maxLevelPath = mainPath.resolve("max_level.txt");
	private static final Path weightPath = mainPath.resolve("weight.txt");
	private static final Path anvilCostPath = mainPath.resolve("anvil_cost.txt");
	private static final Path[] pathList = {
		maxLevelPath, weightPath, anvilCostPath, removePath, testPath
	};
	public static List<String> toRemove;
	public static Map<String, Integer> maxLevel;
	public static Map<String, Integer> weight;
	public static Map<String, Integer> anvilCost;
	@SubscribeEvent
	public static void init(@Nullable FMLCommonSetupEvent event) throws IOException {
		if (!Files.exists(testPath) || !Arrays.equals(Files.readAllBytes(testPath), lastUpdate)) {
			for (Path path : pathList) {
				Files.deleteIfExists(path);
			}
			Files.deleteIfExists(mainPath);
			Files.createDirectory(mainPath);
			for (Path path : pathList) {
				Files.createFile(path);
			}
			Files.setAttribute(testPath, "dos:hidden", true);
			Files.write(testPath, lastUpdate);
		}
		if (Files.exists(testPath) && Arrays.equals(Files.readAllBytes(testPath), disable)) {
			toRemove = List.of();
			maxLevel = Map.of();
			weight = Map.of();
			anvilCost = Map.of();
			return;
		}
		
		toRemove = ImmutableList.copyOf(Files.readAllLines(removePath));
		toRemove = (toRemove == null) ? List.of() : toRemove;
		maxLevel = fromPath(maxLevelPath);
		weight = fromPath(weightPath);
		anvilCost = fromPath(anvilCostPath);
	}
	public static Map<String, Integer> fromPath(Path path) throws IOException {
		if (Files.exists(path)) {
			List<String> lines = Files.readAllLines(path);
			HashMap<String, Integer> enchants = new HashMap<>();
			for (String line : lines) {
				if (line.startsWith("//")) continue;
				boolean passed = false;
				char[] enchant = new char[128], lvl = new char[3];
				int lvlQtny = 0;
				int enchantQtny = 0;
				for (char c : line.toCharArray()) {
					if (c == '$') {
						passed = true;
						continue;
					}
					if (passed) {
						lvl[lvlQtny] = c;
						lvlQtny++;
					}
					else {
						enchant[enchantQtny] = c;
						enchantQtny++;
					}
				}
				enchants.put(new String(enchant).trim(), Integer.parseInt(new String(lvl).trim()));
			}
			return ImmutableMap.copyOf(enchants);
		}
		return Map.of();
	}
}