package net.mcreator.enhancedenchanting.procedures;

import java.io.IOException;

public class ReloadConfigCommandProcedure {
	public static void execute() {
		try {
			net.mcreator.enhancedenchanting.EnchantmentModificationReader.init(null);
		} catch (IOException e) {
		}
	}
}