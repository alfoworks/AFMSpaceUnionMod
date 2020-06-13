package ru.alfomine.afmsm.planet;

import cr0s.warpdrive.data.CelestialObject;
import cr0s.warpdrive.data.CelestialObjectManager;
import ru.alfomine.afmsm.server.PlanetConfig;

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

    public static int getSpaceSize() {
        CelestialObject space = CelestialObjectManager.get(false, "solarSystem");

        return space.borderRadiusX;
    }
}
