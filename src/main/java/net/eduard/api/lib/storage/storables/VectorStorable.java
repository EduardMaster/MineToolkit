package net.eduard.api.lib.storage.storables;

import org.bukkit.util.Vector;

import net.eduard.api.lib.storage.Storable;

public class VectorStorable implements Storable<Vector> {


    @Override
    public Vector newInstance() {
        return new Vector();
    }
}
