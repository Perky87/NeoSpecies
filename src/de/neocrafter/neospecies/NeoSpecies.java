package de.neocrafter.neospecies;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.neocrafter.sql.NeoSQL;

public class NeoSpecies extends JavaPlugin
{
	private NeoSQL database;
	private FileConfiguration config;
	private static NeoSpecies instance;
	
	@Override
	public void onEnable()
	{
		instance = this;
		this.config = this.getConfig();
		setupConfigDefaults();
		this.database = new NeoSQL(this);
		System.out.println(config.getString("database.url"));
		if (!database.connect(config.getString("database.driver"), config.getString("database.url"), config.getString("database.user"), config.getString("database.password")))
		{
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		this.getCommand("species").setExecutor(new CommandHandler());
		this.getServer().getPluginManager().registerEvents(new SpeciesListener(), this);
		
		setupDatabases();
		loadSpecies();
	}
	
	public NeoSQL getNeoDatabase()
	{
		return database;
	}
	
	public static NeoSpecies getInstance()
	{
		return instance;
	}
	
	
	@Override
	public void reloadConfig()
	{
		super.reloadConfig();
		this.config = this.getConfig();
	}
	
	private void loadSpecies()
	{
		SpeciesClassLoader classLoader = new SpeciesClassLoader();
		classLoader.loadSpecies();
		try
		{
			classLoader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void setupConfigDefaults()
	{
		config.options().copyDefaults(true);
		config.addDefault("database.driver", "com.mysql.jdbc.Driver");
		config.addDefault("database.url", "jdbc:mysql://localhost:3306/minecraft");
		config.addDefault("database.user", "root");
		config.addDefault("database.password", "");
		this.saveConfig();
	}
	
	private void setupDatabases()
	{
		database.execute("CREATE TABLE IF NOT EXISTS `NeoSpecies_Player` (" +
				"`ID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
				"`Playername` VARCHAR( 16 ) UNIQUE NOT NULL ," +
				"`Species` VARCHAR(16) NOT NULL ," +
				"`Power` TINYINT( 2 ) NOT NULL DEFAULT '0'" +
				") ENGINE = MYISAM ;");
		
		database.execute("CREATE TABLE IF NOT EXISTS `NeoSpecies_Player-Skill` (" +
				"`PlayerID` INT NOT NULL ," +
				"`Skill` INT NOT NULL ," +
				"`Level` INT NOT NULL ," +
				"`EXP` INT NOT NULL ," +
				"PRIMARY KEY ( `PlayerID` , `Skill` )" +
				") ENGINE = MYISAM ;");
		
		/*
		database.execute("CREATE TABLE IF NOT EXISTS `NeoSpecies_Skill` (" +
				"`ID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
				"`Name` VARCHAR( 64 ) NOT NULL ," +
				"`MaxLevel` INT NOT NULL" +
				") ENGINE = MYISAM ;");
		*/
		
	}
}
