package net.mcreator.enhancedenchanting;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;
import java.util.Set;
import java.util.List;
import org.objectweb.asm.tree.ClassNode;

public class EEEMixinPlugin implements IMixinConfigPlugin {
	private static final Set<String> clientOnly = Set.of("OmniscientDoMixin", "OmniscientDo2Mixin");
	@Override
	public void onLoad(String mixinPackage) {}
	@Override
    public String getRefMapperConfig() {
        return null;
    }
	@Override
	public boolean shouldApplyMixin(String className, String mixinClassName) {
		if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
			for (String s : clientOnly) {
				if (className.contains(s))
					return true;
			}
		}
		return true;
	}
	@Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override
    public List<String> getMixins() {
        return null;
    }
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}