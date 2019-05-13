package de.hawhh.in2.aufg2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer
{
	private String _id;
	private String _vorname;
	private String _nachname;
	private String _datum;
	private int _version;
	
	public Customer()
	{
	}
	
	public Customer(String id, String vorname, String nachname, String datum)
	{
		setID(id);
		setFirstName(vorname);
		setFamilyName(nachname);
		setEntryDate(datum);
	}

	public String getID() { return _id; }
	public void setID(String id) { this._id = id; }
	
	public String getFirstName() { return _vorname; }
	public void setFirstName(String vorname) { this._vorname = vorname; }
	
	public String getFamilyName() { return _nachname; }
	public void setFamilyName(String nachname) { this._nachname = nachname; }
	
	public String getEntryDate() { return _datum; }
	public void setEntryDate(String datum) { this._datum = datum; }
	
    public static boolean isValidDate(String datum)
    {
        Date date = null;
        try 
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            date = sdf.parse(datum);
            if (!datum.equals(sdf.format(date))) 
            {
                return false;
            }
        } 
        catch (ParseException ex) 
        {
            return false;
        }
        return true;
    }
    
    public static void insertCustomer(Connection c, String id, String vorname, String nachname, String datum) throws SQLException
    {
    	if(!isValidDate(datum))
    	{
    		throw new IllegalArgumentException("Datum wird nur im Format dd.MM.yyyy akzeptiert!");
    	}
    	try
		{
			PreparedStatement ps = c.prepareStatement("INSERT INTO CUSTOMER VALUES(" + id + ",'" + vorname + "','" + nachname + "','"+ datum +"')");
			ps.executeUpdate();
		}
		catch(SQLException ex)
		{
			System.out.println("Fehler im SqlBefehl");
		}
    	
    }
    
    public static void ExecuteSql(Connection c, String sql) throws SQLException
	{
		try
		{
			PreparedStatement ps = c.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch(SQLException ex)
		{
			System.out.println("Fehler");
		}
	}

}
