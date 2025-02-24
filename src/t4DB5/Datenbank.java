package t4DB5;

//import java.io.File;
//import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

/*import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
*/
public class Datenbank {

	//private static final String DB_LOCATION = "..\\DB";
	//private static final String DB_LOCATION = "c:\\Users\\puchalska\\ownCloud - Bergmann Robert (bergmann)@owncloud.tuwien.ac.at\\DB";
	private static final String DB_LOCATION = "c:\\Java\\DB";
	private static final String CONNECTION = "jdbc:derby:" + DB_LOCATION + ";create = true";

   
	private static final String BUCH_TABLE = "Buecher";
	private static final String BUCH_TABLE_ISBN = "BuecherISBN";
	private static final String BUCH_TABLE_AUTHOR = "BuecherAuthor";
	private static final String BUCH_TABLE_TITEL = "BuecherTitel";
	private static final String BUCH_TABLE_THEMA = "BuecherThema";
	private static final String BUCH_TABLE_JAHR = "BuecherJahr";
	private static final String BUCH_TABLE_PREIS = "BuecherPreis";

	private static final String KUNDEN_TABLE = "Kuenden";
	private static final String KUNDEN_ID = "KuendenId";
	private static final String KUNDEN_NAME = "KuendenName";
	private static final String KUNDEN_DATUM = "KuendenDatum";

	private static final String BESTELLUNGEN_TABLE = "Bestellungen";
	private static final String BESTELLUNGEN_ID = "BestellungenId";
	private static final String BESTELLUNGEN_KUNDEN_ID = "BestellungenKundenId";
	private static final String BESTELLUNGEN_BUCH_ISBN = "BestellungenBuchISBN";

    
	public static void createKunden() throws SQLException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, KUNDEN_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}
			String ct = "CREATE TABLE " + KUNDEN_TABLE + " (" + KUNDEN_ID + " BIGINT NOT NULL, "
					+ KUNDEN_NAME + " VARCHAR(200), " + KUNDEN_DATUM + " DATE," 
					+ "PRIMARY KEY (" + KUNDEN_ID + "))";
			/*
			 * CREATE TABLE books (isbn CHAR(13) NOT NULL, title VARCHAR(255), thema
			 * VARCHAR(255), author VARCHAR(255), year INTEGER, preis INTEGER, PRIMARY_KEY
			 * (isbn))
			 */

			stat.execute(ct);
			System.out.println("Kunde erstelt");
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void createBestellungen() throws SQLException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, BESTELLUNGEN_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}
			String ct = "CREATE TABLE " + BESTELLUNGEN_TABLE + " (" + 
				    BESTELLUNGEN_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," 
				    + BESTELLUNGEN_KUNDEN_ID + " BIGINT,"
				    + BESTELLUNGEN_BUCH_ISBN + " CHAR(13)," 
				    + "PRIMARY KEY (" + BESTELLUNGEN_ID + "),"
				    + "FOREIGN KEY (" + BESTELLUNGEN_KUNDEN_ID + ") REFERENCES " + KUNDEN_TABLE + "(" + KUNDEN_ID + "),"
				    + "FOREIGN KEY (" + BESTELLUNGEN_BUCH_ISBN + ") REFERENCES " + BUCH_TABLE + "(" + BUCH_TABLE_ISBN + ")" + ")";

			stat.execute(ct);
			System.out.println("createBest");
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static ArrayList<String> readThemen()throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		ArrayList<String> alThemen = new ArrayList<>();
		String select = "SELECT DISTINCT " + BUCH_TABLE_THEMA + " FROM " + BUCH_TABLE + 
				" ORDER BY " + BUCH_TABLE_THEMA	+ " ASC";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			rs = stat.executeQuery();
			while (rs.next())
				alThemen.add(rs.getString(BUCH_TABLE_THEMA));
			rs.close();
			return alThemen;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static ArrayList<Buch> readBuecher(String thema) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		ArrayList<Buch> alleBuecher = new ArrayList<>();
		String select = "SELECT * FROM " + BUCH_TABLE;
		if (thema != null)
			select += " WHERE " + BUCH_TABLE_THEMA + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			if (thema != null)
				stat.setString(1, thema);
			rs = stat.executeQuery();
			while (rs.next()) {
				alleBuecher.add(new Buch(rs.getString(BUCH_TABLE_ISBN), rs.getString(BUCH_TABLE_TITEL),
						rs.getString(BUCH_TABLE_THEMA), rs.getString(BUCH_TABLE_AUTHOR), rs.getInt(BUCH_TABLE_JAHR),
						rs.getInt(BUCH_TABLE_PREIS)));
			}
			rs.close();
			return alleBuecher;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static ArrayList<Kunde> readKunden()throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		ArrayList<Kunde> alKunden = new ArrayList<>();
		String select = "SELECT * FROM " + KUNDEN_TABLE;
		
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			rs = stat.executeQuery();
			while (rs.next()) {
				alKunden.add(new Kunde(rs.getLong(KUNDEN_ID), rs.getString(KUNDEN_NAME), rs.getDate(KUNDEN_DATUM).toLocalDate()));
			}
			rs.close();
			return alKunden;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static ArrayList<Buch> readBestellungen(long customerId) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		ArrayList<Buch> alleBuecher = new ArrayList<>();
		String select = "SELECT * FROM " + BESTELLUNGEN_TABLE
				+ " INNER JOIN " + BUCH_TABLE + " ON " + BESTELLUNGEN_TABLE + "." + BESTELLUNGEN_BUCH_ISBN + 
				"=" + BUCH_TABLE + "." + BUCH_TABLE_ISBN + 
				" WHERE " + BESTELLUNGEN_KUNDEN_ID + "=" + customerId;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			rs = stat.executeQuery();
			while (rs.next()) {
				alleBuecher.add(new Buch(rs.getString(BUCH_TABLE_ISBN), rs.getString(BUCH_TABLE_TITEL),
						rs.getString(BUCH_TABLE_THEMA), rs.getString(BUCH_TABLE_AUTHOR), rs.getInt(BUCH_TABLE_JAHR),
						rs.getInt(BUCH_TABLE_PREIS)));
			}
			rs.close();
			return alleBuecher;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static void insertKunde(Kunde customer) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		String insert = "INSERT INTO " + KUNDEN_TABLE + " VALUES (?, ?, ?)";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(insert);
			stat.setLong(1, customer.getId());
			stat.setString(2, customer.getName());
			LocalDateTime ldt = LocalDateTime.of(customer.getDatum(), LocalTime.of(0, 0));
			java.sql.Date date = new java.sql.Date(ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			stat.setDate(3, date);
			stat.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static void insertBestellung(Bestellung order) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		String insert = "INSERT INTO " + BESTELLUNGEN_TABLE + 
				" (" + BESTELLUNGEN_KUNDEN_ID + "," + BESTELLUNGEN_BUCH_ISBN + ") VALUES (?, ?)";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(insert);
			stat.setLong(1, order.getCustomerId());
			stat.setString(2, order.getDasBuch().getiSBN());
			stat.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	public static void kundeEntfernen(Long customerId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(CONNECTION)) {
            String deleteQuery = "DELETE FROM " + KUNDEN_TABLE + " WHERE " + KUNDEN_ID + " = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setLong(1, customerId);
                int rowsAffected = preparedStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("\nCustomer with ID" + customerId + " deleted successfully.");
                } else {
                    System.out.println("No customer with ID " + customerId + " found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
