/**
 * 
 */
package cvctw.edict;

/**
 * <p>
 * This class 
 * <ul>
 * <li>
 * declares the valid Senses recognized by the JMdict/EDICT file
 * </li>
 * <li>
 * and provides a method to validate that a candidate values is a Sense
 * </li>
 * </ul>
 * </p>
 * 
 * @author minge
 *
 */
public class Sense {
	static final private String[] vals = {
	"abbr",
	"aphorism",
	"arch",
	"chn",
	"col",
	"company",
	"dated",
	"derog",
	"eK",
	"fam",
	"fem",
	"given",
	"hist",
	"hon",
	"hum",
	"id",
	"joc",
	"litf",
	"m-sl",
	"male",
	"net-sl",
	"obs",
	"obsc",
	"on-mim",
	"organization",
	"person",
	"place",
	"poet",
	"pol",
	"product",
	"proverb",
	"quote",
	"rare",
	"sens",
	"sl",
	"station",
	"surname",
	"uk",
	"unclass",
	"vulg",
	"work",
	"X",
	"yoji"
	};

	/**
	 * 
	 * @param inVal
	 * @return true if the inVal is a valid Sense
	 */
	static public boolean isSense(String inVal) {
		boolean ret = false;
		for (int i = 0; i < vals.length; i++) {
			if (! vals[i].contains(";")) {
				if (vals[i].equals(inVal)) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

}
