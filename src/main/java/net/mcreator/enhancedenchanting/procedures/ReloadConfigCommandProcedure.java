package net.mcreator.enhancedenchanting.procedures;

public class ReloadConfigCommandProcedure {
	public static void execute() {
		try {
			net.mcreator.enhancedenchanting.EnchantmentModificationReader.init(null);
		} catch (Exception e) {
		}
	}
}