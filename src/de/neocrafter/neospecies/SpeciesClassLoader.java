package de.neocrafter.neospecies;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import de.neocrafter.neospecies.species.Human;
import de.neocrafter.neospecies.species.Species;
import de.neocrafter.neospecies.species.Werewolf;

public class SpeciesClassLoader extends URLClassLoader
{
	private static File[] jarFiles;
	
	public SpeciesClassLoader()
	{
		super(getClassPath(NeoSpecies.getInstance()), SpeciesClassLoader.class.getClassLoader());
	}
	
	public void loadSpecies()
	{
		//Load default classes
		//Should we make all but human extern?
		Species.addSpecies("human", Human.class);
		Species.addSpecies("werewolf", Werewolf.class);
		
		//Load external Classes
		try
		{
			for (File file : jarFiles)
			{
				ZipFile zipFile = new ZipFile(file);
				ZipEntry entry = zipFile.getEntry("species.yml");
				
				YamlConfiguration config = new YamlConfiguration();
				config.load(zipFile.getInputStream(entry));
				
				Class<?> clazz = Class.forName(config.getString("main"), true, this);
				Species.addSpecies(config.getString("name"), clazz.asSubclass(Species.class));
				
				zipFile.close();
			}
		}
		catch (IOException | InvalidConfigurationException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	private static URL[] getClassPath(NeoSpecies plugin)
	{
		LinkedList<URL> ret = new LinkedList<URL>();
		File jarFolder = new File(plugin.getDataFolder(), "Species");
		jarFolder.mkdirs();
		jarFiles = jarFolder.listFiles();
		
		if (jarFiles == null)
		{
			jarFiles = new File[]{};
			return new URL[]{};
		}
		
		try
		{
			for (File file : jarFiles)
			{
				ret.add(file.toURI().toURL());
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace(); //This should never happen
		}
		
		return ret.toArray(new URL[0]);
	}
}
