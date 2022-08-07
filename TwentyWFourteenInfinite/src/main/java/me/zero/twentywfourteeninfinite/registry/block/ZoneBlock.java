package me.zero.twentywfourteeninfinite.registry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ZoneBlock extends Block {
    public ZoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        double double6 = (double)blockPos.getX() + 0.5D;
        double double8 = (double)blockPos.getZ() + 0.5D;

        for(int integer10 = 0; integer10 < 3; ++integer10) {
            if (randomSource.nextBoolean()) {
                level.addParticle(ParticleTypes.COMPOSTER, double6 + (double)(randomSource.nextFloat() / 5.0F), (double)blockPos.getY() + (0.5D - (double)randomSource.nextFloat()), double8 + (double)(randomSource.nextFloat() / 5.0F), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 3, true, true));
            ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1, true, true));
        }

        if (entity instanceof ItemEntity) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.0D, 2.0D, 0.0D));
        }

        super.entityInside(blockState, level, blockPos, entity);
    }

    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0F;
    }
}
