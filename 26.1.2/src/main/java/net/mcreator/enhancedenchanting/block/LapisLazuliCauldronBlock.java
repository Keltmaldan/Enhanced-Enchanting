package net.mcreator.enhancedenchanting.block;

import net.mcreator.enhancedenchanting.procedures.LapisLazuliCauldronEntityFallsOnTheBlockinitProcedure;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LapisLazuliCauldronBlock extends Block {
    public static final IntegerProperty LAPIS = IntegerProperty.create("lapis", 0, 64);
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);

    private static final int REQUIRED_LAPIS = 64;
    private static final int CHECK_INTERVAL = 5;

    private static final VoxelShape SHAPE = Shapes.or(
        box(0, 3, 0, 2, 16, 16),
        box(2, 3, 2, 14, 4, 14),
        box(14, 3, 0, 16, 16, 16),
        box(2, 3, 0, 14, 16, 2),
        box(2, 3, 14, 14, 16, 16),
        box(0, 0, 0, 4, 3, 2),
        box(0, 0, 2, 2, 3, 4),
        box(12, 0, 0, 16, 3, 2),
        box(14, 0, 2, 16, 3, 4),
        box(0, 0, 14, 4, 3, 16),
        box(0, 0, 12, 2, 3, 14),
        box(12, 0, 14, 16, 3, 16),
        box(14, 0, 12, 16, 3, 14)
    );

    public LapisLazuliCauldronBlock(BlockBehaviour.Properties properties) {
    super(properties
    	.strength(2.0f, 10.0f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((state, level, pos) -> false));
    this.registerDefaultState(this.stateDefinition.any()
    	.setValue(LAPIS, 0).setValue(BLOCKSTATE, 0));
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LAPIS, BLOCKSTATE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
    
	@Override
	protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

    	if (!held.is(Items.LAPIS_LAZULI)) {
        	return InteractionResult.TRY_WITH_EMPTY_HAND;
    	}
    
    	if (state.getValue(BLOCKSTATE) == 1) {
	        return InteractionResult.SUCCESS;
    	}

    	if (!level.isClientSide()) {
        	if (!player.getAbilities().instabuild) {
            	held.shrink(1);
        	}
        	level.setBlock(pos, state.setValue(LAPIS, 1).setValue(BLOCKSTATE, 1), 3);
    	}
    	return InteractionResult.SUCCESS;
	}

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, level, pos, previous, moving);
        if (!level.isClientSide() && state.getValue(LAPIS) == REQUIRED_LAPIS) {
            level.scheduleTick(pos, this, CHECK_INTERVAL);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (state.getValue(LAPIS) != REQUIRED_LAPIS) return;

        AABB inside = new AABB(
            pos.getX() + 0.125, pos.getY() + 0.18, pos.getZ() + 0.125,
            pos.getX() + 0.875, pos.getY() + 1.16, pos.getZ() + 0.875
        );
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, inside)) {
            LapisLazuliCauldronEntityFallsOnTheBlockinitProcedure.execute(
                level, pos.getX(), pos.getY(), pos.getZ(), item);
            if (level.getBlockState(pos).getValue(LAPIS) != REQUIRED_LAPIS) break;
        }
        
        if (level.getBlockState(pos).is(this)
                && level.getBlockState(pos).getValue(LAPIS) == REQUIRED_LAPIS) {
            level.scheduleTick(pos, this, CHECK_INTERVAL);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double distance) {
        super.fallOn(level, state, pos, entity, distance);
        if (!level.isClientSide()) {
            LapisLazuliCauldronEntityFallsOnTheBlockinitProcedure.execute(
                level, pos.getX(), pos.getY(), pos.getZ(), entity);
        }
    }
}