package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

import java.util.regex.Pattern;

public class ReturnLegendaryTextColorProcedure {
	public static MutableComponent execute(String initial) {
		if (initial == null)
			return Component.empty();
		MutableComponent name = Component.empty();
		double colorStep = 0;
		double color = 0;
		color = 0x999900;
		int lenght = (initial).length();
		if (lenght != 0) {
			int i = 1;
			int R = ((int) color >> 16) & 0xFF;
			int G = ((int) color >> 8) & 0xFF;
			int B = (int) color & 0xFF;
			int Alpha = ((int) color >> 24) & 0xFF;
			int maxRGBA = Math.max(Math.max(R, Alpha), Math.max(G, B));
			colorStep = ((int) (Alpha - maxRGBA) << 24) | ((int) (R - maxRGBA) << 16) | ((int) (G - maxRGBA) << 8) | (int) (B - maxRGBA);
			String _splitContent6 = Pattern.quote("");
			for (String stringiterator : initial.split(_splitContent6)) {
				int colortoadd = (int) ((i % lenght) * colorStep);
				name.append(Component.literal(stringiterator).withColor((int) (colortoadd == 0 ? (int) colorStep : colortoadd)));
				i++;
			}
			return name;
		}
		return Component.literal(initial);
	}
}