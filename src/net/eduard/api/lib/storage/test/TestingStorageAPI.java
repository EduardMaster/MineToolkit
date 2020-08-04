package net.eduard.api.lib.storage.test;

import net.eduard.api.lib.command.PlayerOffline;
import net.eduard.api.lib.storage.StorageAPI;

import java.util.UUID;

public class TestingStorageAPI {

    public static void main(String[] args) {

        PlayerOffline offline = new PlayerOffline("EduardKillerPro");

        System.out.println(offline.getUniqueId());

        System.out.println(StorageAPI.getStore(UUID.class));
        String result = StorageAPI.storeInline(PlayerOffline.class, offline);
        System.out.println(result);


    }
}
