package ru.alfomine.afmsm.space;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import ru.alfomine.afmsm.server.SpaceConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SpaceManager {
    public static Space getSpace(boolean allPlanets) {
        List<Planet> planets;

        if (allPlanets) {
            CelestialObjectManager manager;
            planets = new ArrayList<>();

            try {
                Field field = CelestialObjectManager.class.getDeclaredField("SERVER");
                field.setAccessible(true);

                manager = (CelestialObjectManager) field.get(CelestialObjectManager.class);
            } catch (Exception ignored) {
                manager = null;
            }

            if (manager != null) {
                for (CelestialObject object : manager.celestialObjects) {
                    if (!object.parentId.equals("solarSystem")) continue;

                    planets.add(SpaceConfig.getPlanetFromCelestial(object, PlanetDifficulty.EZ));
                }
            }
        } else {
            planets = SpaceConfig.planets;
        }

        return new Space(planets,
                SpaceConfig.mainPlanet,
                CelestialObjectManager.get(false, "solarSystem").borderRadiusX);
    }
}
