package net.momostudios.coldsweat.common.temperature.modifier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.momostudios.coldsweat.common.temperature.Temperature;
import net.momostudios.coldsweat.config.ConfigCache;
import net.momostudios.coldsweat.util.CSMath;
import net.momostudios.coldsweat.util.Units;

public class WaterTempModifier extends TempModifier
{
    public WaterTempModifier()
    {
        addArgument("strength", 0.01);
    }

    public WaterTempModifier(double strength)
    {
        addArgument("strength", strength);
    }

    @Override
    public double getResult(Temperature temp, PlayerEntity player)
    {
        double maxTemp = ConfigCache.getInstance().maxTemp;
        double minTemp = ConfigCache.getInstance().minTemp;

        try
        {
            double strength = getArgument("strength", Double.class);
            double factor = Math.min(-0.0003, -0.0003 - (temp.get() / 800));
            double addAmount = player.isInWaterOrBubbleColumn() ? 0.01 : player.world.isRainingAt(player.getPosition()) ? 0.005 : factor;

            setArgument("strength", CSMath.clamp(strength + addAmount, 0, Math.abs(CSMath.average(maxTemp, minTemp) - temp.get()) / 2));

            if (!player.isInWater() && strength > 0.0)
            {
                if (Math.random() < strength / 40.0)
                {
                    double randX = player.getWidth() * (Math.random() - 0.5);
                    double randY = player.getHeight() * Math.random();
                    double randZ = player.getWidth() * (Math.random() - 0.5);
                    player.world.addParticle(ParticleTypes.FALLING_WATER, player.getPosX() + randX, player.getPosY() + randY, player.getPosZ() + randZ, 0, 0, 0);
                }
            }

            return temp.get() - strength;
        }
        // Remove the modifier if an exception is thrown
        catch (Exception e)
        {
            e.printStackTrace();
            args.remove("strength");
            args.put("strength", 1d);
            return temp.get();
        }
    }

    @Override
    public String getID()
    {
        return "cold_sweat:water";
    }
}
