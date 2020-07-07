package net.eduard.api.lib.game;

import net.eduard.api.lib.modules.MineReflect;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.storage.Storable;

public class Title implements Storable, Copyable {

    private String title;
    private String subTitle;

    private int fadeIn;

    private int stay;

    private int fadeOut;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Title() {
    }

    public Title(String title,
                 String subTitle) {
        this(20, 20, 20, title, subTitle);
    }

    public Title(int fadeIn, int stay, int fadeOut, String title,
                 String subTitle) {
        super();
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.title = title;
        this.subTitle = subTitle;
    }

    public Title create(Player player) {
        MineReflect.sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
        return this;
    }

    public int getFadeIn() {

        return fadeIn;
    }

    public int getFadeOut() {

        return fadeOut;
    }

    public int getStay() {

        return stay;
    }

    public void setFadeIn(int fadeIn) {

        this.fadeIn = fadeIn;
    }

    public void setFadeOut(int fadeOut) {

        this.fadeOut = fadeOut;
    }

    public void setStay(int stay) {

        this.stay = stay;
    }


}
