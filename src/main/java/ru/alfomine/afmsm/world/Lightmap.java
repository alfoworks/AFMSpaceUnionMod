package ru.alfomine.afmsm.world;

public class Lightmap {
    public static float hue;

    public static void getLightMap(float[] lightmap) {
        for (int i = 0; i <= 15; ++i) {
            lightmap[i] = 0xff000000 | 0xFF0000;
        }
        for (int i = 0; i <= 15; ++i) {
            lightmap[i] = 0xff000000 | 0xFF0000;
        }

        hue += 0.1;

        if (hue >= 1) {
            hue = 0;
        }
    }
}
