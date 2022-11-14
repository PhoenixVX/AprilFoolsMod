package me.zero.twentywfourteeninfinite.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Blocks.class)
public interface InvokerBlocks {
    @Invoker("never")
    static Boolean invokeNever(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        throw new AssertionError();
    }

    @Invoker("always")
    static boolean invokeAlways(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        throw new AssertionError();
    }
}
