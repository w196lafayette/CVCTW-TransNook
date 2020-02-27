/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * This class implements the methods of the EdictEnum interface.
 * <br>
 * See the EdictEnum javadoc item for a description of the purpose of each method.
 * 
 * @author minge
 *
 */
public class Context implements EdictEnum {

	public enum ContextE {
		anat,
		archit,
		astron,
		baseb,
		biol,
		bot,
		Buddh,
		bus,
		chem,
		Christn,
		comp,
		econ,
		engr,
		finc,
		food,
		geol,
		geom,
		law,
		ling,
		MA,
		mahj,
		math,
		mil,
		music,
		physics,
		Shinto,
		shogi,
		sports,
		sumo,
		zool
	};

	// List of all enums that have been Written to the database
	private static TreeSet<String> enumsWritten = null;

	public Context() {
		enumsWritten = new TreeSet<String>();
	}
	public String getTag(String inVal) {
		return inVal;
	}
	public String getText(String inVal) {
		return "";
	}

	public boolean isValid(String inVal) {
		for (ContextE v : ContextE.values()) {
			if (inVal.equals(v.toString())) {
				return true;
			}
		}
		return false;
	}
	public String getTable() {
		return TnProp.TABLE_CONTEXTS;
	}
	public String getColumn() {
		return TnProp.COLUMN_CONTEXTS;
	}
	public String getToTable() {
		return TnProp.TABLE_CONTEXTS2MEANINGS;
	}
	public String getToColumn() {
		return TnProp.COLUMN_MEANING_ID;
	}
	public boolean isWritten(String inVal) {
		if (enumsWritten.contains(inVal)) {
			return true;
		}
		return false;
	}
	public void written(String inVal) {
		enumsWritten.add(inVal);
	}

}
