package com.frax.CoinsAPI.sql.coins;

import com.frax.CoinsAPI.CoinsAPI;
import com.frax.CoinsAPI.sql.ICoins;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SyncCoins implements ICoins {

    private String username;

    public SyncCoins(String username) {
        this.username = username;
    }

    @Override
    public void addCoins(int amount) {
        synchronized (SyncCoins.class) {
            try {
                PreparedStatement ps = CoinsAPI.getMain().getSql().getCon().prepareStatement("UPDATE Coins SET coins='?' WHERE username='?'");
                ps.setInt(1, amount + getCoins());
                ps.setString(2, username);
                ps.executeQuery();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeCoins(int amount) {
        synchronized (SyncCoins.class) {
            try {
                PreparedStatement ps = CoinsAPI.getMain().getSql().getCon().prepareStatement("UPDATE Coins SET coins='?' WHERE username='?'");
                ps.setInt(1,  getCoins() - amount);
                ps.setString(2, username);
                ps.executeQuery();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCoins() {
        synchronized (SyncCoins.class) {
            try {
                PreparedStatement ps = CoinsAPI.getMain().getSql().getCon().prepareStatement("SELECT coins FROM Coins WHERE username='?'");
                ps.setString(1, username);

                int coins = ps.executeQuery().getInt("Coins");
                ps.close();

                return coins;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return 0;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
