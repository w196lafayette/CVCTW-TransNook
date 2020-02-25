/**
 * 
 */
package cvctw.edict;

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
public class Xref {
	static final private String[] vals = {
//	"ant",	// Handled in a separate class/method
	"cf. ",
	"ex ",
	"kvar ",
//	"pref",	// is also a part of speech.  Can't distinguish the two uses.  And this usage never occurs YET.
//	"see",	// Can't distinguish this usage from common, non-analytic usage.
	"See ",
	"syn ",
	"uses "
	};

	/**
	 * 
	 * @param inVal
	 * @return true if the inVal is a valid Xref (cross reference)
	 */
	static public boolean isXref(String inVal) {
		boolean ret = false;
		for (int i = 0; i < vals.length; i++) {
			if (inVal.startsWith(vals[i])) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
