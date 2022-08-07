package me.zero.twentywfourteeninfinite.registry.block;

import blue.endless.jankson.annotation.Nullable;
import com.mojang.math.Vector3f;
import me.zero.twentywfourteeninfinite.registry.block.entity.NeitherPortalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NeitherPortalBlock extends PortalBlock implements EntityBlock {
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();

    public NeitherPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected ParticleOptions getParticleType(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity cbt5 = level.getBlockEntity(blockPos);
        if (cbt5 instanceof NeitherPortalEntity) {
            int integer6 = ((NeitherPortalEntity) cbt5).getDimension();
            Vec3 ddp7 = Vec3.fromRGB24(integer6);
            double double8 = 1.0D + (double) (integer6 >> 16 & 255) / 255.0D;
            return new DustParticleOptions(new Vector3f((float) ddp7.x, (float) ddp7.y, (float) ddp7.z), (float) double8);
        } else {
            return super.getParticleType(blockState, level, blockPos);
        }
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NeitherPortalEntity(blockPos, blockState, Math.abs(RANDOM_SOURCE.nextInt()));
    }
}
