package com.kavefozogepezet.dragonexpansion.common.blocks;

import com.kavefozogepezet.dragonexpansion.common.tiles.RideableDragonEggTE;
import com.kavefozogepezet.dragonexpansion.core.init.TileEntityTypeInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class RideableDragonEgg extends Block {

    public static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 11, 13);
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final IntegerProperty TICK_PER_PARTICLE = IntegerProperty.create("tickperparticle", 1, 20);

    private boolean wasBurning = false;
    private int skippedParticleTicks = 0;

    public RideableDragonEgg() {
        super(Properties.of(Material.STONE)
                .strength(50.0f, 1200f)
                .harvestLevel(3)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
        );
        this.registerDefaultState(
                (BlockState)((BlockState)((BlockState)this.stateDefinition.any())
                        .setValue(LIT, false))
                        .setValue(TICK_PER_PARTICLE, 20)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
        builder.add(LIT, TICK_PER_PARTICLE);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(LIT) ? 15: 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypeInit.RIDEABLE_DRAGON_EGG_TE.get().create();
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(stateIn.getValue(LIT)){
            skippedParticleTicks++;

            if (rand.nextInt(8) == 0) {
                worldIn.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }
            double spx = (double)pos.getX() + rand.nextDouble();
            double spy = (double)(pos.getY() + 0.3F) - rand.nextDouble() * (double)0.1F;
            double spz = (double)pos.getZ() + rand.nextDouble();
            worldIn.addParticle(ParticleTypes.LARGE_SMOKE, spx, spy, spz, 0.0D, 0.0D, 0.0D);

            if(skippedParticleTicks >= stateIn.getValue(TICK_PER_PARTICLE)){
                skippedParticleTicks = 0;
                double fpangle = rand.nextDouble() * Math.PI * 2.0D;
                double fpx = (double)pos.getX() + Math.sin(fpangle) * 0.45D + 0.5D;
                double fpy = (double)pos.getY() + rand.nextDouble();
                double fpz = (double)pos.getZ() + Math.cos(fpangle) * 0.45D + 0.5D;
                worldIn.addParticle(ParticleTypes.FLAME, fpx, fpy, fpz, 0.0D, 0.0D, 0.0D);
            }
        }

        if(wasBurning != stateIn.getValue(LIT)) {
            if (stateIn.getValue(LIT)) {
                worldIn.playLocalSound(
                        (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
                        SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            } else {
                worldIn.playLocalSound(
                        (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
                        SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
            wasBurning = stateIn.getValue(LIT);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerentity, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn != null && !worldIn.isClientSide){
            TileEntity tile = worldIn.getBlockEntity(pos);
            ItemStack heldItem = playerentity.getItemInHand(handIn);
            if(tile instanceof RideableDragonEggTE && heldItem.getItem() == Items.FLINT_AND_STEEL) {
                ((RideableDragonEggTE) tile).ignite(false);
                heldItem.hurtAndBreak(1, playerentity, (player) -> {
                    player.broadcastBreakEvent(handIn);
                });
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.fireImmune() && state.getValue(LIT)) {
            entityIn.setRemainingFireTicks(entityIn.getRemainingFireTicks() + 1);
            if (entityIn.getRemainingFireTicks() == 0) {
                entityIn.setSecondsOnFire(8);
            }
            entityIn.hurt(DamageSource.IN_FIRE, 1.0F);
        }
        super.entityInside(state, worldIn, pos, entityIn);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
    }
}
