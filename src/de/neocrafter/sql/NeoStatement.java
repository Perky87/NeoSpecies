package de.neocrafter.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NeoStatement
{
	private PreparedStatement query;
	private int index;
	
	public NeoStatement(PreparedStatement query)
	{
		this.query = query;
	}
	
	public void close()
	{
		try {
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery()
	{
		try {
			return query.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean execute()
	{
		try {
			return query.execute();
		}
		catch (Throwable lmaa)
		{
			return false;
		}
		finally {
			close();
		}
	}
	
	public int getId() 
	{
		try {
			ResultSet result = query.getGeneratedKeys();
			try {
				if(result.next()) return result.getInt(1);
			} finally {
				result.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public NeoStatement setBoolean(boolean value)
	{
		try {
			query.setBoolean(++index, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public NeoStatement setByte(byte value)
	{
		try {
			query.setByte(++index, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public NeoStatement setInt(int value)
	{
		try {
			query.setInt(++index, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public NeoStatement setFloat(float value)
	{
		try {
			query.setFloat(++index, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public NeoStatement setDouble(double value)
	{
		try {
			query.setDouble(++index, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public NeoStatement setString(String value)
	{
		try {
			query.setString(++index, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this;
	}
}
