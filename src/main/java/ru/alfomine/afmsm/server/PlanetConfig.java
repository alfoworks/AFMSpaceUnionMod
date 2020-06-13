package ru.alfomine.afmsm.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.IOUtils;
import ru.alfomine.afmsm.AFMSpaceUnionMod;
import ru.alfomine.afmsm.planet.Planet;
import ru.alfomine.afmsm.planet.PlanetDifficulty;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanetConfig {
    public static ArrayList<Planet> planets = new ArrayList<>();

    public static void init() {
        File configDir = getConfigFolder();

        if (!configDir.exists()) {
            if (!configDir.mkdirs()) {
                AFMSpaceUnionMod.logger.error("Can't create afmsm folder.");

                return;
            }
        }

        File configFile = new File(configDir.getPath() + "/planets.json");

        if (!configFile.exists()) {
            try {
                if (!configFile.createNewFile()) {
                    AFMSpaceUnionMod.logger.error("Can't create planets config file.");

                    return;
                }
            } catch (IOException e) {
                AFMSpaceUnionMod.logger.error("Can't create planets config file.");

                e.printStackTrace();

                return;
            }

            InputStream stream = AFMSpaceUnionMod.class.getClassLoader()
                    .getResourceAsStream("assets/afmsm/planets.json");

            if (stream == null) {
                AFMSpaceUnionMod.logger.error("Can't load config: inputstream is null");

                return;
            }

            try {
                OutputStream outputStream = new FileOutputStream(configFile);

                IOUtils.copy(stream, outputStream);
            } catch (IOException e) {
                AFMSpaceUnionMod.logger.error("IOException when writing config.");

                e.printStackTrace();

                return;
            }
        }

        try {
            readConfig();
        } catch (IOException e) {
            AFMSpaceUnionMod.logger.error("Can't read config file.");

            e.printStackTrace();
        }
    }

    private static void readConfig() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(getConfigFolder().getPath() + "/planets.json"));

        JsonArray json = new Gson().fromJson(bufferedReader, JsonArray.class);

        planets.clear();

        for (JsonElement element : json.getAsJsonArray()) {
            JsonObject jsonPlanet = element.getAsJsonObject();

            String warpId = jsonPlanet.get("warpId").getAsString();
            int difficultyInt = jsonPlanet.get("difficulty").getAsInt();

            CelestialObject cel = CelestialObjectManager.get(false, warpId);

            if (cel == null) {
                AFMSpaceUnionMod.logger.error(String.format("%s is in planet config but not in warpdrive.", warpId));

                continue;
            }

            String name = cel.getDisplayName();
            ResourceLocation iconResource = cel.setRenderData.iterator().next().resourceLocation;
            int size = cel.borderRadiusX;
            PlanetDifficulty difficulty = PlanetDifficulty.values()[difficultyInt];

            planets.add(new Planet(iconResource, name, difficulty, size));
        }
    }

    private static File getConfigFolder() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        File file;

        if (server != null && server.isDedicatedServer()) {
            file = new File(".");
        } else {
            file = Minecraft.getMinecraft().gameDir;
        }

        return new File(file.getPath() + "/AFMSM/");
    }
}
