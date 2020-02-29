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
import cvctw.edict.EdictMeaning;
import cvctw.edict.EdictParser;
import cvctw.edict.EdictTerm;
import cvctw.edict.Source;

/**
 * This class provides methods
 * <ul>
 * <li>to read records (SQL SELECT) from the four "entities" tables, 
 * namely, ENTRIES, TERMS, DEFINITIONS and MEANINGS,</li>
 * <li>to unpack the returned SQL ResultSets, and</li>
 * <li>to convert those results to instances of the four "entities" 
 * classes, namely, EdictEntry, EdictTerm, EdictDefinition and EdictMeaning.</li>  
 * 
 * </ul>
 * @author minge
 *
 */
public class TnRowReader {

	public static final String SQL_SELECT_STAR = "select * from " + TnProp.SCHEMA + ".";
	public static final String SQL_SELECT_MAX_ID = "select max(id) from " + TnProp.SCHEMA + ".";
	public static final String SQL_WHERE_ENTRY_ID = " where entryId=";
	public static final String SQL_WHERE_DEF_ID = " where defId=";
	public static final String SQL_WHERE_ID = " where id=";
	
	private TnConnection tnConn = null;

	public TnRowReader() throws SQLException, FileNotFoundException, IOException {
		this.tnConn = TnConnection.getInstance();
	}

