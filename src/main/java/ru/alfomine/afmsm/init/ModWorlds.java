package ru.alfomine.afmsm.init;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import ru.alfomine.afmsm.world.SpawnWorldProvider;

public class ModWorlds {
    public static DimensionType SPAWN_WORLD_DIM_TYPE;

    public static void init() {
        SPAWN_WORLD_DIM_TYPE = registerDim("spawnworld", -1202);
    }

    private static DimensionType registerDim(String name, int id) {
        DimensionType type = DimensionType.register(name, "_" + name, id, SpawnWorldProvider.class, true);
        DimensionManager.registerDimension(type.getId(), type);

        return type;
    }
}
