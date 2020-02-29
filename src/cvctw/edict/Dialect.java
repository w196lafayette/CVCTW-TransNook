/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * <p>
 * This class represents the Dialect "attribute" and implements the methods of the EdictEnum interface.
 * <br>
 * See the EdictEnum javadoc item for a description of the purpose of each method.
 * @author minge
 *
 */
public class Dialect implements EdictEnum {
	public enum DialectE {
		hob,
		ksb,
		ktb,
		kyb,
		kyu,
		nab,
		osb,
		rkb,
		std,
		thb,
		tsb,
		tsug
	}
	// List of all enums that have been enumsWritten to the database
	private static TreeSet<String> enumsWritten = null;

	public Dialect() {
		enumsWritten = new TreeSet<String>();
	}
	public String getTag(String inVal) {
		// Remove trailing colon
		String goodVal = inVal.replaceAll(":", "");
		return goodVal;
	}
	public String getText(String inVal) {
		return "";
	}

	public boolean isValid(String inVal) {
		// Remove trailing colon
		String goodVal = getTag(inVal);
		for (DialectE v : DialectE.values()) {
			if (goodVal.equals(v.toString())) {
				return true;
			}
		}
		return false;
	}
	public String getTable() {
		return TnProp.TABLE_DIALECTS;
	}
	public String getColumn() {
		return TnProp.COLUMN_DIALECTS;
	}
	public String getToTable() {
		return TnProp.TABLE_DIALECTS2MEANINGS;
	}
	public String getToColumn() {
		return TnProp.COLUMN_MEANING_ID;
	}
	public boolean isWritten(String inVal) {
		// Remove trailing colon
		String goodVal = getTag(inVal);
		if (enumsWritten.contains(goodVal)) {
			return true;
		}
		return false;
	}
	public void written(String inVal) {
		// Remove trailing colon
		String goodVal = getTag(inVal);
		enumsWritten.add(goodVal);
	}
}
