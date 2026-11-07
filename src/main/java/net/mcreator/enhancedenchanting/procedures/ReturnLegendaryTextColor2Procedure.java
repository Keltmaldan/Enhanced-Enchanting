package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

import java.util.regex.Pattern;

public class ReturnLegendaryTextColor2Procedure {
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
			int maxRGB = Math.max(R, Math.max(G, B));
			colorStep = ((255 << 24) | ((int) (R - maxRGB) << 16) | ((int) (G - maxRGB) << 8) | (int) (B - maxRGB)) * 127.5f / (lenght - 1);
			String _splitContent6 = Pattern.quote("");
			for (String stringiterator : initial.split(_splitContent6)) {
				int colortoadd = (int) ((i * colorStep));
				colortoadd = colortoadd & 0xFFFFFF;
				name.append(Component.literal(stringiterator).withColor((int) (colortoadd == 0 ? (int) colorStep : colortoadd)));
				i++;
			}
			return name;
		}
		return Component.literal(initial);
	}
}