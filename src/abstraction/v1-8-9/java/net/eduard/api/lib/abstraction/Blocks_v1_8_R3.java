package net.eduard.api.lib.abstraction;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.jetbrains.annotations.NotNull;

public class Blocks_v1_8_R3 extends CraftBlock implements Blocks {


    private transient final WorldServer worldServer;

    public WorldServer getWorldServer(){return worldServer;}

    public Blocks_v1_8_R3(int x, int y, int z, WorldServer worldServer) {
        super(null, x, y, z);
        this.worldServer = worldServer;
    }
    public Blocks_v1_8_R3(Location location) {
        this(location.getBlock());
    }

    public Blocks_v1_8_R3(org.bukkit.block.Block block) {
        this(block.getX(), block.getY(), block.getZ(), ((CraftWorld) block.getWorld()).getHandle());
    }

    @Override
    public org.bukkit.World getWorld() {
        return worldServer.getWorld();
    }

    @Override
    public org.bukkit.Chunk getChunk() {
        return worldServer.getChunkAt(getX() >> 4, getZ() >> 4).bukkitChunk;
    }

    @Override
    public int getTypeId() {
        final ChunkSection section = getSection();

        final IBlockData type = section.getType(getX() & 0xF, getY() & 0xF, getZ() & 0xF);
        final Block block = type.getBlock();

        return Block.getId(block);
    }

    @Override
    public byte getData() {
        final ChunkSection section = getSection();

        final IBlockData type = section.getType(getX() & 0xF, getY() & 0xF, getZ() & 0xF);
        final Block block = type.getBlock();

        return (byte) block.toLegacyData(type);
    }

    @Override
    public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
        final ChunkSection section = getSection();

        final int x = getX() & 0xF;
        final int y = getY() & 0xF;
        final int z = getZ() & 0xF;



        int combined = type + (data << 12);
        IBlockData blockData = net.minecraft.server.v1_8_R3.Block
                .getByCombinedId(combined);

        if (blockData == section.getType(x, y, z)) {
            return false;
        }

        section.setType(x, y, z, blockData);


        if (applyPhysics) {

            worldServer.notify(new BlockPosition(getX(), getY(), getZ()));
        }

        return true;
    }
    @Override
    public void setTypeAndData(@NotNull org.bukkit.Material material, int data) {
        setTypeIdAndData(material.getId(), (byte) data, true);
    }
    @Override
    public org.bukkit.block.Block getRelative(int modX, int modY, int modZ) {
        return new Blocks_v1_8_R3(getX() + modX, getY() + modY, getZ() + modZ, worldServer);
    }

    private ChunkSection getSection() {
        final Chunk chunk = worldServer.chunkProviderServer
                .originalGetChunkAt(getX() >> 4, getZ() >> 4);

        ChunkSection chunkSection = chunk.getSections()[getY() >> 4];
        if (chunkSection == null) {
            chunkSection
                    = chunk.getSections()[getY() >> 4]
                    = new ChunkSection(getY() >> 4 << 4, !worldServer.worldProvider.o());
        }

        return chunkSection;
    }


}
