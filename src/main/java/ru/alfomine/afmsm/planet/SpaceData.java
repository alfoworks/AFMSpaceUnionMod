package ru.alfomine.afmsm.planet;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import net.minecraft.util.ResourceLocation;
import ru.alfomine.afmsm.server.PlanetConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SpaceData {
    public static List<Planet> getPlanets() {
        try {
            return PlanetConfig.planets;
        } catch (NoClassDefFoundError ignored) {
            return new ArrayList<>();
        }
    }

    public static List<Planet> getSolarAtlasPlanes() {
        // Варпдрайв, попей говна

        CelestialObjectManager manager;

        try {
            Field field = CelestialObjectManager.class.getDeclaredField("SERVER");
            field.setAccessible(true);

            manager = (CelestialObjectManager) field.get(CelestialObjectManager.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }

        ArrayList<Planet> planets = new ArrayList<>();

        for (CelestialObject object : manager.celestialObjects) {
            if (!object.parentId.equals("solarSystem")) continue;

            ResourceLocation iconResource = object.setRenderData.iterator().next().resourceLocation;

            if (iconResource == null) continue;

            planets.add(new Planet(iconResource,
                    object.getDisplayName(),
                    object.id,
                    PlanetDifficulty.EZ,
                    object.borderRadiusX));
        }

        return planets;
    }

    public static int getSpaceSize() {
        CelestialObject space = CelestialObjectManager.get(false, "solarSystem");

        return space.borderRadiusX;
    }
}
