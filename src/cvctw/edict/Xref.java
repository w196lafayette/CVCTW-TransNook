/**
 * 
 */
package cvctw.edict;

/**
 * Xref, meaning "cross reference"
 *
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
