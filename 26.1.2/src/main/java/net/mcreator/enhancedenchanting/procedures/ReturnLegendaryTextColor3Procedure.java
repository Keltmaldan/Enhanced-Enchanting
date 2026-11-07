package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ReturnLegendaryTextColor3Procedure {
    public static MutableComponent execute(String initial) {
        if (initial == null) return Component.empty();
        MutableComponent name = Component.empty();
        int i = 1;
        for (String chr : initial.split("")) {
            int color = i * 0x111100;
            name.append(Component.literal(chr).withColor(color & 0xFFFFFF));
            i++;
        }
        return name;
    }
}