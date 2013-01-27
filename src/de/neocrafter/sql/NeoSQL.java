package de.neocrafter.sql;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

public class NeoSQL
{
	public Connection sql;
	public boolean lite;
	public String AUTOINCREMENT;
	public String AUTOINCREMENT_PRIMARYKEY;
	public String PRIMARYKEY_AUTOINCREMENT;
	
	private Plugin plugin;
	private String driver;
	private String url;
	private String username;
	private String password;
	
	public NeoSQL(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public boolean connect(String driver, String url, String username, String password, boolean shutDown)
	{
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		
		try  {
			lite = driver.equals("org.sqlite.JDBC");
			AUTOINCREMENT = lite ? "AUTOINCREMENT" : "AUTO_INCREMENT";
			AUTOINCREMENT_PRIMARYKEY = lite ? "" : "AUTO_INCREMENT";
			PRIMARYKEY_AUTOINCREMENT = lite ? "PRIMARY KEY" : "PRIMARY KEY AUTO_INCREMENT";
			Class.forName(driver).newInstance();
			
			sql = DriverManager.getConnection(url
	                + (lite ? "" : "?user=" + username + "&password=" + password));
			return true;
		}
		catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			if(shutDown)
			{
				plugin.getLogger().log(Level.SEVERE, "Error while connecting to database - shutting down");
				plugin.getServer().getPluginManager().disablePlugin(plugin);
			}
			else System.out.println("Error while connecting to database");
			
			return false;
		}
	}
	
	public boolean connect(String driver, String url, String username, String password)
	{
		return connect(driver, url, username, password, false);
	}
	
	/*
	 * Query ausfuehren. Ressourcen muessen nachtraeglich geschlossen werden: Fuer select
	 */
	
	public NeoStatement query(String sqlCommand)
	{
		try {
			if(!ensureConnection()) return null;
			return new NeoStatement(sql.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Query ausfuehren und Ressourcen schliessen: Fuer insert, update, delete, create usw.
	 */
	
	public boolean execute(String sqlCommand)
	{
		try {
			if(!ensureConnection()) return false;
			PreparedStatement query = sql.prepareStatement(sqlCommand);
			
			try {
				return query.execute();
			} finally {
				query.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean ensureConnection()
	{
		try {
			if(sql.isClosed())
			{
				plugin.getLogger().log(Level.INFO, "SQL is closed! Trying to reconnect...");
				
				if(connect(driver, url, username, password, false)) plugin.getLogger().log(Level.INFO, "Reconnected");
				else
				{
					plugin.getLogger().log(Level.SEVERE, "Reconnect failed");
					return true;
				}
			}
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}