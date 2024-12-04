package com.ghosteau.generativemountains.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setDataPath implements CommandExecutor
{
    // Stores the user's input path.
    private static String userPath;


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        /*
        - The command to update the data extraction path, particularly used to extract data from Minecraft onto your PC in CSV format (refer to grabChunkData class for more).
        - Note that the path will always default to server if you simply just type in a file name; no spaces allowed at all in the path, including folder names.
        - You must ensure your path actually exists, or else the data may not save at all.
        - It is important that the final part of the path (what your file will be named and saved as) ends with .csv to have data extracted correctly.
        */

        if (cmd.getName().equalsIgnoreCase("setDataPath"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage("§cYou must be in-game to execute this command.");
                return true;
            }
            if (args.length == 1)
            {
                userPath = args[0];
                sender.sendMessage("§a§l[!] Path updated successfully!");
            }
            else
            {
                sender.sendMessage("§cUsage: /setDataPath <your/specified/path.csv>; ensure no spaces in path at all.");
            }
            return true;
        }
        return true;
    }

    public static String getPath()
    {
        // Returns the path string.
        return userPath;
    }
}