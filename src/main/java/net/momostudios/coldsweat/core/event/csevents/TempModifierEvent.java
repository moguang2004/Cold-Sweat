package net.momostudios.coldsweat.core.event.csevents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.momostudios.coldsweat.common.temperature.modifier.TempModifier;
import net.momostudios.coldsweat.common.temperature.modifier.block.BlockEffect;
import net.momostudios.coldsweat.common.world.BlockEffectEntries;
import net.momostudios.coldsweat.common.world.TempModifierEntries;
import net.momostudios.coldsweat.core.event.StorePlayerData;
import net.momostudios.coldsweat.core.util.PlayerTemp;

import java.util.Map;

public class TempModifierEvent extends Event
{
    /**
     * Fired when a {@link TempModifier} is about to be added. <br>
     * <br>
     * {@link #duplicatesAllowed} determines whether the TempModifier may be added if an instance already exists. <br>
     * {@link #player} is the player the TempModifier is being applied to. <br>
     * {@link #type} determines the modifier's {@link PlayerTemp.Types}. It will never be {@link PlayerTemp.Types#COMPOSITE} <br>
     * <br>
     * This event is {@link net.minecraftforge.eventbus.api.Cancelable}. <br>
     * Canceling this event will prevent the TempModifier from being added.<br>
     * <br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
     */
    @Cancelable
    public static class Add extends TempModifierEvent
    {
        private PlayerEntity player;
        private TempModifier modifier;
        public boolean duplicatesAllowed;
        public PlayerTemp.Types type;

        public void setDuplicatesAllowed(boolean allowDuplicates) {
            this.duplicatesAllowed = allowDuplicates;
        }

        public void setModifierType(PlayerTemp.Types newType) {
            this.type = newType;
        }

        public final TempModifier getModifier() {
            return modifier;
        }

        public final PlayerEntity getPlayer() {
            return player;
        }

        public Add(TempModifier modifier, PlayerEntity player, PlayerTemp.Types type, boolean duplicates)
        {
            duplicatesAllowed = duplicates;
            this.player = player;
            this.type = type;
            this.modifier = modifier;
        }
    }


    /**
     * Fired when a {@link TempModifier} is about to be removed. <br>
     * <br>
     * {@link #player} is the player the TempModifier is being removed from. <br>
     * {@link #type} is the modifier's {@link PlayerTemp.Types}. It will never be {@link PlayerTemp.Types#COMPOSITE}. <br>
     * {@link #modifierClass} is the class of the TempModifier being removed. <br>
     * {@link #count} is the number of TempModifiers of the specified class being removed. <br>
     * <br>
     * This event is {@link net.minecraftforge.eventbus.api.Cancelable}. <br>
     * Canceling this event will prevent the TempModifier from being removed. <br>
     * <br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
     */
    @Cancelable
    public static class Remove extends TempModifierEvent
    {
        public final PlayerEntity player;
        public final Class<? extends TempModifier> modifierClass;
        public final PlayerTemp.Types type;
        public int count;

        public Remove(PlayerEntity player, Class<? extends TempModifier> modClass, PlayerTemp.Types type, int count)
        {
            this.player = player;
            this.modifierClass = modClass;
            this.type = type;
            this.count = count;
        }

        public void setCount(int newCount) {
            this.count = newCount;
        }
    }


    /**
     * Fired when the {@link TempModifier} or {@link BlockEffect} registry is being built. <br>
     * The event is fired during {@link net.minecraftforge.event.world.WorldEvent.Load}. <br>
     * <br>
     * {@link Modifier} refers to registries being added to {@link TempModifierEntries}. <br>
     * {@link Block} refers to registries being added to {@link BlockEffectEntries}. <br>
     * Use {@code getPool().flush()} if calling manually to prevent duplicates. <br>
     * <br>
     * This event is not {@link net.minecraftforge.eventbus.api.Cancelable}. <br>
     * <br>
     * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
     */
    public static class Init extends TempModifierEvent
    {
        public static class Modifier extends TempModifierEvent
        {
            /**
             * @return the map of registered {@link TempModifier}s.
             */
            public final TempModifierEntries getPool() {
                return TempModifierEntries.getEntries();
            }

            /**
             * Adds a new {@link TempModifier} to the registry.
             * @param clazz The class of the TempModifier to add.
             * @throws InstantiationException If the TempModifier has no default constructor.
             * @throws IllegalAccessException If the default constructor is not accessible.
             */
            public void addModifier(Class<? extends TempModifier> clazz) throws InstantiationException, IllegalAccessException
            {
                this.getPool().add(clazz.newInstance());
            }
        }

        public static class Block extends TempModifierEvent
        {
            /**
             * @return the map of registered {@link BlockEffect}s.
             */
            public final BlockEffectEntries getPool() {
                return BlockEffectEntries.getEntries();
            }

            /**
             * Adds a new {@link BlockEffect} to the registry.
             * @param clazz The class of the BlockEffect to add.
             * @throws InstantiationException If the BlockEffect doesn't have a default constructor.
             * @throws IllegalAccessException If the default constructor is not accessible.
             */
            public void addBlockEffect(Class<? extends BlockEffect> clazz) throws InstantiationException, IllegalAccessException
            {
                this.getPool().add(clazz.newInstance());
            }
        }
    }
}