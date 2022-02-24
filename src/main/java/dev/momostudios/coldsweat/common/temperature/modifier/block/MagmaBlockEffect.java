package dev.momostudios.coldsweat.common.temperature.modifier.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import dev.momostudios.coldsweat.util.CSMath;
import dev.momostudios.coldsweat.util.Units;

public class MagmaBlockEffect extends BlockEffect
{

    @Override
    public double getTemperature(PlayerEntity player, BlockState state, BlockPos pos, double distance)
    {
        return CSMath.blend(0.2, 0, distance, 0.5, 3);
    }

    @Override
    public boolean hasBlock(BlockState block)
    {
        return block.getBlock() == net.minecraft.block.Blocks.MAGMA_BLOCK;
    }

    @Override
    public double maxEffect()
    {
        return CSMath.convertUnits(30, Units.F, Units.MC, false);
    }

    @Override
    public double maxTemperature() {
        return CSMath.convertUnits(800, Units.F, Units.MC, true);
    }
}