package de.neocrafter.neospecies;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.neocrafter.neospecies.species.Species;

public class SpeciesListener implements Listener
{	
	@EventHandler()
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Species.removePlayerFromCache(event.getPlayer());
	}
	
	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Species player = Species.getPlayer(event.getPlayer());
		Material[] disallowed = player.getDisallowedItems();
		for (Material mat : disallowed)
		{
			if (player.getItemInHand().getType() == mat)
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}
