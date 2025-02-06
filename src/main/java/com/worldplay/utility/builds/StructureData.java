package com.worldplay.utility.builds;

import net.minecraft.util.math.Vec3d;

public class StructureData {
    public boolean visibleLineBox = false;
    public String nameStructure = "";
    public Vec3d posStructure;
    public int levelStructure;

    public StructureData(Vec3d vec3d, String nameStructure, boolean visibleLineBox) {
        this.posStructure = vec3d;
        this.nameStructure = nameStructure;
        char lastChar = nameStructure.charAt(nameStructure.length() - 1);
        this.levelStructure = Character.getNumericValue(lastChar);
        this.visibleLineBox = visibleLineBox;
        System.out.println(" Обэкт: " + nameStructure + " Уровень " + levelStructure + " Pos: " + vec3d);
    }
}
