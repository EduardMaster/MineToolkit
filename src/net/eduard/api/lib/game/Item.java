package net.eduard.api.lib.game;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;
import org.bukkit.inventory.meta.BookMeta;

@StorageAttributes(inline = true)
public class Item implements Storable {

    private int id=1;
    private int data;
    private int amount=1;
    private String name;
    private List<String> lore = new ArrayList<>();
    private Map<Integer, Integer> enchants = new HashMap<>();
    private String bookTitle;
    private List<String> bookPages = new ArrayList<>();
    private String bookAuthor;

    public Item() {

    }

    public Item(int type) {
        setId(type);
    }

    public Item(String title, String author, String... pages) {
        setType(Material.WRITTEN_BOOK);
        setBookTitle(title);
        setBookTitle(author);
        bookPages.addAll(Arrays.asList(pages));
    }

    @SuppressWarnings("deprecation")
    public ItemStack create() {
        ItemStack item = new ItemStack(id, amount, (short) data);
        Mine.setName(item, name);
        Mine.setLore(item, lore);
        enchants.entrySet().forEach(e -> {
           item.addUnsafeEnchantment(Enchantment.getById(e.getKey()), e.getValue());
        });
        if (item.getItemMeta() instanceof BookMeta) {
            BookMeta meta = (BookMeta) item.getItemMeta();
            bookPages.forEach(p -> meta.addPage(p));
            meta.setAuthor(bookAuthor);
            meta.setTitle(bookTitle);
            item.setItemMeta(meta);
        }
        return item;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Map<Integer, Integer> getEnchants() {
        return enchants;
    }


    public void setType(Material type) {
        this.id = type.getId();
    }


    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

}