	public boolean isRowPresent(String table, String column1, String value1, String column2, String value2) 
			throws SQLException  {
		boolean ret = false;
		String query = "select " + column1 + "," + column2 + " from " + table + " where " + 
				column1 + "='" + value1 + "' and " + column2 + "='" + value2 + "'";
		ResultSet rs = tnConn.tnExecuteQuery(query);
		String qVal1 = null;
		String qval2 = null;
		while(rs.next()) {
			qVal1 = rs.getString(1);
			qval2 = rs.getString(2);
		}
		if (value1.equals(qVal1) && value2.equals(qval2)) {
			ret = true;
		}

		return ret;
	}
	public boolean isRowPresent(String table, String column, String value, boolean isString) throws SQLException {
		boolean ret = false;
		String valQ = null;
		if (isString == true) {
			valQ = "'" + value + "'";
		} else {
			valQ = value;
		}
		String query = "select " + column + " from " + table + " where " + column + "=" + valQ;
		ResultSet rs = tnConn.tnExecuteQuery(query);
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
		ResultSet rs = tnConn.tnExecuteQuery(query);
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
		ResultSet rs = tnConn.tnExecuteQuery(query);
		// st.executeQuery(SQL_SELECT_MAX_ID + table + SQL_WHERE_ENTRY_ID + entryId);
		while(rs.next()) {
			ret = rs.getInt(1);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictEntry readEntry(Integer entryId) throws SQLException {
		EdictEntry ret = null;
		String query = SQL_SELECT_STAR + TnProp.TABLE_ENTRIES + SQL_WHERE_ID + entryId;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		while(rs.next()) {
			ret = rs2Entry(rs);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictEntry rs2Entry(ResultSet rs) throws SQLException {
		EdictEntry e = new EdictEntry();
		e.id = rs.getInt("id");
		e.entry = rs.getString("entry");
		e.language = rs.getString("language");
		e.eDictId = rs.getString("eDictId");
		e.sourceE = Source.SourceE.valueOf(rs.getString("source"));
		return e;
	}
	public ArrayList<EdictTerm> readTerms() throws SQLException {
		ArrayList<EdictTerm> ret = new ArrayList<EdictTerm>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_TERMS;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		// st.executeQuery(SQL_SELECT_STAR + TnProp.TABLE_TERMS);
		EdictTerm t = null;
		while(rs.next()) {
			t = rs2Term(rs);
			ret.add(t);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public ArrayList<EdictTerm> readTerms(Integer entryId) throws SQLException {
		ArrayList<EdictTerm> ret = new ArrayList<EdictTerm>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_TERMS + SQL_WHERE_ENTRY_ID + entryId;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		// st.executeQuery(SQL_SELECT_STAR + TnProp.TABLE_TERMS);
		EdictTerm t = null;
		while(rs.next()) {
			t = rs2Term(rs);
			ret.add(t);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictTerm rs2Term(ResultSet rs) throws SQLException {
		EdictTerm t = new EdictTerm();
		t.entryId = rs.getInt("entryId");
		t.id = rs.getInt("id");
		t.term = rs.getString("term");
		t.alphabetE = Alphabet.AlphabetE.valueOf(rs.getString("alphabet"));
		return t;
	}

	public ArrayList<EdictDefinition> readDefinitions() throws SQLException {
		ArrayList<EdictDefinition> ret = new ArrayList<EdictDefinition>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_DEFINITIONS;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		EdictDefinition d = null;
		while(rs.next()) {
			d = rs2Definition(rs);
			ret.add(d);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public ArrayList<EdictDefinition> readDefinitions(Integer entryId) throws SQLException {
		ArrayList<EdictDefinition> ret = new ArrayList<EdictDefinition>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_DEFINITIONS + SQL_WHERE_ENTRY_ID + entryId;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		EdictDefinition d = null;
		while(rs.next()) {
			d = rs2Definition(rs);
			ret.add(d);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictDefinition rs2Definition(ResultSet rs) throws SQLException {
		EdictDefinition d = new EdictDefinition();
		d.entryId = rs.getInt("entryId");
		d.id = rs.getInt("id");
		d.defOrder = rs.getInt("defOrder");
		d.language = rs.getString("language");
		d.definition = rs.getString("definition");
		return d;
	}

	public ArrayList<EdictMeaning> readMeanings() throws SQLException {
		ArrayList<EdictMeaning> ret = new ArrayList<EdictMeaning>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_DEFINITIONS;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		EdictMeaning d = null;
		while(rs.next()) {
			d = rs2Meaning(rs);
			ret.add(d);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public ArrayList<EdictMeaning> readMeanings(Integer defId) throws SQLException {
		ArrayList<EdictMeaning> ret = new ArrayList<EdictMeaning>();
		String query = SQL_SELECT_STAR + TnProp.TABLE_MEANINGS + SQL_WHERE_DEF_ID + defId;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		EdictMeaning d = null;
		while(rs.next()) {
			d = rs2Meaning(rs);
			ret.add(d);
		}
		tnConn.tnCloseResultSet();
		return ret;
	}
	public EdictMeaning rs2Meaning(ResultSet rs) throws SQLException {
		EdictMeaning m = new EdictMeaning();
		m.entryId = rs.getInt("entryId");
		m.defId = rs.getInt("defId");
		m.id = rs.getInt("id");
		m.meaningOrder = rs.getInt("meaningOrder");
		m.meaning = rs.getString("meaning");
		// Now PARSE and POPULATE all of the arrays of "attributes"
		EdictMeaning mean = EdictParser.createMeaning(m.meaningOrder, m.meaning);
		return mean;
	}

	public EdictEntry readEntryFull(Integer entryId) throws SQLException {
		EdictEntry ret = readEntry(entryId);
		if (ret == null) {
			System.err.println("Entry for entryId=" + entryId + " not found");
			return ret;
		}
		ArrayList<EdictTerm> termArray = readTerms(entryId);
		if (termArray == null) {
			System.err.println("Terms for entryId=" + entryId + " not found");
			return null;
		}
		ret.terms = termArray;
		ArrayList<EdictDefinition> defArray = readDefinitions(entryId);
		if (defArray == null) {
			System.err.println("Definitions for entryId=" + entryId + " not found");
			return null;
		}
		for (EdictDefinition d : defArray) {
			ArrayList<EdictMeaning> meaningArray = readMeanings(d.id);
			if (meaningArray == null) {
				System.err.println("Meanings for defId=" + d.id + " not found");
				return null;
			}
			d.meanings = meaningArray;
		}
		ret.definitions = defArray;
		return ret;
	}
}
