


package net.eduard.api.server.permissions;

import com.google.common.base.Preconditions;

import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionRemovedExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * PermissionAttachment for LuckPerms.
 *
 * Applies all permissions directly to the backing user instance via transient nodes.
 */
public class SimplePermissionAttachment extends PermissionAttachment {

    /**
     * The field in PermissionAttachment where the attachments applied permissions
     * are *usually* held.
     */
    private static final Field PERMISSION_ATTACHMENT_PERMISSIONS_FIELD;

    static {
        try {
            PERMISSION_ATTACHMENT_PERMISSIONS_FIELD = PermissionAttachment.class.getDeclaredField("permissions");
            PERMISSION_ATTACHMENT_PERMISSIONS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * The parent LPPermissible
     */
    private final SimplePermissible permissible;

    /**
     * The plugin which "owns" this attachment, may be null
     */
    @SuppressWarnings("unused")
	private final Plugin owner;

    /**
     * The permissions being applied by this attachment
     */
    private final Map<String, Boolean> perms = Collections.synchronizedMap(new HashMap<>());

    /**
     * If the attachment has been applied to the user
     */
    private boolean hooked = false;

    /**
     * Callback to run when the attachment is removed
     */
    private PermissionRemovedExecutor removalCallback = null;

    /**
     * Delegate attachment
     */
    private PermissionAttachment source;

    public SimplePermissionAttachment(SimplePermissible permissible, Plugin owner) {
        super(owner, null);
        this.permissible = permissible;
        this.owner = owner;

        injectFakeMap();
    }

    public SimplePermissionAttachment(SimplePermissible permissible, PermissionAttachment source, Plugin owner) {
        super(owner, null);
        this.permissible = permissible;
        this.owner = source.getPlugin();

        // copy
        this.perms.putAll(source.getPermissions());
        this.source = source;

        injectFakeMap();
    }

    /**
     * Injects a fake 'permissions' map into the superclass, for (clever/dumb??) plugins
     * which attempt to modify attachment permissions using reflection to get around the slow bukkit
     * behaviour in the base PermissionAttachment implementation.
     *
     * The fake map proxies calls back to the methods on this attachment
     */
    private void injectFakeMap() {
        // inner class - this proxies calls back to us
        FakeBackingMap fakeMap = new FakeBackingMap();

        try {
            // the field we need to modify is in the superclass - it has private
            // and final modifiers so we have to use reflection to modify it.
            PERMISSION_ATTACHMENT_PERMISSIONS_FIELD.set(this, fakeMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SimplePermissible getPermissible() {
        return this.permissible;
    }

    @Override
    public PermissionRemovedExecutor getRemovalCallback() {
        return this.removalCallback;
    }

    @Override
    public void setRemovalCallback(PermissionRemovedExecutor removalCallback) {
        this.removalCallback = removalCallback;
    }

    PermissionAttachment getSource() {
        return this.source;
    }

    /**
     * Hooks this attachment with the parent {@link User} instance.
     */
    public void hook() {
        this.hooked = true;
        this.permissible.lpAttachments.add(this);
        for (Map.Entry<String, Boolean> entry : this.perms.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isEmpty()) {
                continue;
            }
            setPermissionInternal(entry.getKey(), entry.getValue());
        }
    }

    private void setPermissionInternal(String name, boolean value) {
//        if (!this.permissible.getPlugin().getConfiguration().get(ConfigKeys.APPLY_BUKKIT_ATTACHMENT_PERMISSIONS)) {
//            return;
//        }

        // construct a node for the permission being set
        // we use the servers static context to *try* to ensure that the node will apply
//        Node node = NodeFactory.builder(name)
//                .setValue(value)
//                .withExtraContext(this.permissible.getPlugin().getContextManager().getStaticContext())
//                .build();
//
//        // convert the constructed node to a transient node instance to refer back to this attachment
//        ImmutableTransientNode<LPPermissionAttachment> transientNode = ImmutableTransientNode.of(node, this);
//
//        // set the transient node
//        User user = this.permissible.getUser();
//        user.setTransientPermission(transientNode);
    }

    private void unsetPermissionInternal(String name) {
//        if (!this.permissible.getPlugin().getConfiguration().get(ConfigKeys.APPLY_BUKKIT_ATTACHMENT_PERMISSIONS)) {
//            return;
//        }

//        // remove transient permissions from the holder which were added by this attachment & equal the permission
//        User user = this.permissible.getUser();
//        user.removeIfTransient(LocalizedNode.composedPredicate(n -> n instanceof ImmutableTransientNode && ((ImmutableTransientNode) n).getOwner() == this && n.getPermission().equals(name)));
    }

    private void clearInternal() {
//        // remove all transient permissions added by this attachment
//        User user = this.permissible.getUser();
//        user.removeIfTransient(LocalizedNode.composedPredicate(n -> n instanceof ImmutableTransientNode && ((ImmutableTransientNode) n).getOwner() == this));
    }

    @Override
    public boolean remove() {
        if (!this.hooked) {
            return false;
        }

        // clear the internal permissions
        clearInternal();

        // run the callback
        if (this.removalCallback != null) {
            this.removalCallback.attachmentRemoved(this);
        }

        // unhook from the permissible
        this.hooked = false;
        this.permissible.lpAttachments.remove(this);
        return true;
    }

    @Override
    public void setPermission(String name, boolean value) {
        Objects.requireNonNull(name, "name is null");
        Preconditions.checkArgument(!name.isEmpty(), "name is empty");

        String permission = name.toLowerCase();

        Boolean previous = this.perms.put(permission, value);
        if (previous != null && previous == value) {
            return;
        }

        // if we're not hooked, then don't actually apply the change
        // it will get applied on hook - if that ever happens
        if (!this.hooked) {
            return;
        }

        if (previous != null) {
            unsetPermissionInternal(permission);
        }

        setPermissionInternal(permission, value);
    }

    @Override
    public void unsetPermission(String name) {
        Objects.requireNonNull(name, "name is null");
        Preconditions.checkArgument(!name.isEmpty(), "name is empty");

        String permission = name.toLowerCase();

        Boolean previous = this.perms.remove(permission);
        if (previous == null) {
            return;
        }

        // if we're not hooked, then don't actually apply the change
        // it will get applied on hook - if that ever happens
        if (!this.hooked) {
            return;
        }

        unsetPermissionInternal(permission);
    }

    @Override
    public Map<String, Boolean> getPermissions() {
        return this.perms;
    }

    @Override
    public Plugin getPlugin() {
//        return this.owner != null ? this.owner : this.permissible.getPlugin().getBootstrap();
    	return null;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    /**
     * A fake map to be injected into the superclass. This implementation simply
     * proxies calls back to this attachment instance.
     *
     * Some (clever/dumb??) plugins attempt to modify attachment permissions using reflection
     * to get around the slow bukkit behaviour in the base PermissionAttachment implementation.
     *
     * An instance of this map is injected into the super instance so these plugins continue
     * to work with LuckPerms.
     */
    private final class FakeBackingMap implements Map<String, Boolean> {

        @Override
        public Boolean put(String key, Boolean value) {
            // grab the previous result, so we can still satisfy the method signature of Map
            Boolean previous = SimplePermissionAttachment.this.perms.get(key);

            // proxy the call back through the PermissionAttachment instance
            setPermission(key, value);

            // return the previous value
            return previous;
        }

        @Override
        public Boolean remove(Object key) {
            // we only accept string keys
            if (!(key instanceof String)) {
                return null;
            }

            String permission = ((String) key);

            // grab the previous result, so we can still satisfy the method signature of Map
            Boolean previous = SimplePermissionAttachment.this.perms.get(permission);

            // proxy the call back through the PermissionAttachment instance
            unsetPermission(permission);

            // return the previous value
            return previous;
        }

        @Override
        public void putAll(Map<? extends String, ? extends Boolean> m) {
            for (Map.Entry<? extends String, ? extends Boolean> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void clear() {
            // remove the permissions which have already been applied
            if (SimplePermissionAttachment.this.hooked) {
                clearInternal();
            }

            // clear the backing map
            SimplePermissionAttachment.this.perms.clear();
        }

        @Override
        public int size() {
            // return the size of the permissions map - probably the most accurate value we have
            return SimplePermissionAttachment.this.perms.size();
        }

        @Override
        public boolean isEmpty() {
            // return if the permissions map is empty - again probably the most accurate thing
            // we can return
            return SimplePermissionAttachment.this.perms.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            // just proxy
            return SimplePermissionAttachment.this.perms.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            // just proxy
            return SimplePermissionAttachment.this.perms.containsValue(value);
        }

        @Override
        public Boolean get(Object key) {
            // just proxy
            return SimplePermissionAttachment.this.perms.get(key);
        }

        @Override
        public Set<String> keySet() {
            // just proxy
            return Collections.unmodifiableSet(SimplePermissionAttachment.this.perms.keySet());
        }

        @Override
        public Collection<Boolean> values() {
            // just proxy
            return Collections.unmodifiableCollection(SimplePermissionAttachment.this.perms.values());
        }

        @Override
        public Set<Entry<String, Boolean>> entrySet() {
            // just proxy
            return Collections.unmodifiableSet(SimplePermissionAttachment.this.perms.entrySet());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Map<?, ?> && SimplePermissionAttachment.this.perms.equals(obj);
        }

        @Override
        public int hashCode() {
            return SimplePermissionAttachment.this.perms.hashCode();
        }
    }
}
