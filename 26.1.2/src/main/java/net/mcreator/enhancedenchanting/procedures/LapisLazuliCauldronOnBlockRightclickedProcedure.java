package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.block.LapisLazuliCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class LapisLazuliCauldronOnBlockRightclickedProcedure {
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        if (!(state.getBlock() instanceof LapisLazuliCauldronBlock)) return;
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.LAPIS_LAZULI) || stack.getCount() != 64) return;
        if (state.getValue(LapisLazuliCauldronBlock.LAPIS) != 0) return;

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        if (!event.getLevel().isClientSide()) {
            event.getLevel().setBlock(pos,
                state.setValue(LapisLazuliCauldronBlock.LAPIS, 64), 3);
            if (!event.getEntity().getAbilities().instabuild) {
                stack.shrink(64);
            }
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z,
                               BlockState state, Entity entity) {
        if (!(entity instanceof Player player)) return;
        BlockPos pos = BlockPos.containing(x, y, z);
        if (!(state.getBlock() instanceof LapisLazuliCauldronBlock)) return;
        ItemStack stack = player.getMainHandItem();
        if (stack.is(Items.LAPIS_LAZULI) && stack.getCount() == 64
                && state.getValue(LapisLazuliCauldronBlock.LAPIS) == 0
                && !player.level().isClientSide()) {
            world.setBlock(pos, state.setValue(LapisLazuliCauldronBlock.LAPIS, 64), 3);
            if (!player.getAbilities().instabuild) stack.shrink(64);
        }
    }
}