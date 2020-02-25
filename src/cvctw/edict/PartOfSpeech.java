/**
 * 
 */
package cvctw.edict;

/**
 * <p>
 * This class 
 * <ul>
 * <li>
 * declares the valid Parts-of-Speech recognized by the JMdict/EDICT file
 * </li>
 * <li>
 * and provides a method to validate that a candidate values is a Part-of-Speech
 * </li>
 * </ul>
 * </p>
 * 
 * @author minge
 *
 */
public class PartOfSpeech {
	static final private String[] vals = {
	"adj-f",
	"adj-i",
	"adj-ix",
	"adj-kari",
	"adj-ku",
	"adj-na",
	"adj-nari",
	"adj-no",
	"adj-pn",
	"adj-shiku",
	"adj-t",
	"adv",
	"adv-to",
	"aux",
	"aud-adj",
	"aux-v",
	"conj",
	"cop",
	"ctr",
	"exp",
	"Int",
	"n",
	"n-adv",
	"n-pr",
	"n-pref",
	"n-suf",
	"n-t",
	"num",
	"pn",
	"pref",
	"prt",
	"suf",
	"unc",
	"v-unspec",
	"v1",
	"v1-s",
	"v2a-s",
	"v2b-k",
	"v2b-s",
	"v2d-k",
	"v2d-s",
	"v2g-k",
	"v2g-s",
	"v2h-k",
	"v2h-s",
	"v2k-k",
	"v2k-s",
	"v2m-k",
	"v2m-s",
	"v2n-s",
	"v2r-k",
	"v2r-s",
	"v2s-s",
	"v2t-k",
	"v2t-s",
	"v2w-s",
	"v2y-k",
	"v2y-s",
	"v2z-s",
	"v4b",
	"v4g",
	"v4h",
	"v4k",
	"v4m",
	"v4n",
	"v4r",
	"v4s",
	"v4t",
	"v5aru",
	"v5b",
	"v5g",
	"v5k",
	"v5k-s",
	"v5m",
	"v5n",
	"v5r",
	"v5r-i",
	"v5s",
	"v5t",
	"v5u",
	"v5u-s",
	"v5uru",
	"vi",
	"vk",
	"vn",
	"vr",
	"vs",
	"vs-c",
	"vs-i",
	"vs-s",
	"vt",
	"vz"
	};

	/**
	 * 
	 * @param inVal
	 * @return true if the inVal is a valid Part-of-Speech
	 */
	static public boolean isPartOfSpeech(String inVal) {
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
