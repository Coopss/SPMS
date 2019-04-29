package com.spms.portfolio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.auth.AuthDAO;
import com.spms.database.SPMSDB;
import com.spms.ticker.live.TickerDAO;

public class PortfolioDAO {
	
	private Connection conn;
	private TickerDAO tdao;
	private final Logger log = LogManager.getLogger(PortfolioDAO.class);
	protected static final String transactionTableName = "internal.transactions";
	protected static final String portfolioTableName = "internal.portfolio";
	
	public PortfolioDAO() throws SQLException {
		conn = SPMSDB.getConnection();
		tdao = new TickerDAO();
	}
	
	public Boolean createTransactionTable() throws SQLException {
		
		if (SPMSDB.tableExists(conn, transactionTableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + transactionTableName + "](\n" + 
				"	[username] [nvarchar](50) NOT NULL,\n" +
				"	[date] [datetime] NOT NULL,\n" +
				"	[symbol] [nvarchar](255) NOT NULL,\n" + 
				"	[shares] [int] NOT NULL,\n" +
				"	[sharePrice] [float] NOT NULL\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, transactionTableName);
	}
	
	public Boolean createPortfolioValueTable() throws SQLException {
		if (SPMSDB.tableExists(conn, portfolioTableName)) {
			return false;
		}
				
		String makeTableCommand = 
				"CREATE TABLE [dbo].[" + portfolioTableName + "](\n" + 
				"	[date] [datetime] NOT NULL,\n" +
				"	[username] [nvarchar](50) NOT NULL,\n" +
				"	[value] [float] NOT NULL\n" +
				") ON [PRIMARY]\n";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(makeTableCommand);
			
		return SPMSDB.tableExists(conn, portfolioTableName);
	}
	
	private Boolean insertPortfolioValue(String user, Float value) throws SQLException {
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + portfolioTableName + "] ([date], [username], [value]) VALUES ('" + SPMSDB.getMSSQLDatetime() + "','" + user + "'," + value + ");");
		
		return true;
	}
	
	private Boolean insertTransaction(String user, String sym, String date, String share, String price) throws SQLException {		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("INSERT INTO [" + transactionTableName + "] ([username], [symbol], [date], [shares], [sharePrice]) VALUES ('" + user + "','" + sym.toUpperCase() + "','" + date + "'," + share + "," + price + ");");
		
		return true;
	}
	
	public List<Transaction> getAllUsersTransactions(String user) throws NumberFormatException, SQLException {
		List<Transaction> data = new ArrayList<Transaction>();
		
		PreparedStatement stmt = conn.prepareStatement("select [username], [symbol], [date], [shares], [sharePrice] from [" + transactionTableName + "] where username = '" + user + "' order by [date] asc");
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	Transaction row = new Transaction();
	    	row.user = rs.getString(1);
	    	row.symbol = rs.getString(2);
	    	row.date = rs.getString(3);
	    	row.shares = Integer.parseInt(rs.getString(4));
	    	row.sharePrice = Float.parseFloat(rs.getString(5));
	    	data.add(row);

	    }
	    
	    return data;
	}
	
	public Portfolio getUserPortfolio(String user) throws NumberFormatException, SQLException, PortfolioConstraintException {
		List<Transaction> transactions = getAllUsersTransactions(user);
		Portfolio portfolio = new Portfolio(transactions, tdao);
		
		return portfolio;
		
	}
	
	public Boolean addTransaction(Transaction t) throws SQLException {
		return insertTransaction(t.user, t.symbol, t.date, t.shares.toString(), t.sharePrice.toString());
	}
	
	public Boolean addTransaction(String user, String sym, String date, String share, String price) throws SQLException {
		return insertTransaction(user, sym, date, share, price);
	}

	public Boolean reloadPortfolio(String user) throws NumberFormatException, SQLException, PortfolioConstraintException, ParseException {
		Portfolio p = getUserPortfolio(user);
		insertPortfolioValue(user, p.getValue());
		log.info(user + " " + p.portfolio + " " + p.getValue().toString());
		
		return true;
	}
	
	public List<PortfolioValue> getUserValueOverTime(String user) throws NumberFormatException, SQLException {
		List<PortfolioValue> data = new ArrayList<PortfolioValue>();
		
		PreparedStatement stmt = conn.prepareStatement("select [date], [username], [value] from [" + portfolioTableName + "] where username = '" + user + "' order by [date] desc");
		ResultSet rs = stmt.executeQuery();		
	    while(rs.next()) {
	    	PortfolioValue row = new PortfolioValue();
	    	row.user = rs.getString(1);
	    	row.date = rs.getString(2);
	    	row.value = Float.parseFloat(rs.getString(3));
	    	data.add(row);

	    }
	    
	    return data;
	}
	
}
