package net.mcreator.enhancedenchanting.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.procedures.ObsidianGrindStoneOnBlockRightclickedProcedure;

import com.google.common.collect.ImmutableMap;

public class ObsidianGrindStoneBlock extends Block {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<AttachFace> FACE = FaceAttachedHorizontalDirectionalBlock.FACE;
	private final ImmutableMap<BlockState, VoxelShape> shapes = this.makeShapes();

	public ObsidianGrindStoneBlock() {
		super(BlockBehaviour.Properties.of().strength(10f, 10000000f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
	}

	private ImmutableMap<BlockState, VoxelShape> makeShapes() {
		return this.getShapeForEachState(state -> {
			return switch (state.getValue(FACING)) {
				case NORTH -> switch (state.getValue(FACE)) {
					case FLOOR -> Shapes.or(box(12, 0, 6, 14, 7, 10), box(2, 0, 6, 4, 7, 10), box(12, 7, 5, 14, 13, 11), box(2, 7, 5, 4, 13, 11), box(4, 4, 2, 12, 16, 14));
					case WALL -> Shapes.or(box(12, 6, 9, 14, 10, 16), box(2, 6, 9, 4, 10, 16), box(12, 5, 3, 14, 11, 9), box(2, 5, 3, 4, 11, 9), box(4, 2, 0, 12, 14, 12));
					case CEILING -> Shapes.or(box(2, 9, 6, 4, 16, 10), box(12, 9, 6, 14, 16, 10), box(2, 3, 5, 4, 9, 11), box(12, 3, 5, 14, 9, 11), box(4, 0, 2, 12, 12, 14));
				};
				case EAST -> switch (state.getValue(FACE)) {
					case FLOOR -> Shapes.or(box(6, 0, 12, 10, 7, 14), box(6, 0, 2, 10, 7, 4), box(5, 7, 12, 11, 13, 14), box(5, 7, 2, 11, 13, 4), box(2, 4, 4, 14, 16, 12));
					case WALL -> Shapes.or(box(0, 6, 12, 7, 10, 14), box(0, 6, 2, 7, 10, 4), box(7, 5, 12, 13, 11, 14), box(7, 5, 2, 13, 11, 4), box(4, 2, 4, 16, 14, 12));
					case CEILING -> Shapes.or(box(6, 9, 2, 10, 16, 4), box(6, 9, 12, 10, 16, 14), box(5, 3, 2, 11, 9, 4), box(5, 3, 12, 11, 9, 14), box(2, 0, 4, 14, 12, 12));
				};
				case WEST -> switch (state.getValue(FACE)) {
					case FLOOR -> Shapes.or(box(6, 0, 2, 10, 7, 4), box(6, 0, 12, 10, 7, 14), box(5, 7, 2, 11, 13, 4), box(5, 7, 12, 11, 13, 14), box(2, 4, 4, 14, 16, 12));
					case WALL -> Shapes.or(box(9, 6, 2, 16, 10, 4), box(9, 6, 12, 16, 10, 14), box(3, 5, 2, 9, 11, 4), box(3, 5, 12, 9, 11, 14), box(0, 2, 4, 12, 14, 12));
					case CEILING -> Shapes.or(box(6, 9, 12, 10, 16, 14), box(6, 9, 2, 10, 16, 4), box(5, 3, 12, 11, 9, 14), box(5, 3, 2, 11, 9, 4), box(2, 0, 4, 14, 12, 12));
				};
				default -> switch (state.getValue(FACE)) {
					case FLOOR -> Shapes.or(box(2, 0, 6, 4, 7, 10), box(12, 0, 6, 14, 7, 10), box(2, 7, 5, 4, 13, 11), box(12, 7, 5, 14, 13, 11), box(4, 4, 2, 12, 16, 14));
					case WALL -> Shapes.or(box(2, 6, 0, 4, 10, 7), box(12, 6, 0, 14, 10, 7), box(2, 5, 7, 4, 11, 13), box(12, 5, 7, 14, 11, 13), box(4, 2, 4, 12, 14, 16));
					case CEILING -> Shapes.or(box(12, 9, 6, 14, 16, 10), box(2, 9, 6, 4, 16, 10), box(12, 3, 5, 14, 9, 11), box(2, 3, 5, 4, 9, 11), box(4, 0, 2, 12, 12, 14));
				};
			};
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
		builder.add(FACING, FACE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		if (context.getClickedFace().getAxis() == Direction.Axis.Y)
			return state.setValue(FACE, context.getClickedFace().getOpposite() == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection());
		return state.setValue(FACE, AttachFace.WALL).setValue(FACING, context.getClickedFace());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
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
		ObsidianGrindStoneOnBlockRightclickedProcedure.execute(world, x, y, z, entity);
		return InteractionResult.SUCCESS;
	}
}