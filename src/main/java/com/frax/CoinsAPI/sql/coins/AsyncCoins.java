package com.frax.CoinsAPI.sql.coins;

import com.frax.CoinsAPI.CoinsAPI;
import com.frax.CoinsAPI.sql.ICoins;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AsyncCoins implements ICoins {

    private String username;

    public AsyncCoins(String username) {
        this.username = username;
    }

    @Override
    public void addCoins(int amount) {
        try {
            PreparedStatement ps = CoinsAPI.getMain().getSql().getCon().prepareStatement("UPDATE Coins SET coins='?' WHERE username='?'");
            ps.setInt(1, amount + getCoins(new Callable<HashMap>() {
                @Override
                public int onSucces(HashMap done) {
                    return (int) done.get("coins");
                }

                @Override
                public void onFailure(Throwable cause) { System.out.println("Failed to add coins! Error: " + cause); }
            }));

            ps.setString(2, username);
            ps.executeQuery();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCoins(int amount) {
        try {
            PreparedStatement ps = CoinsAPI.getMain().getSql().getCon().prepareStatement("UPDATE Coins SET coins='?' WHERE username='?'");
            ps.setInt(1,  getCoins(new Callable<HashMap>() {

                @Override
                public int onSucces(HashMap done) {
                    return (int) done.get("coins");
                }

                @Override
                public void onFailure(Throwable cause) { System.out.println("Failed to remove coins! Error: " + cause); }
            }) - amount);

            ps.setString(2, username);
            ps.executeQuery();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCoins(Callable<HashMap> callback) {
        ProxyServer.getInstance().getScheduler().runAsync(CoinsAPI.getMain(), () -> {
            final HashMap<String, Integer> result = new HashMap<>();

            try {
                PreparedStatement ps = CoinsAPI.getMain().getSql().getCon().prepareStatement("SELECT coins FROM Coins WHERE username='?'");

                ResultSet r = ps.executeQuery();
                if (r.next()) {
                    int coins = r.getInt("coins");
                    result.put("coins", coins);
                    ps.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            ProxyServer.getInstance().getScheduler().schedule(CoinsAPI.getMain(), () -> callback.onSucces(result), 1, TimeUnit.SECONDS);
        });

        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public interface Callable<V> {
         int onSucces(V done);
         void onFailure(Throwable cause);
    }

}
