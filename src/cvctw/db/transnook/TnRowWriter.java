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
 * @author minge
 *
 */
public class TnRowWriter {

	TnRowReader rowReader = null;
	TnConnection tnConn = null;

	public TnRowWriter() throws SQLException, FileNotFoundException, IOException {
		this.tnConn = TnConnection.getInstance();
		this.rowReader = new TnRowReader();
	}

	public Integer writeEntry(EdictEntry e, char source) throws SQLException {
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_ENTRIES + 
				" (entry,language,eDictId,source) VALUES ('" +
				e.entry + "','" + e.language + "','" + e.eDictId + "','" + source + "')";
		tnConn.tnExecuteStatement(inst, TnStatementType.UPDATE);
//		System.out.println("new INSERT: " + inst);
//		Statement st = conn.createStatement();
//		int stat = st.executeUpdate(inst);
//		if (stat != 1) {
//			throw new SQLException("INSERT failed");
//		}
		// return the id of the Entry just created
		Integer newId = rowReader.readMaxId(TnProp.TABLE_ENTRIES);
		return newId;
	}
	public Integer writeTerm(EdictTerm t) throws SQLException {
		Integer realEntryId = t.entryId;
		if (t.entryId == null) {
			realEntryId = rowReader.readMaxId(TnProp.TABLE_ENTRIES) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_TERMS +
				" (entryId,term,alphabet) VALUES (" +
				realEntryId + /* "," + realId + */ ",'" + t.term + "','" + t.alphabet + "')";
//		System.out.println("new INSERT: " + inst);
//		Statement st = conn.createStatement();
		try {
			tnConn.tnExecuteStatement(inst, TnStatementType.UPDATE);
			// return the id of the Term just created
			Integer newId = rowReader.readMaxId(TnProp.TABLE_TERMS);
			return newId;
//			int stat = st.executeUpdate(inst);
//			if (stat != 1) {
//				throw new SQLException("INSERT failed");
//			}
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			System.err.println("Term for Entry=" + realEntryId + " failed as DUPLICATE.  Term=" + t.term);
			System.err.println(e);
			return null;
		}
	}
	public Integer writeDefinition(EdictDefinition d) throws SQLException {
		Integer realEntryId = d.entryId;
		if (d.entryId == null) {
			realEntryId = rowReader.readMaxId(TnProp.TABLE_ENTRIES) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_DEFINITIONS +
				" (entryId,defOrder,definition,language) VALUES (" +
				realEntryId + "," + d.defOrder + ",'" + d.definition + "','" + d.language + "')";
		tnConn.tnExecuteStatement(inst, TnStatementType.UPDATE);
//		System.out.println("new INSERT: " + inst);
//		Statement st = conn.createStatement();
//		int stat = st.executeUpdate(inst);
//		if (stat != 1) {
//			throw new SQLException("INSERT failed");
//		}
		// return the id of the Definition just created
		Integer newId = rowReader.readMaxId(TnProp.TABLE_DEFINITIONS);
		return newId;
	}
	public Integer writeMeaning(EdictMeaning m) throws SQLException {
		Integer realDefId = m.defId;
		if (m.defId == null) {
			realDefId = rowReader.readMaxId(TnProp.TABLE_DEFINITIONS) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_MEANINGS +
				" (defId,meaningOrder,meaning) VALUES (" +
				realDefId + "," + m.meaningOrder + ",'" + m.meaning + "')";
		tnConn.tnExecuteStatement(inst, TnStatementType.UPDATE);
//		System.out.println("new INSERT: " + inst);
//		Statement st = conn.createStatement();
//		int stat = st.executeUpdate(inst);
//		if (stat != 1) {
//			throw new SQLException("INSERT failed");
//		}
		// return the id of the Definition just created
		Integer newId = rowReader.readMaxId(TnProp.TABLE_MEANINGS);
		return newId;
	}

	public Integer writeAttrToMeaning(String table, String column, String attribute, Integer meaningId) throws SQLException {
		Integer realMeaningId = meaningId;
		if (meaningId == null) {
			realMeaningId = rowReader.readMaxId(TnProp.TABLE_MEANINGS) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + column + ",meaningId) VALUES (" +
				"'" + attribute + "'," + realMeaningId + ")";
		tnConn.tnExecuteStatement(inst, TnStatementType.UPDATE);
//		System.out.println("new INSERT: " + inst);
//		Statement st = conn.createStatement();
//		int stat = st.executeUpdate(inst);
//		if (stat != 1) {
//			throw new SQLException("INSERT failed");
//		}
		// return the id of the Attribute to Meaning just created
		Integer newId = rowReader.readMaxId(table);
		return newId;
	}

	public void writeRow(String table, String column, String inVal, boolean isString) throws SQLException  {
		String newVal = null;
		if (isString == true) {
			newVal = "'" + inVal + "'";
		} else {
			newVal = inVal;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + column + ") VALUES (" + newVal + ")";
		tnConn.tnExecuteStatement(inst, TnStatementType.UPDATE);
//		System.out.println("new INSERT: " + inst);
//		try {
//			Statement st = conn.createStatement();
//			int stat = st.executeUpdate(inst);
//			if (stat != 1) {
//				throw new SQLException("INSERT failed");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.err.println("INSERT error for: " + inst);
//		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
