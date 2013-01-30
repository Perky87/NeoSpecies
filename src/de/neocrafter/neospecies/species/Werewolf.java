package de.neocrafter.neospecies.species;

import org.bukkit.Material;

public class Werewolf extends Species
{
	@Override
	public Material[] getDisallowedItems()
	{
		return new Material[]
		{
			Material.BREAD,
			Material.MELON,
			Material.POTATO,
			Material.BAKED_POTATO,
			Material.APPLE,
			Material.CARROT,
			Material.COOKIE,
			Material.GOLDEN_APPLE,
			Material.GOLDEN_CARROT,
			Material.MUSHROOM_SOUP,
			Material.POISONOUS_POTATO,
			Material.SPIDER_EYE
		};
	}
}
