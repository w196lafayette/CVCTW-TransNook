/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * This class represents the Alphabet "attribute" and implements the methods of the EdictEnum interface.
 * <br>
 * See the EdictEnum javadoc item for a description of the purpose of each method.
 * @author minge
 *
 */
public class Alphabet implements EdictEnum {
	public enum AlphabetE {
		kanji,
		hiragana,
		katakana,
		chinese,
		latin,
		greek,
		cyrillic,
		armenian,
		georgian,
		hangul,
		arabic,
		hebrew,
		korean,
		egyptian_hieroglyphic,
		mongolian,
		coptic
	}
	// List of all enums that have been enumsWritten to the database
	private static TreeSet<String> enumsWritten = null;

	public Alphabet() {
		enumsWritten = new TreeSet<String>();
	}

	@Override
	public String getTag(String inVal) {
		return "";
	}

	@Override
	public String getText(String inVal) {
		return "";
	}

	@Override
	public String getTable() {
		return TnProp.TABLE_ALPHABETS;
	}

	@Override
	public String getColumn() {
		return TnProp.COLUMN_ALPHABETS;
	}

	@Override
	public String getToTable() {
		return "";
	}

	@Override
	public String getToColumn() {
		return "";
	}

	@Override
	public boolean isValid(String inVal) {
		String goodVal = getTag(inVal);
		for (AlphabetE v : AlphabetE.values()) {
			if (goodVal.equals(v.toString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isWritten(String inVal) {
		if (enumsWritten.contains(inVal)) {
			return true;
		}
		return false;
	}

	@Override
	public void written(String inVal) {
		enumsWritten.add(inVal);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
