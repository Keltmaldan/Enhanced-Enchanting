package net.mcreator.enhancedenchanting;

import net.minecraft.client.renderer.PostChain;
import java.nio.file.Path;
import net.neoforged.fml.loading.FMLPaths;
import java.nio.file.Files;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.JsonParser;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class BlurManager {
	/*private static final Path blurPath = FMLPaths.CONFIGDIR.get().resolve("enhanced_enchanting").resolve("blurs");
	private static Map<Integer, PostChain> map = new HashMap<>();
	public static PostChain parse(int level) throws NullPointerException {
		if (map.get(level) != null)
			return map.get(level);
		final String name = String.format("blur_%d.json", level);
		final Path path = blurPath.resolve(name);
		JsonObject json;
		Files.createDirectories(blurPath);
		try (JsonReader reader = Files.newBufferedReader(path)) {
			if (Files.exists(path))
				json = JsonParser.parseReader(reader);
			else {
				createFile(level);
				return parse(level);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (json == null)
			throw new NullPointerException(String.format("File not found : %s", path.toString()));
		
	}

	private static void createFile(int level) throws IOException {
		Files.createDirectories(blurPath);
    	Path path = blurPath.resolve(String.format("blur_%d.json", level));
    	if (Files.exists(path))
    	    return;
    	String json = """
        {
            "targets": ["swap"],
            "passes": [
                {
                    "name": "box_blur",
                    "intarget": "minecraft:main",
                    "outtarget": "swap",
                    "use_linear_filter": true,
                    "uniforms": [
                        {
                            "name": "BlurDir",
                            "values": [2.0, 0.0]
                        }
                    ]
                },
                {
                    "name": "box_blur",
                    "intarget": "swap",
                    "outtarget": "minecraft:main",
                    "use_linear_filter": true,
                    "uniforms": [
                        {
                            "name": "BlurDir",
                            "values": [0.0, 2.0]
                        }
                    ]
                }
            ]
        }
        """;
    	Files.writeString(path, json);
	}*/
}