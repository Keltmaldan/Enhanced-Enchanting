package net.mcreator.enhancedenchanting.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.procedures.LapisLazuliCauldronOnTickUpdateProcedure;
import net.mcreator.enhancedenchanting.procedures.LapisLazuliCauldronOnBlockRightclickedProcedure;
import net.mcreator.enhancedenchanting.procedures.LapisLazuliCauldronEntityFallsOnTheBlockinitProcedure;

import com.google.common.collect.ImmutableMap;

public class LapisLazuliCauldronBlock extends Block {
	public static final IntegerProperty LAPIS = IntegerProperty.create("lapis", 0, 64);
	public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);
	private final ImmutableMap<BlockState, VoxelShape> shapes = this.makeShapes();

	public LapisLazuliCauldronBlock() {
		super(BlockBehaviour.Properties.of().strength(2f, 10f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(LAPIS, 0).setValue(BLOCKSTATE, 0));
	}

	private ImmutableMap<BlockState, VoxelShape> makeShapes() {
		return this.getShapeForEachState(state -> {
			if (state.getValue(BLOCKSTATE) == 1) {
				return Shapes.or(box(0, 3, 0, 2, 16, 16), box(2, 3, 2, 14, 4, 14), box(14, 3, 0, 16, 16, 16), box(2, 3, 0, 14, 16, 2), box(2, 3, 14, 14, 16, 16), box(0, 0, 0, 4, 3, 2), box(0, 0, 2, 2, 3, 4), box(12, 0, 0, 16, 3, 2),
						box(14, 0, 2, 16, 3, 4), box(0, 0, 14, 4, 3, 16), box(0, 0, 12, 2, 3, 14), box(12, 0, 14, 16, 3, 16), box(14, 0, 12, 16, 3, 14));
			}
			return Shapes.or(box(0, 3, 0, 2, 16, 16), box(2, 3, 2, 14, 4, 14), box(14, 3, 0, 16, 16, 16), box(2, 3, 0, 14, 16, 2), box(2, 3, 14, 14, 16, 16), box(0, 0, 0, 4, 3, 2), box(0, 0, 2, 2, 3, 4), box(12, 0, 0, 16, 3, 2),
					box(14, 0, 2, 16, 3, 4), box(0, 0, 14, 4, 3, 16), box(0, 0, 12, 2, 3, 14), box(12, 0, 14, 16, 3, 16), box(14, 0, 12, 16, 3, 14));
		});
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return shapes.get(state);
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LAPIS, BLOCKSTATE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(LAPIS, 0).setValue(BLOCKSTATE, 0);
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		LapisLazuliCauldronOnTickUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), blockstate);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public void fallOn(Level world, BlockState blockstate, BlockPos pos, Entity entity, float distance) {
		super.fallOn(world, blockstate, pos, entity, distance);
		LapisLazuliCauldronEntityFallsOnTheBlockinitProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double hitX = hit.getLocation().x;
		double hitY = hit.getLocation().y;
		double hitZ = hit.getLocation().z;
		Direction direction = hit.getDirection();
		LapisLazuliCauldronOnBlockRightclickedProcedure.execute(world, x, y, z, blockstate, entity);
		return InteractionResult.SUCCESS;
	}
}