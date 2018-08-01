package net.eduard.api.server.permissions;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableList;

public class SimplePermissible extends PermissibleBase {
	  private static final Field ATTACHMENTS_FIELD;

	    static {
	        try {
	            ATTACHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
	            ATTACHMENTS_FIELD.setAccessible(true);
	        } catch (NoSuchFieldException e) {
	            throw new ExceptionInInitializerError(e);
	        }
	    }

	    public boolean hasPermission2(String permission) {
	        if (permission == null) {
	            throw new NullPointerException("permission");
	        }

//	        Tristate ts = this.user.getCachedData().getPermissionData(this.contextsSupplier.getContexts()).getPermissionValue(permission, CheckOrigin.PLATFORM_PERMISSION_CHECK);
//	        return ts != Tristate.UNDEFINED ? ts.asBoolean() : Permission.DEFAULT_PERMISSION.getValue(isOp());
	        return false;
	    }
	@Override
	public boolean hasPermission(String perm) {
		for (PermissionAttachmentInfo permisao : getEffectivePermissions()) {
			if (perm.endsWith(".*")) {
				String newPerm = perm.split(".*")[0].toLowerCase();
				if (permisao.getPermission().toLowerCase().startsWith(newPerm)) {
					return true;
				}
			}else {
				if (perm.equalsIgnoreCase(permisao.getPermission())) {
					return true;
				}
			}
		}
		
		return false;
			
		
	}
	   // the LuckPerms user this permissible references.
//    private final User user;

    // the player this permissible is injected into.
    private Player player;

    // the luckperms plugin instance
    private Plugin plugin;

    // caches context lookups for the player
//    private final ContextsSupplier contextsSupplier;

    // the players previous permissible. (the one they had before this one was injected)
    private PermissibleBase oldPermissible = null;

    // if the permissible is currently active.
//    private final AtomicBoolean active = new AtomicBoolean(false);

    // the attachments hooked onto the permissible.
    // this collection is only modified by the attachments themselves
    final Set<SimplePermissionAttachment> lpAttachments = ConcurrentHashMap.newKeySet();

    public SimplePermissible(Player player, JavaPlugin  plugin) {
        super(player);
//        this.user = Objects.requireNonNull(user, "user");
//        this.player = Objects.requireNonNull(player, "player");
//        this.plugin = Objects.requireNonNull(plugin, "plugin");
//        this.contextsSupplier = plugin.getContextManager().getCacheFor(player);

        injectFakeAttachmentsList();
    }

