package de.neocrafter.neospecies.species;

public abstract class Species
{
	public enum Type
	{
		HUMAN(Human.class, "Mensch"),
		UNDEAD(Undead.class, "Untot"),
		WEREWOLF(Werewolf.class, "Werwolf"),
		;
		
		private Class<? extends Species> clazz;
		private String name;
		
		Type(Class<? extends Species> clazz, String name)
		{
			this.clazz = clazz;
			this.name = name;
		}
		
		public Species getInstance()
		{
			try
			{
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
		public String getName()
		{
			return name;
		}
	}
	
	private int power;
	
}
