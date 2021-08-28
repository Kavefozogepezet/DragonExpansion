package com.kavefozogepezet.dragonexpansion.common.tiles;

import com.kavefozogepezet.dragonexpansion.common.blocks.RideableDragonEgg;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import com.kavefozogepezet.dragonexpansion.core.init.EntityTypeInit;
import com.kavefozogepezet.dragonexpansion.core.init.TileEntityTypeInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class RideableDragonEggTE extends TileEntity implements ITickableTileEntity {

    public RideableDragonEggTE(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    public RideableDragonEggTE() {
        super(TileEntityTypeInit.RIDEABLE_DRAGON_EGG_TE.get());
    }

    public int burnTime = 0;
    private final int maxHatchTime = 960; // 24000
    private int hatchTime = maxHatchTime;

    public UUID parent;

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if(this.isBurning()){
            burnTime--;
        }

        if(this.level != null && !this.level.isClientSide){
            if(flag) {
                hatchTime--;
                int hatchProgress = (hatchTime / (maxHatchTime / 19)) + 1;
                if(hatchProgress != this.level.getBlockState(this.worldPosition).getValue(RideableDragonEgg.TICK_PER_PARTICLE)){
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(
                            RideableDragonEgg.TICK_PER_PARTICLE, hatchProgress), 3
                    );
                }
                if (hatchTime == 0) {
                    RideableDragonEntity createdEntity = new RideableDragonEntity(EntityTypeInit.PURPUR_DRAGON.get(), this.level);
                    createdEntity.setAge(-24000);
                    createdEntity.setPos(
                            this.getBlockPos().getX() + 0.5d,
                            this.getBlockPos().getY(),
                            this.getBlockPos().getZ() + 0.5d
                    );

                    Block egg = this.level.getBlockState(this.getBlockPos()).getBlock();
                    this.level.addFreshEntity(createdEntity);

                    this.level.destroyBlock(this.getBlockPos(), false);
                    flag1 = true;
                }
            } else {
                MathHelper.clamp(hatchTime + 24, 0, maxHatchTime);
            }

            if ((flag != this.isBurning()) && !flag1) {
                flag1 = true;
                this.level.setBlock(this.getBlockPos(), this.level.getBlockState(this.getBlockPos()).setValue(RideableDragonEgg.LIT, this.isBurning()), 3);
            }
        }

        if(flag1) {
            this.setChanged();
        }
    }

    private boolean isBurning(){
        return burnTime > 0;
    }

    public void ignite(UUID playerID){
        this.burnTime = 960 + this.level.random.nextInt(480);
        this.parent = playerID;
        if (this.level != null && !this.level.isClientSide) {
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(RideableDragonEgg.LIT, this.isBurning()), 3);
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        if(nbt.hasUUID("Owner")){
            parent = nbt.getUUID("Owner");
        } else {
            parent = null;
        }
        burnTime = nbt.getInt("BurnTime");
        hatchTime = nbt.getInt("HatchTime");

    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);

        if(parent != null){
            compound.putUUID("Owner", parent);
        }
        compound.putInt("BurnTime", burnTime);
        compound.putInt("HatchTime", hatchTime);

        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return new SUpdateTileEntityPacket(this.worldPosition, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.level.getBlockState(this.worldPosition), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.load(state, tag);
    }
}
