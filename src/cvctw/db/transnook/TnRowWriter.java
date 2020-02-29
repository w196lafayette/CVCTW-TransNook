/**
 * 
 */
package cvctw.db.transnook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import cvctw.edict.EdictDefinition;
import cvctw.edict.EdictEntry;
import cvctw.edict.EdictMeaning;
import cvctw.edict.EdictTerm;

/**
 * This class provides 
 * <ul>
 * <li>methods to write (SQL INSERT) records to the four "entities" tables, namely,
 * ENTRIES, TERMS, DEFINITIONS and MEANINGS.</li>
 * <li>a general-purpose method to write records to the requested table.</li>
 * 
 * </ul>
 * 
 * @author minge
 *
 */
public class TnRowWriter {

	private TnRowReader rowReader = null;
	private TnConnection tnConn = null;

	public TnRowWriter() throws SQLException, FileNotFoundException, IOException {
		this.tnConn = TnConnection.getInstance();
		this.rowReader = new TnRowReader();
	}

	public Integer writeEntry(EdictEntry e, char source) throws SQLException {
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + EdictEntry.getTable() + 
				EdictEntry.getColumnList() + " VALUES ('" +
				e.entry + "','" + e.language + "','" + e.eDictId + "','" + source + "')";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Entry just created
		return newId;
	}
	public Integer writeTerm(EdictTerm t) throws SQLException {
		Integer realEntryId = t.entryId;
		if (t.entryId == null) {
			realEntryId = rowReader.readMaxId(EdictTerm.getParentTable()) + 1;
		}
		// Grab only the first character of term type
		String termType = t.type.name().substring(0,1);
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + EdictTerm.getTable() +
				EdictTerm.getColumnList() + " VALUES (" +
				realEntryId + /* "," + realId + */ ",'" + t.term + "','" + t.alphabetE + "','" + termType + "')";
//		try {
			Integer newId = tnConn.tnExecuteUpdate(inst);
			// return the id of the Term just created
			return newId;
//		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
//			System.err.println("Term for Entry=" + realEntryId + " failed as DUPLICATE.  Term=" + t.term);
//			System.err.println(e);
//			return null;
//		}
	}
	public Integer writeDefinition(EdictDefinition d) throws SQLException {
		Integer realEntryId = d.entryId;
		if (d.entryId == null) {
			realEntryId = rowReader.readMaxId(EdictDefinition.getParentTable()) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + EdictDefinition.getTable() + 
				EdictDefinition.getColumnList() + " VALUES (" +
				realEntryId + "," + d.defOrder + ",'" + d.definition + "','" + d.language + "')";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Definition just created
		return newId;
	}
	public Integer writeMeaning(EdictMeaning m) throws SQLException {
		Integer realDefId = m.defId;
		if (m.defId == null) {
			realDefId = rowReader.readMaxId(EdictMeaning.getParentTable()) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + EdictMeaning.getTable() + 
				EdictMeaning.getColumnList() + " VALUES (" +
				m.entryId + "," + realDefId + "," + m.meaningOrder + ",'" + m.meaning + "')";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Meaning just created
		return newId;
	}

	public Integer writeAttrToMeaning(String table, String column, String attribute, Integer meaningId) 
			throws SQLException {
		Integer realMeaningId = meaningId;
		if (meaningId == null) {
			realMeaningId = rowReader.readMaxId(EdictMeaning.getTable()) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + column + ",meaningId) VALUES (" +
				"'" + attribute + "'," + realMeaningId + ")";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Attribute 2 Meaning just created
		return newId;
	}

	public Integer writeRow(String table, String columnList, String valueList, boolean isString) throws SQLException   {
		Integer rowId = null;
		String newVal = null;
		if (isString == true) {
			newVal = "'" + valueList + "'";
		} else {
			newVal = valueList;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + columnList + ") VALUES (" + newVal + ")";
		try {
		rowId = tnConn.tnExecuteUpdate(inst);
		} catch (SQLException e) {
			System.out.println ("INSERT failed: " + inst);
			throw e;
		}
		return rowId;
	}
}
