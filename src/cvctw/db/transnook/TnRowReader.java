/**
 * 
 */
package cvctw.db.transnook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cvctw.edict.Alphabet;
import cvctw.edict.EdictDefinition;
import cvctw.edict.EdictEntry;
import cvctw.edict.EdictTerm;
import cvctw.edict.Source;

/**
 * @author minge
 *
 */
public class TnRowReader {

	public static final String SQL_SELECT_STAR = "select * from transnook.";
	public static final String SQL_SELECT_MAX_ID = "select max(id) from transnook.";
	public static final String SQL_WHERE_ENTRY_ID = " where entryId=";
	
	private TnConnection tnConn = null;

//	@SuppressWarnings("unused")
//	private TnRowReader() {
//		; // disable this ctor
//	}

	public TnRowReader() throws SQLException, FileNotFoundException, IOException {
//		if (conn == null) {
//			throw new SQLException("Connection argument may not be NULL");
//		}
		this.tnConn = TnConnection.getInstance();
	}

	public boolean isRowPresent(String table, String column, String value, boolean isString) throws SQLException {
		boolean ret = false;
//		Statement st = conn.createStatement();
		String valQ = null;
		if (isString == true) {
			valQ = "'" + value + "'";
		} else {
			valQ = value;
		}
		String query = "select " + column + " from " + table + " where " + column + "=" + valQ;
		ResultSet rs = tnConn.tnExecuteStatement(query, TnStatementType.QUERY);
		// st.executeQuery(query);
		if (isString == true) {
			String qVal = null;
			while(rs.next()) {
				qVal = rs.getString(1);
			}
			if (value.equals(qVal)) {
				ret = true;
			}
		} else {
			Integer qVal = null;
			while(rs.next()) {
				qVal = rs.getInt(1);
			}
			if (Integer.valueOf(value) == qVal) {
				ret = true;
			}
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public Integer readMaxId(String table) throws SQLException {
		Integer ret = 0;
		String query = SQL_SELECT_MAX_ID + table;
//		Statement st = conn.createStatement();
		ResultSet rs = tnConn.tnExecuteStatement(query, TnStatementType.QUERY);
		// st.executeQuery(SQL_SELECT_MAX_ID + table);
		while(rs.next()) {
			ret = rs.getInt(1);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public Integer readMaxIdForEntry(String table, Integer entryId) throws SQLException {
		Integer ret = 0;
		String query = SQL_SELECT_MAX_ID + table + SQL_WHERE_ENTRY_ID + entryId;
//		Statement st = conn.createStatement();
		ResultSet rs = tnConn.tnExecuteStatement(query, TnStatementType.QUERY);
		// st.executeQuery(SQL_SELECT_MAX_ID + table + SQL_WHERE_ENTRY_ID + entryId);
		while(rs.next()) {
			ret = rs.getInt(1);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public ArrayList<EdictEntry> readEntries() throws SQLException {
		ArrayList<EdictEntry> ret = new ArrayList<EdictEntry>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_ENTRIES;
//		Statement st = conn.createStatement();
		ResultSet rs = tnConn.tnExecuteStatement(query, TnStatementType.QUERY);
		// st.executeQuery(SQL_SELECT_STAR + TnProp.TABLE_ENTRIES);
		EdictEntry e = null;
		while(rs.next()) {
			e = rsToEntry(rs);
			ret.add(e);
		}
		System.out.println("entry count=" + ret.size());
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictEntry rsToEntry(ResultSet rs) throws SQLException {
		EdictEntry e = new EdictEntry();
		e.id = rs.getInt("id");
		e.entry = rs.getString("entry");
		e.language = rs.getString("language");
		e.eDictId = rs.getString("eDictId");
		e.source = Source.valueOf(rs.getString("source"));
		return e;
	}
	public ArrayList<EdictTerm> readTerms() throws SQLException {
		ArrayList<EdictTerm> ret = new ArrayList<EdictTerm>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_TERMS;
//		Statement st = conn.createStatement();
		ResultSet rs = tnConn.tnExecuteStatement(query, TnStatementType.QUERY);
		// st.executeQuery(SQL_SELECT_STAR + TnProp.TABLE_TERMS);
		EdictTerm t = null;
		while(rs.next()) {
			t = rsToTerm(rs);
			ret.add(t);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictTerm rsToTerm(ResultSet rs) throws SQLException {
		EdictTerm t = new EdictTerm();
		t.entryId = rs.getInt("entryId");
		t.id = rs.getInt("id");
		t.term = rs.getString("term");
		t.alphabet = Alphabet.valueOf(rs.getString("alphabet"));
		return t;
	}

	public ArrayList<EdictDefinition> readDefinitions() throws SQLException {
		ArrayList<EdictDefinition> ret = new ArrayList<EdictDefinition>();
		String query = "SQL_SELECT_STAR + TnProp.TABLE_DEFINITIONS";
//		Statement st = conn.createStatement();
		ResultSet rs = tnConn.tnExecuteStatement(query, TnStatementType.QUERY);
		// st.executeQuery(SQL_SELECT_STAR + TnProp.TABLE_DEFINITIONS);
		EdictDefinition d = null;
		while(rs.next()) {
			d = rsToDefinition(rs);
			ret.add(d);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}

	public EdictDefinition rsToDefinition(ResultSet rs) throws SQLException {
		EdictDefinition d = new EdictDefinition();
		d.entryId = rs.getInt("entryId");
		d.id = rs.getInt("id");
		d.defOrder = rs.getInt("defOrder");
		d.language = rs.getString("language");
		d.definition = rs.getString("definition");
		return d;
	}

}
