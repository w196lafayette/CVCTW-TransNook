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
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Entry just created
		return newId;
	}
	public Integer writeTerm(EdictTerm t) throws SQLException {
		Integer realEntryId = t.entryId;
		if (t.entryId == null) {
			realEntryId = rowReader.readMaxId(TnProp.TABLE_ENTRIES) + 1;
		}
		// Grab only the first character of term type
		String termType = t.type.name().substring(0,1);
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_TERMS +
				" (entryId,term,alphabet,termType) VALUES (" +
				realEntryId + /* "," + realId + */ ",'" + t.term + "','" + t.alphabet + "','" + termType + "')";
		try {
			Integer newId = tnConn.tnExecuteUpdate(inst);
			// return the id of the Term just created
			return newId;
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			System.err.println("Term for Entry=" + realEntryId + " failed as DUPLICATE.  Term=" + t.term);
			System.err.println(e);
			return null;
		}
	}
	public Integer writeAttrToTerm(String table, String column, String attribute, Integer termId) throws SQLException {
		Integer realTermId = termId;
		if (termId == null) {
			realTermId = rowReader.readMaxId(TnProp.TABLE_TERMS) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + column + ",termId) VALUES (" +
				"'" + attribute + "'," + realTermId + ")";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Attribute 2 Term just created
		return newId;
	}
	public Integer writeDefinition(EdictDefinition d) throws SQLException {
		Integer realEntryId = d.entryId;
		if (d.entryId == null) {
			realEntryId = rowReader.readMaxId(TnProp.TABLE_ENTRIES) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_DEFINITIONS +
				" (entryId,defOrder,definition,language) VALUES (" +
				realEntryId + "," + d.defOrder + ",'" + d.definition + "','" + d.language + "')";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Definition just created
		return newId;
	}
	public Integer writeMeaning(EdictMeaning m) throws SQLException {
		Integer realDefId = m.defId;
		if (m.defId == null) {
			realDefId = rowReader.readMaxId(TnProp.TABLE_DEFINITIONS) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + TnProp.TABLE_MEANINGS +
				" (entryId,defId,meaningOrder,meaning) VALUES (" +
				m.entryId + "," + realDefId + "," + m.meaningOrder + ",'" + m.meaning + "')";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Meaning just created
		return newId;
	}

	public Integer writeAttrToMeaning(String table, String column, String attribute, Integer meaningId) 
			throws SQLException {
		Integer realMeaningId = meaningId;
		if (meaningId == null) {
			realMeaningId = rowReader.readMaxId(TnProp.TABLE_MEANINGS) + 1;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + column + ",meaningId) VALUES (" +
				"'" + attribute + "'," + realMeaningId + ")";
		Integer newId = tnConn.tnExecuteUpdate(inst);
		// return the id of the Attribute 2 Meaning just created
		return newId;
	}

	public Integer writeRow(String table, String column, String inVal, boolean isString) throws SQLException  {
		Integer rowId = null;
		String newVal = null;
		if (isString == true) {
			newVal = "'" + inVal + "'";
		} else {
			newVal = inVal;
		}
		String inst = "INSERT INTO " + TnProp.SCHEMA + "." + table +
				" (" + column + ") VALUES (" + newVal + ")";
		rowId = tnConn.tnExecuteUpdate(inst);
		return rowId;
	}
}
