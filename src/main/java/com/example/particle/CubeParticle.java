package com.example.particle;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3f;

public class CubeParticle {

    ServerWorld world;
    BlockPos pos;
    Vec3i size;




    float scale = 1f;
    DustParticleEffect dustParticleEffect = new DustParticleEffect(16711680    ,scale);

    public CubeParticle(ServerWorld world, Vec3d pos, Vec3i size) {
        this.world = world;
        this.pos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
        this.size = size;


    }

    public void draw() {
        drawLineX(pos.getX(), pos.getY(), pos.getZ());
        drawLineY(pos.getX(), pos.getY(), pos.getZ());
        drawLineZ(pos.getX(), pos.getY(), pos.getZ());

        drawLineX(pos.getX(), pos.getY(), pos.getZ() + size.getZ());
        drawLineY(pos.getX() + size.getX(), pos.getY(), pos.getZ() + size.getZ());
        drawLineZ(pos.getX() + size.getX(), pos.getY(), pos.getZ() );


        drawLineY(pos.getX(), pos.getY(), pos.getZ() + size.getZ());
        drawLineY(pos.getX() + size.getX(), pos.getY(), pos.getZ());

        drawLineX(pos.getX(), pos.getY() + size.getY(), pos.getZ());
        drawLineY(pos.getX(), pos.getY() + size.getY(), pos.getZ());
        drawLineZ(pos.getX(), pos.getY() + size.getY(), pos.getZ());


        drawLineX(pos.getX(), pos.getY()+ size.getY(), pos.getZ() + size.getZ());
        drawLineZ(pos.getX() + size.getX(), pos.getY()+ size.getY(), pos.getZ());
    }



    public void drawLineX(int X, int Y, int Z) {
        for(int x = 0; x <= size.getX(); x++) {
            world.spawnParticles(dustParticleEffect, X + x, Y, Z, 0, 0, 0,0,0);
        }
    }
    public void drawLineY(int X, int Y, int Z) {
        for(int y = 0; y <= size.getY(); y++) {
            world.spawnParticles(dustParticleEffect, X, Y + y, Z, 0, 0, 0,0,0);
        }
    }
    public void drawLineZ(int X, int Y, int Z) {
        for(int z = 0; z <= size.getZ(); z++) {
            world.spawnParticles(dustParticleEffect, X, Y, Z + z, 0, 0, 0,0,0);
        }
    }

}