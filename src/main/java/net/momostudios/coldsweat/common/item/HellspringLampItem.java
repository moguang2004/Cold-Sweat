package net.momostudios.coldsweat.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.momostudios.coldsweat.common.temperature.modifier.HellLampTempModifier;
import net.momostudios.coldsweat.config.ConfigCache;
import net.momostudios.coldsweat.core.itemgroup.ColdSweatGroup;
import net.momostudios.coldsweat.core.network.ColdSweatPacketHandler;
import net.momostudios.coldsweat.core.network.message.PlaySoundMessage;
import net.momostudios.coldsweat.util.CSMath;
import net.momostudios.coldsweat.util.PlayerHelper;

public class HellspringLampItem extends Item
{
    public HellspringLampItem()
    {
        super(new Properties().group(ColdSweatGroup.COLD_SWEAT).maxStackSize(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entityIn;
            double max = ConfigCache.getInstance().maxTemp;
            double temp = PlayerHelper.hasModifier(player, HellLampTempModifier.class, PlayerHelper.Types.AMBIENT) ?
                    player.getPersistentData().getDouble("preLampTemp") : PlayerHelper.getTemperature(player, PlayerHelper.Types.AMBIENT).get();

            // Fuel the item on creation
            if (!stack.getOrCreateTag().getBoolean("hasTicked"))
            {
                stack.getOrCreateTag().putBoolean("hasTicked", true);
                setFuel(stack, 64);
            }

            if ((isSelected || player.getHeldItemOffhand() == stack) && player.world.getDimensionKey().getLocation().getPath().equals("the_nether") && temp > max)
            {
                if (getFuel(stack) > 0)
                {
                    // Drain fuel
                    if (player.ticksExisted % 10 == 0 && !(player.isCreative() || player.isSpectator()))
                        addFuel(stack, -0.02f * (float) CSMath.clamp(temp - ConfigCache.getInstance().maxTemp, 1, 3));

                    // Give effect to nearby players
                    AxisAlignedBB bb = new AxisAlignedBB(player.getPosX() - 2, player.getPosY() - 2, player.getPosZ() - 2, player.getPosX() + 2, player.getPosY() + 2, player.getPosZ() + 2);
                    worldIn.getEntitiesWithinAABB(PlayerEntity.class, bb).forEach(e ->
                    {
                        PlayerHelper.addModifier(e, new HellLampTempModifier(), PlayerHelper.Types.AMBIENT, false);

                        e.getPersistentData().putInt("soulLampTimeout", 5);
                    });
                }
            }

            // Handle state changes & sounds
            if (stack.getOrCreateTag().getInt("stateChangeTimer") > 0)
            {
                stack.getOrCreateTag().putInt("stateChangeTimer", stack.getOrCreateTag().getInt("stateChangeTimer") - 1);
            }

            if (stack.getOrCreateTag().getInt("fuel") > 0 && player.world.getDimensionKey().getLocation().getPath().equals("the_nether") && temp > max &&
            (isSelected || player.getHeldItemOffhand() == stack))
            {
                if (stack.getOrCreateTag().getInt("stateChangeTimer") <= 0 && !stack.getOrCreateTag().getBoolean("isOn"))
                {
                    if (!worldIn.isRemote)
                    {
                        stack.getOrCreateTag().putInt("stateChangeTimer", 10);
                        stack.getOrCreateTag().putBoolean("isOn", true);
                    }

                    // In case the player is on a server
                    if (!worldIn.isRemote)
                    {
                        ColdSweatPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new PlaySoundMessage(1, 1.5f, (float) Math.random() / 5f + 0.9f, player.getUniqueID()));
                    }
                }
            }
            else
            {
                if (stack.getOrCreateTag().getInt("stateChangeTimer") <= 0 && stack.getOrCreateTag().getBoolean("isOn"))
                {
                    if (!worldIn.isRemote)
                    {
                        stack.getOrCreateTag().putInt("stateChangeTimer", 10);
                        stack.getOrCreateTag().putBoolean("isOn", false);
                    }

                    if (getFuel(stack) < 0.5)
                        setFuel(stack, 0);

                    // In case the player is on a server
                    if (!worldIn.isRemote)
                    {
                        ColdSweatPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new PlaySoundMessage(2, 1.5f, (float) Math.random() / 5f + 0.9f, player.getUniqueID()));
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    private void setFuel(ItemStack stack, float fuel)
    {
        stack.getOrCreateTag().putFloat("fuel", fuel);
    }
    private void addFuel(ItemStack stack, float fuel)
    {
        setFuel(stack, getFuel(stack) + fuel);
    }
    private float getFuel(ItemStack stack)
    {
        return stack.getOrCreateTag().getFloat("fuel");
    }
}
