package net.eduard.api.lib.abstraction

import org.bukkit.Location
import org.bukkit.entity.Player

class Hologram_v1_7_R4 : Hologram {
    override var text: String
        get() = TODO("Not yet implemented")
        set(value) {}
    override var playersSeeing: MutableList<Player>
        get() = TODO("Not yet implemented")
        set(value) {}
    override val isSpawned: Boolean
        get() = TODO("Not yet implemented")

    override fun canSee(player: Player): Boolean {
        TODO("Not yet implemented")
    }

    override fun spawn(local: Location) {
        TODO("Not yet implemented")
    }

    override fun move(local: Location) {
        TODO("Not yet implemented")
    }

    override fun show(player: Player) {
        TODO("Not yet implemented")
    }

    override fun hide(player: Player) {
        TODO("Not yet implemented")
    }
    /*
    private List<String> lines = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();
    private boolean showing = false;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public Hologram_1_7_R4(String[] lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    public void show(OfflinePlayer p, Location loc) {
        if (this.showing) {
            try {
                throw new Exception("Is already showing!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Location first = loc.clone().add(0.0D, this.lines.size() / 2 * 0.23D, 0.0D);
        for (int i = 0; i < this.lines.size(); i++) {
            this.ids.addAll(showLine(p, first.clone(), this.lines.get(i)));
            first.subtract(0.0D, 0.23D, 0.0D);
        }

        this.showing = true;
        this.location = loc;
    }

    public void destroy() {
        if (!this.showing) {
            try {
                throw new Exception("Isn't showing!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int[] ints = new int[this.ids.size()];
        for (int j = 0; j < ints.length; j++) {
            ints[j] = this.ids.get(j);
        }
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);
        for (Player player : Mine.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        this.showing = false;
        this.location = null;
    }

    private static List<Integer> showLine(OfflinePlayer p, Location loc, String text) {
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityWitherSkull skull = new EntityWitherSkull(world);
        skull.setLocation(loc.getX(), loc.getY() + 1.0D + 55.0D, loc.getZ(), 0.0F, 0.0F);
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(skull);

        EntityHorse horse = new EntityHorse(world);
        horse.setLocation(loc.getX(), loc.getY() + 55.0D, loc.getZ(), 0.0F, 0.0F);
        horse.setAge(-1700000);
        horse.setCustomName(text);
        horse.setCustomNameVisible(true);
        PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
        EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        nmsPlayer.playerConnection.sendPacket(packedt);

        PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
        nmsPlayer.playerConnection.sendPacket(pa);
        return Arrays.asList(new Integer[] {
        }

           */
    }