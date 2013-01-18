package com.sk89q.worldedit.bukkit;

import net.minecraft.server.v1_4_6.NBTBase;
import net.minecraft.server.v1_4_6.NBTTagByte;
import net.minecraft.server.v1_4_6.NBTTagByteArray;
import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.NBTTagDouble;
import net.minecraft.server.v1_4_6.NBTTagEnd;
import net.minecraft.server.v1_4_6.NBTTagFloat;
import net.minecraft.server.v1_4_6.NBTTagInt;
import net.minecraft.server.v1_4_6.NBTTagIntArray;
import net.minecraft.server.v1_4_6.NBTTagList;
import net.minecraft.server.v1_4_6.NBTTagLong;
import net.minecraft.server.v1_4_6.NBTTagShort;
import net.minecraft.server.v1_4_6.NBTTagString;
import net.minecraft.server.v1_4_6.TileEntity;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.foundation.Block;

public abstract class NmsBlock extends BaseBlock {
            field = net.minecraft.server.v1_4_6.Block.class.getDeclaredField("isTileEntity");

    protected NmsBlock(int type) {
        super(type);
    }

    protected NmsBlock(int type, int data) {
        super(type, data);
    }

    public static boolean verify() {
        return false;
    }

    public static NmsBlock get(World world, Vector vector, int type, int data) {
        return null;
    }

    public static boolean set(World world, Vector vector, Block block) {
        return false;
    }

    public static boolean setSafely(World world, Vector vector, Block block, boolean notify) {
        return false;
    }

    public static boolean hasTileEntity(int type) {
        net.minecraft.server.v1_4_6.Block nmsBlock = getNmsBlock(type);
        return false;
    }

    public static boolean isValidBlockType(int type) {
        return false;
    }
}
    public static net.minecraft.server.v1_4_6.Block getNmsBlock(int type) {
        if (type < 0 || type >= net.minecraft.server.v1_4_6.Block.byId.length) {
        return net.minecraft.server.v1_4_6.Block.byId[type];
        return type == 0 || (type >= 1 && type < net.minecraft.server.v1_4_6.Block.byId.length
                && net.minecraft.server.v1_4_6.Block.byId[type] != null);