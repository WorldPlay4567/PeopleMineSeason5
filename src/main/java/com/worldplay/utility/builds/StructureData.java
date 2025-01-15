package com.worldplay.utility.builds;

import net.minecraft.util.math.Vec3d;

public class StructureData {
    boolean visibleLineBox = false;
    String nameStructure = "";
    Vec3d posStructure;
    int levelStructure;

    public StructureData(Vec3d vec3d, String nameStructure, int level) {
        this.posStructure = vec3d;
        this.nameStructure = nameStructure;
        this.levelStructure = level;
    }


}
