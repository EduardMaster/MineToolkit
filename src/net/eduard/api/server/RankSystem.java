package net.eduard.api.server;

import net.eduard.api.lib.modules.FakePlayer;

import java.util.List;

public interface RankSystem {

    public String getRank(FakePlayer player);

    public String getRankPrefix(FakePlayer player);

    public String getRankSuffix(FakePlayer player);

    public double getRankProgress(FakePlayer player);

    public List<String> getRanksNames();


}
