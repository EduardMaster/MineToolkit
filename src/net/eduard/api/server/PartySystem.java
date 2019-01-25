package net.eduard.api.server;

import java.util.List;

import net.eduard.api.lib.modules.FakePlayer;

public interface PartySystem {

	public boolean isInParty(FakePlayer player);

	public boolean isLeader(FakePlayer player);

	public boolean isMember(FakePlayer player);
	
	public FakePlayer getPartyLeader(FakePlayer player);

	public List<FakePlayer> getPartyMembers(FakePlayer player);

}
