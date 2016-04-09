package me.markus.bungeeban;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import me.markus.bungeeban.MiniConnectionPoolManager.TimeoutException;

public class MySQLDataSource {

	private String host;
	private String port;
	private String username;
	private String password;
	private String database;
	
	private MiniConnectionPoolManager conPool;

	public MySQLDataSource() throws ClassNotFoundException, SQLException {
		this.host = Settings.getMySQLHost;
		this.port = Settings.getMySQLPort;
		this.username = Settings.getMySQLUsername;
		this.password = Settings.getMySQLPassword;
		this.database = Settings.getMySQLDatabase;


		connect();
	}

	private synchronized void connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		BungeeBan.instance.getLogger().info("MySQL driver loaded");
		MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setDatabaseName(database);
		dataSource.setServerName(host);
		dataSource.setPort(Integer.parseInt(port));
		dataSource.setUser(username);
		dataSource.setPassword(password);
		conPool = new MiniConnectionPoolManager(dataSource, 20);
		BungeeBan.instance.getLogger().info("Connection pool ready");
		// check if database exists
		//this.setup();
	}
	
	public synchronized void updatePlayerIP(String playername, String ip){
		String lwcPlayername = playername.toLowerCase();
		Connection con = null;
		PreparedStatement pstDelete = null;
		PreparedStatement pstInsert = null;
		try {
			con = makeSureConnectionIsReady();
			pstDelete = con.prepareStatement("Delete FROM iphistory WHERE name=?;");
			pstDelete.setString(1, lwcPlayername);
			
			pstInsert = con.prepareStatement("INSERT INTO iphistory (name, ip) VALUES (?,?);");
			pstInsert.setString(1, lwcPlayername);
			pstInsert.setString(2, ip);
			
			int resultDelete = pstDelete.executeUpdate();
			int resultInsert = pstInsert.executeUpdate();

			System.out.println("is success? " +resultDelete + " " + resultInsert);
			close(pstDelete);
			close(pstInsert);
			close(con);

		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} finally {
			close(pstDelete);
			close(pstInsert);
			close(con);
		}
	}
	
	public synchronized String getPlayerIP(String playername){
		String lwcPlayername = playername.toLowerCase();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = makeSureConnectionIsReady();
			pst = con.prepareStatement("SELECT ip FROM iphistory WHERE name =?;");
			pst.setString(1, lwcPlayername);
			rs = pst.executeQuery();
			
			String ip = "";
			while (rs.next()){
				ip = rs.getString("ip");
			}
			close(rs);
			close(pst);
			close(con);
			
			return ip;
			
		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return "";
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return "";
		} finally {
			close(rs);
			close(pst);
			close(con);
		}
	}
	
	public synchronized ArrayList<String> getPlayersByIP(String ip){
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<String> players = new ArrayList<String>();
		try {
			con = makeSureConnectionIsReady();
			pst = con.prepareStatement("SELECT name FROM iphistory WHERE ip =?;");
			pst.setString(1, ip);
			rs = pst.executeQuery();
			
			// get longest ban
			
			while (rs.next()){
				players.add(rs.getString("name"));
			}
			close(rs);
			close(pst);
			close(con);
			
			return players;
			
		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return players;
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return players;
		} finally {
			close(rs);
			close(pst);
			close(con);
		}
	}
	
	public synchronized void banPlayerByName(String playername, String reason, String banner, Timestamp expires) {
	
		Connection con = null;
		PreparedStatement pst = null;
		Timestamp now = new Timestamp(new Date().getTime());
		try {
			con = makeSureConnectionIsReady();
			pst = con.prepareStatement("INSERT INTO bans (name, reason, banner, time, expires) VALUES (?,?,?,?,?);");
			pst.setString(1, playername);
			pst.setString(2, reason);
			pst.setString(3, banner);
			pst.setLong(4, now.getTime());
			pst.setLong(5, expires.getTime());
			int result = pst.executeUpdate();
			System.out.println("is success? " +result );
			close(pst);
			close(con);

		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} finally {
			close(pst);
			close(con);
		}
		
	}
	
	public synchronized void unbanPlayerByName(String playername){
		String lwcPlayername = playername.toLowerCase();
		Connection con = null;
		PreparedStatement pstDelete = null;
		try {
			con = makeSureConnectionIsReady();
			pstDelete = con.prepareStatement("Delete FROM bans WHERE name=?;");
			pstDelete.setString(1, lwcPlayername);
			
			int resultDelete = pstDelete.executeUpdate();

			System.out.println("is success? " +resultDelete);
			close(pstDelete);
			close(con);

		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} finally {
			close(pstDelete);
			close(con);
		}
	}
	
	public synchronized void unbanPlayerByIP(String ip){
		Connection con = null;
		PreparedStatement pstDelete = null;
		try {
			con = makeSureConnectionIsReady();
			pstDelete = con.prepareStatement("Delete FROM ipbans WHERE ip=?;");
			pstDelete.setString(1, ip);
			
			int resultDelete = pstDelete.executeUpdate();

			System.out.println("is success? " +resultDelete);
			close(pstDelete);
			close(con);

		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} finally {
			close(pstDelete);
			close(con);
		}
	}
	
	public synchronized void banPlayerByIP(String ip, String reason, String banner, Timestamp expires) {
		
		Connection con = null;
		PreparedStatement pst = null;
		Timestamp now = new Timestamp(new Date().getTime());
		try {
			con = makeSureConnectionIsReady();
			pst = con.prepareStatement("INSERT INTO ipbans (ip, reason, banner, time, expires) VALUES (?,?,?,?,?);");
			pst.setString(1, ip);
			pst.setString(2, reason);
			pst.setString(3, banner);
			pst.setLong(4, now.getTime());
			pst.setLong(5, expires.getTime());
			int result = pst.executeUpdate();
			System.out.println("is success? " +result );
			close(pst);
			close(con);

		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		} finally {
			close(pst);
			close(con);
		}
		
	}
	
	public synchronized Timestamp getPlayerBansByIp(String ip) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = makeSureConnectionIsReady();
			pst = con.prepareStatement("SELECT expires FROM ipbans WHERE ip =?;");
			pst.setString(1, ip);
			rs = pst.executeQuery();
			
			long bantime = -1;
			// get longest ban
			while (rs.next()){
				long ban = rs.getLong("expires");
				if (ban > bantime){
					bantime = ban;
				}
			}
			close(rs);
			close(pst);
			close(con);
			
			// convert to timestamp
			Timestamp now = new Timestamp(new Date().getTime());
			Timestamp expires = new Timestamp(bantime);
			if (expires.after(now)){
				return expires;
			} else {
				return null;
			}
		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return null;
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return null;
		} finally {
			close(rs);
			close(pst);
			close(con);
		}
	}
	
	public synchronized Timestamp getPlayerBansByName(String playername) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = makeSureConnectionIsReady();
			pst = con.prepareStatement("SELECT expires FROM bans WHERE name =?;");
			pst.setString(1, playername.toLowerCase());
			rs = pst.executeQuery();
			
			long bantime = -1;
			// get longest ban
			while (rs.next()){
				long ban = rs.getLong("expires");
				if (ban > bantime){
					bantime = ban;
				}
			}
			close(rs);
			close(pst);
			close(con);
			
			// convert to timestamp
			Timestamp now = new Timestamp(new Date().getTime());
			Timestamp expires = new Timestamp(bantime);
			if (expires.after(now)){
				return expires;
			} else {
				return null;
			}
		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return null;
		} catch (TimeoutException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
			return null;
		} finally {
			close(rs);
			close(pst);
			close(con);
		}
	}
	
	public synchronized void close() {
		try {
			conPool.dispose();
		} catch (SQLException ex) {
			BungeeBan.instance.getLogger().severe(ex.getMessage());
		}
	}

	private void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException ex) {
				BungeeBan.instance.getLogger().severe(ex.getMessage());
			}
		}
	}

	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				BungeeBan.instance.getLogger().severe(ex.getMessage());
			}
		}
	}

	private void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				BungeeBan.instance.getLogger().severe(ex.getMessage());
			}
		}
	}

	private synchronized Connection makeSureConnectionIsReady() {
		Connection con = null;
		try {
			con = conPool.getValidConnection();
		} catch (Exception te) {
			try {
				con = null;
				reconnect();
			} catch (Exception e) {
				BungeeBan.instance.getLogger().severe(e.getMessage());
				BungeeBan.instance.getLogger().severe("Can't reconnect to MySQL database... Please check your MySQL informations ! SHUTDOWN...");
				BungeeBan.instance.shutdown();
			}
		} catch (AssertionError ae) {
			// Make sure assertionerror is caused by the connectionpoolmanager, else re-throw it
			if (!ae.getMessage().equalsIgnoreCase("BungeeDatabaseError"))
				throw new AssertionError(ae.getMessage());
			try {
				con = null;
				reconnect();
			} catch (Exception e) {
				BungeeBan.instance.getLogger().severe(e.getMessage());
				BungeeBan.instance.getLogger().severe("Can't reconnect to MySQL database... Please check your MySQL informations ! SHUTDOWN...");
				BungeeBan.instance.shutdown();
			}
		}
		if (con == null)
			con = conPool.getValidConnection();
		return con;
	}

	private synchronized void reconnect() throws ClassNotFoundException, SQLException, TimeoutException {
		conPool.dispose();
		Class.forName("com.mysql.jdbc.Driver");
		MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setDatabaseName(database);
		dataSource.setServerName(host);
		dataSource.setPort(Integer.parseInt(port));
		dataSource.setUser(username);
		dataSource.setPassword(password);
		conPool = new MiniConnectionPoolManager(dataSource, 10);
		BungeeBan.instance.getLogger().info("ConnectionPool was unavailable... Reconnected!");
	}

}
