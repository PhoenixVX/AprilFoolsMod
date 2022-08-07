package me.zero.twentywfourteeninfinite.registry.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BoxOfBooksBlock extends Block {
    private static final char[] CHARACTERS = new char[]{' ', ',', '.', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BoxOfBooksBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        Direction blockDirection = (Direction)blockState.getValue(FACING);
        int integer9 = blockPos.getY();
        int integer10;
        int integer11;
        switch (blockDirection) {
            case NORTH:
                integer10 = 15 - blockPos.getX() & 15;
                integer11 = 0;
                break;
            case SOUTH:
                integer10 = blockPos.getX() & 15;
                integer11 = 2;
                break;
            case EAST:
                integer10 = 15 - blockPos.getZ() & 15;
                integer11 = 1;
                break;
            case WEST:
            default:
                integer10 = blockPos.getZ() & 15;
                integer11 = 3;
        }

        if (integer10 > 0 && integer10 < 15) {
            ChunkPos chunkPos = new ChunkPos(blockPos);
            String bookName = chunkPos.x + "/" + chunkPos.z + "/" + integer11 + "/" + integer10 + "/" + integer9;
            RandomSource random14 = RandomSource.create((long)chunkPos.x);
            RandomSource random15 = RandomSource.create(((long)chunkPos.z));
            RandomSource random16 = RandomSource.create(((long)((integer10 << 8) + (integer9 << 4) + integer11)));
            ItemStack bin17 = new ItemStack(Items.WRITTEN_BOOK);
            CompoundTag compoundTag = bin17.getOrCreateTag();
            ListTag listTag = new ListTag();

            for(int integer20 = 0; integer20 < 16; ++integer20) {
                StringBuilder stringBuilder21 = new StringBuilder();

                for(int integer22 = 0; integer22 < 128; ++integer22) {
                    int integer23 = random14.nextInt() + random15.nextInt() + -random16.nextInt();
                    stringBuilder21.append(CHARACTERS[Math.floorMod(integer23, CHARACTERS.length)]);
                }

                listTag.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(stringBuilder21.toString()))));
            }

            compoundTag.put("pages", listTag);
            compoundTag.putString("author", ChatFormatting.OBFUSCATED + "Universe itself");
            compoundTag.putString("title", bookName);
            popResource(level, blockPos.relative(blockHitResult.getDirection()), bin17);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder builder) {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction ft3 = blockPlaceContext.getHorizontalDirection().getOpposite();
        return (BlockState)this.defaultBlockState().setValue(FACING, ft3);
    }
}