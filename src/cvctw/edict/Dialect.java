/**
 * 
 */
package cvctw.edict;

/**
 * @author minge
 *
 */
public class Dialect {
	static final private String[] vals = {
		"hob",
		"ksb",
		"ktb",
		"kyb",
		"kyu",
		"nab",
		"osb",
		"rkb",
		"std",
		"thb",
		"tsb",
		"tsug"
	};

	/**
	 * 
	 * @param inVal
	 * @return
	 */
	static public boolean isDialect(String inVal) {
		boolean ret = false;
		for (int i = 0; i < vals.length; i++) {
			if (vals[i].equals(inVal)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
