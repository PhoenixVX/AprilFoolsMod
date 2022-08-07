package me.zero.twentywfourteeninfinite.registry.block.entity;

import blue.endless.jankson.annotation.Nullable;
import me.zero.twentywfourteeninfinite.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NeitherPortalEntity extends BlockEntity {
    private int dimension;

    public NeitherPortalEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.NEITHER, blockPos, blockState);
    }

    public NeitherPortalEntity(BlockPos blockPos, BlockState blockState, int integer) {
        this(blockPos, blockState);
        this.dimension = integer;
    }

    public CompoundTag save(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("Dimension", this.dimension);
        return compoundTag;
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.dimension = compoundTag.getInt("Dimension");
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, ModBlockEntities.NEITHER, this.getUpdateTag());
    }

    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int integer) {
        this.dimension = integer;
    }
}