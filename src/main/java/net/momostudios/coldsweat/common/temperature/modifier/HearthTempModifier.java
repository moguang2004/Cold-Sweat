package net.momostudios.coldsweat.common.temperature.modifier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.momostudios.coldsweat.common.temperature.Temperature;
import net.momostudios.coldsweat.config.ColdSweatConfig;
import net.momostudios.coldsweat.core.util.ModEffects;

public class HearthTempModifier extends TempModifier implements IForgeRegistryEntry<TempModifier>
{
    @Override
    public double calculate(Temperature temp, PlayerEntity player)
    {
        ColdSweatConfig config = ColdSweatConfig.getInstance();

        double min = config.minHabitable();
        double max = config.maxHabitable();
        double mid = (min + max) / 2;

        int hearthEffect = player.isPotionActive(ModEffects.INSULATION) ?
                player.getActivePotionEffect(ModEffects.INSULATION).getAmplifier() : 0;

        return mid + ((temp.get() - mid) / (hearthEffect * 2));
    }

    public String getID()
    {
        return "cold_sweat:hearth_insulation";
    }
}