/**
 * 
 */
package cvctw.edict;

/**
 * @author minge
 *
 */
public class Language {
	static final private String[] vals = {
	"jap",
	"eng",
	"afr",
	"ain",
	"alg",
	"amh",
	"ara",
	"arn",
	"bnt",
	"bre",
	"bul",
	"bur",
	"chi",
	"chn",
	"cze",
	"dan",
	"dut",
	"epo",
	"est",
	"fil",
	"fin",
	"fre",
	"geo",
	"ger",
	"glg",
	"grc",
	"gre",
	"haw",
	"heb",
	"hin",
	"hun",
	"ice",
	"ind",
	"ita",
	"khm",
	"kor",
	"kur",
	"lat",
	"mal",
	"mao",
	"mas",
	"may",
	"mnc",
	"mol",
	"mon",
	"nor",
	"per",
	"pol",
	"por",
	"rum",
	"rus",
	"san",
	"scr",
	"slo",
	"slv",
	"som",
	"spa",
	"swa",
	"swe",
	"tah",
	"tam",
	"tha",
	"tib",
	"tur",
	"urd",
	"vie",
	"yid"
	};
	
	static public boolean isLanguage(String inVal) {
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
