package com.frax.CoinsAPI;

import com.frax.CoinsAPI.manager.FileManager;
import com.frax.CoinsAPI.sql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class CoinsAPI extends Plugin {

    private MySQL sql;
    private FileManager fm;

    private static CoinsAPI plugin;

    public void onEnable() {
        plugin = this;
        fm = new FileManager(ProxyServer.getInstance().getPluginsFolder() + "/mysql.yml");
        fm.createDefaults();

        sql = new MySQL(fm.getC().getString("username"), fm.getC().getString("password"), fm.getC().getString("database"), fm.getC().getInt("port"), fm.getC().getString("host"));

        sql.openCon();
        sql.createTable();
    }

    public void onDisable() {
        sql.close();
    }

    public static CoinsAPI getMain() {
        return plugin;
    }

    public MySQL getSql() {
        return sql;
    }
}
