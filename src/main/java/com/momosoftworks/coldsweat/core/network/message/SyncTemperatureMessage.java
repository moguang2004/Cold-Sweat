package com.momosoftworks.coldsweat.core.network.message;

import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.client.gui.Overlays;
import com.momosoftworks.coldsweat.common.capability.handler.EntityTempManager;
import com.momosoftworks.coldsweat.common.capability.temperature.PlayerTempCap;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncTemperatureMessage
{
    int entityId;
    CompoundTag traits;
    boolean instant;

    public SyncTemperatureMessage(LivingEntity entity, CompoundTag traits, boolean instant)
    {   this.entityId = entity.getId();
        this.traits = traits;
        this.instant = instant;
    }

    SyncTemperatureMessage(int entityId, CompoundTag traits, boolean instant)
    {   this.entityId = entityId;
        this.traits = traits;
        this.instant = instant;
    }

    public static void encode(SyncTemperatureMessage message, FriendlyByteBuf buffer)
    {   buffer.writeInt(message.entityId);
        buffer.writeNbt(message.traits);
        buffer.writeBoolean(message.instant);
    }

    public static SyncTemperatureMessage decode(FriendlyByteBuf buffer)
    {   return new SyncTemperatureMessage(buffer.readInt(), buffer.readNbt(), buffer.readBoolean());
    }

    public static void handle(SyncTemperatureMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient())
        {
            context.enqueueWork(() ->
            {
                LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.entityId);

                if (entity != null)
                {
                    EntityTempManager.getTemperatureCap(entity).ifPresent(cap ->
                    {
                        cap.deserializeTraits(message.traits);
                        if (message.instant && cap instanceof PlayerTempCap)
                        {   Overlays.setBodyTempInstant(cap.getTrait(Temperature.Trait.BODY));
                        }
                    });
                }
            });
        }

        context.setPacketHandled(true);
    }
}