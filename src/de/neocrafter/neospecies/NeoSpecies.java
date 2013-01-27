package de.neocrafter.neospecies;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.neocrafter.neospecies.species.Species;
import de.neocrafter.sql.NeoSQL;

public class NeoSpecies extends JavaPlugin
{
	private NeoSQL database;
	private FileConfiguration config;
	
	@Override
	public void onEnable()
	{
		config = this.getConfig();
		setupConfigDefaults();
		database = new NeoSQL(this);
		System.out.println(config.getString("database.url"));
		if (!database.connect(config.getString("database.driver"), config.getString("database.url"), config.getString("database.user"), config.getString("database.password")))
		{
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		setupDatabases();
		
		NeoListener listener = new NeoListener();
		this.getServer().getPluginManager().registerEvents(listener, this);
		
		Species.Type type = Species.Type.valueOf("HUMAN");
		type.getInstance();
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
				"`Playername` VARCHAR( 16 ) NOT NULL ," +
				"`Species` INT NOT NULL ," +
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
