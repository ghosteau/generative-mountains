package com.ghosteau.generativemountains;

import com.ghosteau.generativemountains.commands.grabChunkData;
import com.ghosteau.generativemountains.commands.setDataPath;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class GenerativeMountains extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getCommand("grabChunkData").setExecutor(new grabChunkData());
        getCommand("setDataPath").setExecutor(new setDataPath());
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Generative Mountains]: Plugin enabled.");
    }

    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[Generative Mountains]: Plugin disabled.");
    }
}