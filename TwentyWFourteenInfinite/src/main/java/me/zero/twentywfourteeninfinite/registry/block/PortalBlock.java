package me.zero.twentywfourteeninfinite.registry.block;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import me.zero.twentywfourteeninfinite.registry.ModBlocks;
import me.zero.twentywfourteeninfinite.registry.block.entity.NeitherPortalEntity;
import me.zero.twentywfourteeninfinite.util.DimHash;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class PortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public PortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public static boolean trySpawnPortal(LevelAccessor levelAccessor, BlockPos blockPos, Block block) {
        PortalBlock.PortalShape portalShape = isPortal(levelAccessor, blockPos, block);
        if (portalShape != null) {
            portalShape.createPortalBlocks();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public static PortalBlock.PortalShape isPortal(LevelAccessor levelAccessor, BlockPos blockPos, Block block) {
        PortalBlock.PortalShape portalShape = new PortalBlock.PortalShape(levelAccessor, blockPos, Direction.Axis.X, block);
        if (portalShape.isValid() && portalShape.numPortalBlocks == 0) {
            return portalShape;
        } else {
            PortalBlock.PortalShape portalShape1 = new PortalBlock.PortalShape(levelAccessor, blockPos, Direction.Axis.Z, block);
            return portalShape1.isValid() && portalShape1.numPortalBlocks == 0 ? portalShape1 : null;
        }
    }

    public static BlockPattern.BlockPatternMatch getPortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Block block) {
        Direction.Axis axis = Direction.Axis.Z;
        PortalBlock.PortalShape portalShape = new PortalBlock.PortalShape(levelAccessor, blockPos, Direction.Axis.X, block);
        LoadingCache loadingCache6 = BlockPattern.createLevelCache(levelAccessor, true);
        if (!portalShape.isValid()) {
            axis = Direction.Axis.X;
            portalShape = new PortalBlock.PortalShape(levelAccessor, blockPos, Direction.Axis.Z, block);
        }

        if (!portalShape.isValid()) {
            return new BlockPattern.BlockPatternMatch(blockPos, Direction.NORTH, Direction.UP, loadingCache6, 1, 1, 1);
        } else {
            int[] arr7 = new int[Direction.AxisDirection.values().length];
            Direction direction = portalShape.rightDir.getCounterClockWise();
            BlockPos blockPos1 = portalShape.bottomLeft.above(portalShape.getHeight() - 1);

            for (Direction.AxisDirection axisDirection : Direction.AxisDirection.values()) {
                BlockPattern.BlockPatternMatch blockPatternMatch = new BlockPattern.BlockPatternMatch(direction.getAxisDirection() == axisDirection ? blockPos1 : blockPos1.relative(portalShape.rightDir, portalShape.getWidth() - 1), Direction.get(axisDirection, axis), Direction.UP, loadingCache6, portalShape.getWidth(), portalShape.getHeight(), 1);

                for (int integer15 = 0; integer15 < portalShape.getWidth(); ++integer15) {
                    for (int integer16 = 0; integer16 < portalShape.getHeight(); ++integer16) {
                        BlockInWorld cdx17 = blockPatternMatch.getBlock(integer15, integer16, 1);
                        if (!cdx17.getState().isAir()) {
                            ++arr7[axisDirection.ordinal()];
                        }
                    }
                }
            }

            Direction.AxisDirection axisDirection = Direction.AxisDirection.POSITIVE;

            for (Direction.AxisDirection axisDirection1 : Direction.AxisDirection.values()) {
                if (arr7[axisDirection1.ordinal()] < arr7[axisDirection.ordinal()]) {
                    axisDirection = axisDirection1;
                }
            }

            return new BlockPattern.BlockPatternMatch(direction.getAxisDirection() == axisDirection ? blockPos1 : blockPos1.relative(portalShape.rightDir, portalShape.getWidth() - 1), Direction.get(axisDirection, axis), Direction.UP, loadingCache6, portalShape.getWidth(), portalShape.getHeight(), 1);
        }
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch ((Direction.Axis) blockState.getValue(AXIS)) {
            case Z:
                return Z_AXIS_AABB;
            case X:
            default:
                return X_AXIS_AABB;
        }
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis axis1 = (Direction.Axis) blockState.getValue(AXIS);
        boolean boolean10 = axis1 != axis && axis.isHorizontal();
        return !boolean10 && blockState1.getBlock() != this && !(new PortalBlock.PortalShape(levelAccessor, blockPos, axis1, this)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
    }

    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Entity entity) {
        if (entity instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity) entity).getItem();
            if (itemStack.getItem() == Items.WRITTEN_BOOK || itemStack.getItem() == Items.WRITABLE_BOOK) {
                BookViewScreen.BookAccess bookAccess = BookViewScreen.BookAccess.fromItem(itemStack);
                String string8 = IntStream.range(0, bookAccess.getPageCount()).mapToObj(bookAccess::getPage).map(FormattedText::toString).collect(Collectors.joining("\n"));
                if (!string8.isEmpty()) {
                    int integer9 = DimHash.getHash(string8);
                    this.floodFillReplace(level, blockPos, blockState, integer9);
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }

                return;
            }
        }

        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
            entity.handleInsidePortal(blockPos);
        }

    }

    private void floodFillReplace(Level level, BlockPos blockPos, BlockState blockState, int integer) {
        Set set6 = Sets.newHashSet();
        Queue queue7 = Queues.newArrayDeque();
        Direction.Axis axis = (Direction.Axis) blockState.getValue(AXIS);
        BlockState blockState1 = ModBlocks.NEITHER_PORTAL.defaultBlockState().setValue(AXIS, axis);
        Direction direction;
        Direction direction1;
        switch (axis) {
            case Z:
            default:
                direction = Direction.UP;
                direction1 = Direction.SOUTH;
                break;
            case X:
                direction = Direction.UP;
                direction1 = Direction.EAST;
                break;
            case Y:
                direction = Direction.SOUTH;
                direction1 = Direction.EAST;
        }

        Direction directionOpposite = direction.getOpposite();
        Direction direction1Opposite = direction1.getOpposite();
        queue7.add(blockPos);

        BlockPos blockPos1;
        while ((blockPos1 = (BlockPos) queue7.poll()) != null) {
            set6.add(blockPos1);
            BlockState blockState2 = level.getBlockState(blockPos1);
            if (blockState2 == blockState) {
                level.setBlock(blockPos1, blockState1, 18);
                BlockEntity blockEntity = level.getBlockEntity(blockPos1);
                if (blockEntity instanceof NeitherPortalEntity) {
                    ((NeitherPortalEntity) blockEntity).setDimension(integer);
                }

                BlockPos blockPos2 = blockPos1.relative(direction);
                if (!set6.contains(blockPos2)) {
                    queue7.add(blockPos2);
                }

                blockPos2 = blockPos1.relative(directionOpposite);
                if (!set6.contains(blockPos2)) {
                    queue7.add(blockPos2);
                }

                blockPos2 = blockPos1.relative(direction1);
                if (!set6.contains(blockPos2)) {
                    queue7.add(blockPos2);
                }

                blockPos2 = blockPos1.relative(direction1Opposite);
                if (!set6.contains(blockPos2)) {
                    queue7.add(blockPos2);
                }
            }
        }

    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(100) == 0) {
            level.playLocalSound((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, randomSource.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int integer6 = 0; integer6 < 4; ++integer6) {
            double double7 = (double) blockPos.getX() + (double) randomSource.nextFloat();
            double double9 = (double) blockPos.getY() + (double) randomSource.nextFloat();
            double double11 = (double) blockPos.getZ() + (double) randomSource.nextFloat();
            double double13 = ((double) randomSource.nextFloat() - 0.5D) * 0.5D;
            double double15 = ((double) randomSource.nextFloat() - 0.5D) * 0.5D;
            double double17 = ((double) randomSource.nextFloat() - 0.5D) * 0.5D;
            int integer19 = randomSource.nextInt(2) * 2 - 1;
            if (level.getBlockState(blockPos.west()).getBlock() != this && level.getBlockState(blockPos.east()).getBlock() != this) {
                double7 = (double) blockPos.getX() + 0.5D + 0.25D * (double) integer19;
                double13 = (randomSource.nextFloat() * 2.0F * (float) integer19);
            } else {
                double11 = (double) blockPos.getZ() + 0.5D + 0.25D * (double) integer19;
                double17 = (randomSource.nextFloat() * 2.0F * (float) integer19);
            }

            level.addParticle(this.getParticleType(blockState, level, blockPos), double7, double9, double11, double13, double15, double17);
        }

    }

    protected ParticleOptions getParticleType(BlockState blockState, Level level, BlockPos blockPos) {
        return ParticleTypes.PORTAL;
    }

    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return ItemStack.EMPTY;
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis) blockState.getValue(AXIS)) {
                    case Z:
                        return blockState.setValue(AXIS, Direction.Axis.X);
                    case X:
                        return blockState.setValue(AXIS, Direction.Axis.Z);
                    default:
                        return blockState;
                }
            default:
                return blockState;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder builder) {
        builder.add(AXIS);
    }

    public static class PortalShape {
        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final Direction leftDir;
        private final Block portalBlock;
        private int numPortalBlocks;
        @Nullable
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public PortalShape(LevelAccessor levelAccessor, BlockPos blockPos, Direction.Axis axis, Block block) {
            this.level = levelAccessor;
            this.axis = axis;
            this.portalBlock = block;
            if (axis == Direction.Axis.X) {
                this.leftDir = Direction.EAST;
                this.rightDir = Direction.WEST;
            } else {
                this.leftDir = Direction.NORTH;
                this.rightDir = Direction.SOUTH;
            }

            for (BlockPos blockPos1 = blockPos; blockPos.getY() > blockPos1.getY() - 21 && blockPos.getY() > 0 && this.isEmpty(levelAccessor.getBlockState(blockPos.below())); blockPos = blockPos.below()) {
            }

            int integer7 = this.getDistanceUntilEdge(blockPos, this.leftDir) - 1;
            if (integer7 >= 0) {
                this.bottomLeft = blockPos.relative(this.leftDir, integer7);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }

        }

        protected int getDistanceUntilEdge(BlockPos blockPos, Direction direction) {
            int integer4;
            for (integer4 = 0; integer4 < 22; ++integer4) {
                BlockPos blockPos1 = blockPos.relative(direction, integer4);
                if (!this.isEmpty(this.level.getBlockState(blockPos1)) || this.level.getBlockState(blockPos1.below()).getBlock() != Blocks.OBSIDIAN) {
                    break;
                }
            }

            Block block = this.level.getBlockState(blockPos.relative(direction, integer4)).getBlock();
            return block == Blocks.OBSIDIAN ? integer4 : 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        protected int calculatePortalHeight() {
            label56:
            for (this.height = 0; this.height < 21; ++this.height) {
                for (int integer2 = 0; integer2 < this.width; ++integer2) {
                    BlockPos blockPos = this.bottomLeft.relative(this.rightDir, integer2).above(this.height);
                    BlockState blockState = this.level.getBlockState(blockPos);
                    if (!this.isEmpty(blockState)) {
                        break label56;
                    }

                    Block block = blockState.getBlock();
                    if (block == this.portalBlock) {
                        ++this.numPortalBlocks;
                    }

                    if (integer2 == 0) {
                        block = this.level.getBlockState(blockPos.relative(this.leftDir)).getBlock();
                        if (block != Blocks.OBSIDIAN) {
                            break label56;
                        }
                    } else if (integer2 == this.width - 1) {
                        block = this.level.getBlockState(blockPos.relative(this.rightDir)).getBlock();
                        if (block != Blocks.OBSIDIAN) {
                            break label56;
                        }
                    }
                }
            }

            for (int integer2 = 0; integer2 < this.width; ++integer2) {
                if (this.level.getBlockState(this.bottomLeft.relative(this.rightDir, integer2).above(this.height)).getBlock() != Blocks.OBSIDIAN) {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            } else {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean isEmpty(BlockState blockState) {
            Block btu3 = blockState.getBlock();
            return blockState.isAir() || blockState.is(BlockTags.FIRE) || btu3 == this.portalBlock;
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void createPortalBlocks() {
            for (int integer2 = 0; integer2 < this.width; ++integer2) {
                BlockPos blockPos = this.bottomLeft.relative(this.rightDir, integer2);

                for (int integer4 = 0; integer4 < this.height; ++integer4) {
                    this.level.setBlock(blockPos.above(integer4), (BlockState) this.portalBlock.defaultBlockState().setValue(PortalBlock.AXIS, this.axis), 18);
                }
            }

        }

        private boolean hasAllPortalBlocks() {
            return this.numPortalBlocks >= this.width * this.height;
        }

        public boolean isComplete() {
            return this.isValid() && this.hasAllPortalBlocks();
        }
    }
}
