/**
 * 
 */
package cvctw.edict;

import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * <p>
 * Xref, meaning "cross reference"
 * </p>
 * <p>
 * This class 
 * <ul>
 * <li>declares the valid Xrefs recognized by the JMdict/EDICT file</li>
 * <li>and provides a method to validate that a candidate value is an Xref</li>
 * </ul>
 * </p>
 * <p>
 * The two valid formats of Xrefs in the JMdict/EDICT file are
 * <ul>
 * <li>"cf. [text]"</li>
 * <li>"See [text]"</li>
 * </ul>
 * </p>
 * @author minge
 *
 */
public class Xref implements EdictEnum {

	public enum XrefE {
		//      ant,    // Handled in a separate class/method
		cf,
		ex,
		kvar,
		//      pref,   // is also a part of speech.  Can't distinguish the two uses.  And this usage never occurs YET.
		//      see,    // Can't distinguish this usage from common, non-analytic usage.
		See,
		syn,
		uses
	}
	// List of all enums that have been enumsWritten to the database
	private static TreeSet<String> enumsWritten = null;

	public Xref() {
		enumsWritten = new TreeSet<String>();
	}
	public String getTag(String inVal) {
		if (inVal.length() < 3) {
			return "";
		}
		String goodVal = inVal.substring(0, 3);
		goodVal = goodVal.replaceAll("\\.", "");
		return goodVal;
	}
	public String getText(String inVal) {
		if (inVal.length() < 5) {
			return "";
		}
		String attrText = inVal.substring(4);
		return attrText;
	}
	// "cf. " and "See " must be parsed before being tested
	public boolean isValid(String inVal) {
		String goodVal;
		if (inVal.length() < 5) {
			goodVal = inVal;
		} else {
			goodVal = getTag(inVal);
		}
		for (XrefE v : XrefE.values()) {
			if (goodVal.equals(v.toString())) {
				return true;
			}
		}
		return false;
	}
	public String getTable() {
		return TnProp.TABLE_XREFS;
	}
	public String getColumn() {
		return TnProp.COLUMN_XREFS;
	}
	public String getToTable() {
		return TnProp.TABLE_XREFS2MEANINGS;
	}
	public String getToColumn() {
		return TnProp.COLUMN_MEANING_ID;
	}
	// "cf. " and "See " must be parsed before being tested
	public boolean isWritten(String inVal) {
		String goodVal = getTag(inVal);
		if (enumsWritten.contains(goodVal)) {
			return true;
		}
		return false;
	}
	// "cf. " and "See " must be parsed before being tested
	public void written(String inVal) {
		String goodVal = getTag(inVal);
		enumsWritten.add(goodVal);
	}
	public static void main(String[] args) throws IOException, SQLException, Exception {
		Xref x = new Xref();
		String s = "See the other side";
		String c = "cf. the other item";
		System.out.println(x.getTag(s) + ", " + x.getText(s));
		System.out.println(x.getTag(c) + ", " + x.getText(c));
	}
}