    /**
     * Injects a fake 'attachments' list into the superclass, for dumb plugins
     * which for some reason decided to add attachments via reflection.
     *
     * The fake list proxies (some) calls back to the proper methods on this permissible.
     */
    private void injectFakeAttachmentsList() {
        FakeAttachmentList fakeList = new FakeAttachmentList();

        try {
            // the field we need to modify is in the superclass - it has private
            // and final modifiers so we have to use reflection to modify it.
            ATTACHMENTS_FIELD.set(this, fakeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPermissionSet(String permission) {
        if (permission == null) {
            throw new NullPointerException("permission");
        }

//        Tristate ts = this.user.getCachedData().getPermissionData(this.contextsSupplier.getContexts()).getPermissionValue(permission, CheckOrigin.PLATFORM_LOOKUP_CHECK);
//        return ts != Tristate.UNDEFINED || Permission.DEFAULT_PERMISSION.getValue(isOp());
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        if (permission == null) {
            throw new NullPointerException("permission");
        }

//        Tristate ts = this.user.getCachedData().getPermissionData(this.contextsSupplier.getContexts()).getPermissionValue(permission.getName(), CheckOrigin.PLATFORM_LOOKUP_CHECK);
//        if (ts != Tristate.UNDEFINED) {
//            return true;
//        }
//
//        if (!this.plugin.getConfiguration().get(ConfigKeys.APPLY_BUKKIT_DEFAULT_PERMISSIONS)) {
//            return Permission.DEFAULT_PERMISSION.getValue(isOp());
//        } else {
//            return permission.getDefault().getValue(isOp());
//        }
        return false;
    }

//    @Override
//    public boolean hasPermission(String permission) {
////        if (permission == null) {
////            throw new NullPointerException("permission");
////        }
////
////        Tristate ts = this.user.getCachedData().getPermissionData(this.contextsSupplier.getContexts()).getPermissionValue(permission, CheckOrigin.PLATFORM_PERMISSION_CHECK);
////        return ts != Tristate.UNDEFINED ? ts.asBoolean() : Permission.DEFAULT_PERMISSION.getValue(isOp());
//    	return false;
//    }

//    @Override
//    public boolean hasPermission(Permission permission) {
//        if (permission == null) {
//            throw new NullPointerException("permission");
//        }
//
////        Tristate ts = this.user.getCachedData().getPermissionData(this.contextsSupplier.getContexts()).getPermissionValue(permission.getName(), CheckOrigin.PLATFORM_PERMISSION_CHECK);
////        if (ts != Tristate.UNDEFINED) {
////            return ts.asBoolean();
////        }
////
////        if (!this.plugin.getConfiguration().get(ConfigKeys.APPLY_BUKKIT_DEFAULT_PERMISSIONS)) {
////            return Permission.DEFAULT_PERMISSION.getValue(isOp());
////        } else {
////            return permission.getDefault().getValue(isOp());
////        }
//    }

    /**
     * Adds attachments to this permissible.
     *
     * @param attachments the attachments to add
     */
    void convertAndAddAttachments(Collection<PermissionAttachment> attachments) {
//        for (PermissionAttachment attachment : attachments) {
//            new SimplePermissionAttachment(this, attachment).hook();
//        }
    }

    @Override
    public void setOp(boolean value) {
        this.player.setOp(value);
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
//        return this.user.getCachedData().getPermissionData(this.contextsSupplier.getContexts()).getImmutableBacking().entrySet().stream()
//                .map(entry -> new PermissionAttachmentInfo(this.player, entry.getKey(), null, entry.getValue()))
//                .collect(ImmutableCollectors.toSet());
    	return null;
    }

    @Override
    public SimplePermissionAttachment addAttachment(Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }

        SimplePermissionAttachment ret = new SimplePermissionAttachment(this, plugin);
        ret.hook();
        return ret;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String permission, boolean value) {
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }
        if (permission == null) {
            throw new NullPointerException("permission");
        }

        PermissionAttachment ret = addAttachment(plugin);
        ret.setPermission(permission, value);
        return ret;
    }

    @Override
    public SimplePermissionAttachment addAttachment(Plugin plugin, int ticks) {
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }

        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is not enabled");
        }

        SimplePermissionAttachment ret = addAttachment(plugin);
//        if (getPlugin().getBootstrap().getServer().getScheduler().scheduleSyncDelayedTask(plugin, ret::remove, ticks) == -1) {
//            ret.remove();
//            throw new RuntimeException("Could not add PermissionAttachment to " + this.player + " for plugin " + plugin.getDescription().getFullName() + ": Scheduler returned -1");
//        }
        return ret;
    }

    @Override
    public SimplePermissionAttachment addAttachment(Plugin plugin, String permission, boolean value, int ticks) {
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }
        if (permission == null) {
            throw new NullPointerException("permission");
        }

        SimplePermissionAttachment ret = addAttachment(plugin, ticks);
        ret.setPermission(permission, value);
        return ret;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        if (attachment == null) {
            throw new NullPointerException("attachment");
        }

        SimplePermissionAttachment a;

        if (!(attachment instanceof SimplePermissionAttachment)) {
            // try to find a match
            SimplePermissionAttachment match = this.lpAttachments.stream().filter(at -> at.getSource() == attachment).findFirst().orElse(null);
            if (match != null) {
                a = match;
            } else {
                throw new IllegalArgumentException("Given attachment is not a SimplePermissionAttachment.");
            }
        } else {
            a = (SimplePermissionAttachment) attachment;
        }

        if (a.getPermissible() != this) {
            throw new IllegalArgumentException("Attachment does not belong to this permissible.");
        }

        a.remove();
    }

    @Override
    public void recalculatePermissions() {
        // do nothing
    }

    @Override
    public void clearPermissions() {
        this.lpAttachments.forEach(SimplePermissionAttachment::remove);
    }

