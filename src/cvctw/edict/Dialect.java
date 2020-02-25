/**
 * 
 */
package cvctw.edict;

/**
 * <p>
 * This class 
 * <ul>
 * <li>
 * declares the valid Dialects recognized by the JMdict/EDICT file
 * </li>
 * <li>
 * and provides a method to validate that a candidate values is a Dialect
 * </li>
 * </ul>
 * </p>
 * 
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
	 * @return true if the inVal is a valid Dialect
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
