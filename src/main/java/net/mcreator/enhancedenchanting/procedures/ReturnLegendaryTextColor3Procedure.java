package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

import java.util.regex.Pattern;

public class ReturnLegendaryTextColor3Procedure {
	public static MutableComponent execute(String initial) {
		if (initial == null)
			return Component.empty();
		MutableComponent name = Component.empty();
		if (initial.length() != 0) {
			int i = 1;
			String _splitContent4 = Pattern.quote("");
			for (String stringiterator : initial.split(_splitContent4)) {
				int colortoadd = (int) (i * 0x111100);
				name.append(Component.literal(stringiterator).withColor((int) colortoadd));
				i++;
			}
			return name;
		}
		return Component.literal(initial);
	}
}