//    public User getUser() {
//        return this.user;
//    }

    public Player getPlayer() {
        return this.player;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    PermissibleBase getOldPermissible() {
        return this.oldPermissible;
    }

//    AtomicBoolean getActive() {
//        return this.active;
//    }

    void setOldPermissible(PermissibleBase oldPermissible) {
        this.oldPermissible = oldPermissible;
    }

    /**
     * A fake list to be injected into the superclass. This implementation simply
     * proxies calls back to this permissible instance.
     *
     * Some (clever/dumb??) plugins attempt to add/remove/query attachments using reflection.
     *
     * An instance of this map is injected into the super instance so these plugins continue
     * to work with LuckPerms.
     */
    private final class FakeAttachmentList implements List<PermissionAttachment> {

        @Override
        public boolean add(PermissionAttachment attachment) {
//            if (LPPermissible.this.lpAttachments.stream().anyMatch(at -> at.getSource() == attachment)) {
//                return false;
//            }
//
//            new SimplePermissionAttachment(LPPermissible.this, attachment).hook();
            return true;
        }

        @Override
        public boolean remove(Object o) {
            removeAttachment((PermissionAttachment) o);
            return true;
        }

        @Override
        public void clear() {
            clearPermissions();
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends PermissionAttachment> c) {
            boolean modified = false;
            for (PermissionAttachment e : c) {
                if (add(e)) {
                    modified = true;
                }
            }
            return modified;
        }

        @Override
        public boolean contains(Object o) {
            PermissionAttachment attachment = (PermissionAttachment) o;
            return SimplePermissible.this.lpAttachments.stream().anyMatch(at -> at.getSource() == attachment);
        }

        @Override
        public Iterator<PermissionAttachment> iterator() {
            return ImmutableList.<PermissionAttachment>copyOf(SimplePermissible.this.lpAttachments).iterator();
        }

        @Override
        public ListIterator<PermissionAttachment> listIterator() {
            return ImmutableList.<PermissionAttachment>copyOf(SimplePermissible.this.lpAttachments).listIterator();
        }

        @Nonnull
        @Override
        public Object[] toArray() {
            return ImmutableList.<PermissionAttachment>copyOf(SimplePermissible.this.lpAttachments).toArray();
        }

        @Nonnull
        @Override
        public <T> T[] toArray(@Nonnull T[] a) {
            return ImmutableList.<PermissionAttachment>copyOf(SimplePermissible.this.lpAttachments).toArray(a);
        }

        @Override public int size() { throw new UnsupportedOperationException(); }
        @Override public boolean isEmpty() { throw new UnsupportedOperationException(); }
        @Override public boolean containsAll(@Nonnull Collection<?> c) { throw new UnsupportedOperationException(); }
        @Override public boolean addAll(int index, @Nonnull Collection<? extends PermissionAttachment> c) { throw new UnsupportedOperationException(); }
        @Override public boolean removeAll(@Nonnull Collection<?> c) { throw new UnsupportedOperationException(); }
        @Override public boolean retainAll(@Nonnull Collection<?> c) { throw new UnsupportedOperationException(); }
        @Override public PermissionAttachment get(int index) { throw new UnsupportedOperationException(); }
        @Override public PermissionAttachment set(int index, PermissionAttachment element) { throw new UnsupportedOperationException(); }
        @Override public void add(int index, PermissionAttachment element) { throw new UnsupportedOperationException(); }
        @Override public PermissionAttachment remove(int index) { throw new UnsupportedOperationException(); }
        @Override public int indexOf(Object o) { throw new UnsupportedOperationException(); }
        @Override public int lastIndexOf(Object o) { throw new UnsupportedOperationException(); }
        @Nonnull @Override public ListIterator<PermissionAttachment> listIterator(int index) { throw new UnsupportedOperationException(); }
        @Nonnull @Override public List<PermissionAttachment> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
    }
}


