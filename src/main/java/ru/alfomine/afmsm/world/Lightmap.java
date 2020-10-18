package ru.alfomine.afmsm.world;

import ru.alfomine.afmsm.AFMSpaceUnionMod;

public class Lightmap {
    public static void initLightmap() {
        AFMSpaceUnionMod.class.getClassLoader()
                .getResourceAsStream("assets/afmsm/space_ligtmap");
    }
}
