/**
 * 
 */
package cvctw.edict;

/**
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
	 * @return
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
