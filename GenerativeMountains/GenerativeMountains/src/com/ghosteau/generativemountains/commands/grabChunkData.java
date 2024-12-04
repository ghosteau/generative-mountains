package com.ghosteau.generativemountains.commands;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Light;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;

public class grabChunkData implements CommandExecutor
{
    // Height limit and minimum Y-value as of 1.21.1.
    final private int maxY = 320;
    final private int minY = -64;

    // ArrayLists that store chunk data.
    private ArrayList<Integer> ListX = new ArrayList<>();
    private ArrayList<Integer> ListY = new ArrayList<>();
    private ArrayList<Integer> ListZ = new ArrayList<>();
    private ArrayList<String> Biome = new ArrayList<>();
    private ArrayList<String> Block_ID = new ArrayList<>();
    private ArrayList<Boolean> Is_Surface = new ArrayList<>();
    private ArrayList<Double> Light_Level = new ArrayList<>();
    private ArrayList<String> Block_to_Left = new ArrayList<>();
    private ArrayList<String> Block_to_Right = new ArrayList<>();
    private ArrayList<String> Block_Below = new ArrayList<>();
    private ArrayList<String> Block_Above = new ArrayList<>();
    private ArrayList<String> Block_in_Front = new ArrayList<>();
    private ArrayList<String> Block_Behind = new ArrayList<>();


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // Retrieves information from player's current chunk and stores it into respective ArrayLists, then finally extracts data to CSV format using other method.
        Player player = null;

        if (!(sender instanceof Player))
        {
            Bukkit.getConsoleSender().sendMessage("§cYou must be in-game to execute this command.");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("grabChunkData"))
        {
            player = (Player) sender;
            Location playerLocation = player.getLocation();
            World world = player.getWorld();
            Chunk playerChunk = world.getChunkAt(playerLocation);

            if (setDataPath.getPath() == null)
            {
                player.sendMessage("§cWarning: please set path before using this command via /setDataPath; default is server file.");
                return true;
            }
            // Loops through every block in the chunk.
            for (int x = 0; x < 16; x++)
            {
                for (int y = minY; y <= maxY; y++)
                {
                    for (int z = 0; z < 16; z++)
                    {
                        ListX.add(x);
                        ListY.add(y);
                        ListZ.add(z);
                        Biome.add(playerChunk.getBlock(x, y, z).getBiome().toString());
                        Block_ID.add(playerChunk.getBlock(x, y, z).getType().toString());
                        Is_Surface.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.UP).getType() == Material.AIR);
                        Light_Level.add((double)playerChunk.getBlock(x, y, z).getLightLevel());
                        Block_to_Left.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.WEST).getType().toString());
                        Block_to_Right.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.EAST).getType().toString());
                        Block_in_Front.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.NORTH).getType().toString());
                        Block_Behind.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.SOUTH).getType().toString());
                        Block_Below.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.DOWN).getType().toString());
                        Block_Above.add(playerChunk.getBlock(x, y, z).getRelative(BlockFace.UP).getType().toString());
                    }
                }
            }
        }
        // Eventually have a way for people to implement their own path via commands; for now just copy and paste path into this string.
        try
        {
            exportToCSV(setDataPath.getPath());
            clearAllLists();
            player.sendMessage("§a§l[!] Chunk data fetched successfully!");
            return true;
        }
        catch (Exception e)
        {
            player.sendMessage("§c§l[!] Fatal error. Please try again.");
            e.printStackTrace();
            return true;
        }
    }

    private void exportToCSV(String path)
    {
        // Exports all data from respective ArrayLists to CSV file format.
        final int length = ListY.size();
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try
        {
            fileWriter = new FileWriter(path);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("x, y, z, Biome, Block_ID, Is_Surface, Light_Level, Block_to_Left, Block_to_Right, Block_Below, Block_Above");
            bufferedWriter.newLine();

            for (int i = 0; i < length; i++)
            {
                bufferedWriter.write(ListX.get(i).toString());
                bufferedWriter.write("," + ListY.get(i).toString());
                bufferedWriter.write("," + ListZ.get(i).toString());
                bufferedWriter.write("," + Block_ID.get(i));
                bufferedWriter.write("," + Biome.get(i));
                bufferedWriter.write("," + Is_Surface.get(i).toString());
                bufferedWriter.write("," + Light_Level.get(i).toString());
                bufferedWriter.write("," + Block_to_Left.get(i));
                bufferedWriter.write("," + Block_to_Right.get(i));
                bufferedWriter.write("," + Block_Below.get(i));
                bufferedWriter.write("," + Block_Above.get(i));
                bufferedWriter.write("," + Block_in_Front.get(i));
                bufferedWriter.write("," + Block_Behind.get(i));
                bufferedWriter.newLine();
            }
        }
        catch (IOException ioe)
        {
            System.err.println("Failed to export to CSV; file failed to create/file not found.");
            ioe.printStackTrace();
        }
        finally
        // Note: finally block always runs regardless of exception status; exists to close all file and buffered readers.
        {
            try
            {
                if (bufferedWriter != null)
                {
                    bufferedWriter.close();
                }

                if (fileWriter != null)
                {
                    fileWriter.close();
                }
            }
            catch (IOException ioe)
            {
                System.err.println("Error while closing BufferedWriter or FileWriter object(s).");
                ioe.printStackTrace();
            }
        }
    }

    private void clearAllLists()
    {
        // Clears all ArrayList data stored in the class.
        ListX.clear();
        ListY.clear();
        ListZ.clear();
        Block_ID.clear();
        Biome.clear();
        Is_Surface.clear();
        Light_Level.clear();
        Block_to_Left.clear();
        Block_to_Right.clear();
        Block_Above.clear();
        Block_Below.clear();
        Block_in_Front.clear();
        Block_Behind.clear();
    }
}