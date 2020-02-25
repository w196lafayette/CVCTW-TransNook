/**
 * 
 */
package cvctw.edict;

/**
 * <p>
 * <p>
 * This class 
 * <ul>
 * <li>
 * declares the valid Contexts recognized by the JMdict/EDICT file
 * </li>
 * <li>
 * and provides a method to validate that a candidate value is a Context
 * </li>
 * </ul>
 * </p>
 * 
 * @author minge
 *
 */
public class Context {
	static final private String[] vals = {
			"anat",
			"archit",
			"astron",
			"baseb",
			"biol",
			"bot",
			"Buddh",
			"bus",
			"chem",
			"Christn",
			"comp",
			"econ",
			"engr",
			"finc",
			"food",
			"geol",
			"geom",
			"law",
			"ling",
			"MA",
			"mahj",
			"math",
			"mil",
			"music",
			"physics",
			"Shinto",
			"shogi",
			"sports",
			"sumo",
			"zool"
	};

	/**
	 * 
	 * @param inVal
	 * @return true if the inVal is a valid Context
	 */
	static public boolean isContext(String inVal) {
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
