package me.zero.twentywfourteeninfinite.registry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

public class AntBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AntBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public void tick(@NotNull BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, @NotNull RandomSource random) {
        BlockState cdt6 = serverLevel.getBlockState(blockPos.below());
        if (cdt6.getBlock() == Blocks.WHITE_CONCRETE) {
            this.move(blockState, serverLevel, blockPos, AntBlock.Step.CW);
        } else if (cdt6.getBlock() == Blocks.BLACK_CONCRETE) {
            this.move(blockState, serverLevel, blockPos, AntBlock.Step.CCW);
        }

    }

    private void move(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, AntBlock.Step step) {
        Direction ft6 = blockState.getValue(FACING);
        Direction ft7 = step == AntBlock.Step.CW ? ft6.getClockWise() : ft6.getCounterClockWise();
        BlockPos fo8 = blockPos.relative(ft7);
        if (serverLevel.isLoaded(fo8)) {
            switch (step) {
                case CW -> {
                    serverLevel.setBlock(blockPos.below(), Blocks.BLACK_CONCRETE.defaultBlockState(), 19);
                    serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                    serverLevel.setBlock(fo8, blockState.setValue(FACING, ft7), 3);
                }
                case CCW -> {
                    serverLevel.setBlock(blockPos.below(), Blocks.WHITE_CONCRETE.defaultBlockState(), 19);
                    serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                    serverLevel.setBlock(fo8, blockState.setValue(FACING, ft7), 3);
                }
            }
        }

    }

    public BlockState updateShape(@NotNull BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState1, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        return super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos2);
    }

    public void onPlace(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState1, boolean b) {
        level.scheduleTick(blockPos, this, 1);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    enum Step {
        CW,
        CCW;
    }
}
