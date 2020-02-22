/**
 * 
 */
package cvctw.edict;

/**
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
