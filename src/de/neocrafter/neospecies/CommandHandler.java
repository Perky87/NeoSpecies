package de.neocrafter.neospecies;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.neocrafter.neospecies.species.Species;

public class CommandHandler implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		NeoSpecies plugin = NeoSpecies.getInstance();
		if (label.equalsIgnoreCase("species"))
		{
			if (args.length == 0)
			{
				sender.sendMessage(ChatColor.RED + "You need to specify an argument."); //TODO: Print out possible parameters
			}
			if (args.length >= 2 && args[0].equalsIgnoreCase("assign"))
			{
				if (sender.hasPermission("species.admin.assign"))
				{
					Species player = Species.getPlayer(plugin.getServer().getPlayer(args[1]));
					if (player.setSpecies(args[2]))
					{
						sender.sendMessage(ChatColor.GREEN + player.getName() + " has been succesfully assigned " + args[2]);
					}
					else
					{
						sender.sendMessage(ChatColor.RED + args[2] + " could not be found.");
					}
				}
			}
		}
		return false;
	}	
	
}
