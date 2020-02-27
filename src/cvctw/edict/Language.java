/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * <p>
 * This class implements the methods of the EdictEnum interface.
 * <br>
 * See the EdictEnum javadoc item for a description of the purpose of each method.
 * @author minge
 *
 */
public class Language implements EdictEnum {

	public enum LanguageE {
		jap,
		eng,
		afr,
		ain,
		alg,
		amh,
		ara,
		arn,
		bnt,
		bre,
		bul,
		bur,
		chi,
		chn,
		cze,
		dan,
		dut,
		epo,
		est,
		fil,
		fin,
		fre,
		geo,
		ger,
		glg,
		grc,
		gre,
		haw,
		heb,
		hin,
		hun,
		ice,
		ind,
		ita,
		khm,
		kor,
		kur,
		lat,
		mal,
		mao,
		mas,
		may,
		mnc,
		mol,
		mon,
		nor,
		per,
		pol,
		por,
		rum,
		rus,
		san,
		scr,
		slo,
		slv,
		som,
		spa,
		swa,
		swe,
		tah,
		tam,
		tha,
		tib,
		tur,
		urd,
		vie,
		yid
	}
	// List of all enums that have been enumsWritten to the database
	private static TreeSet<String> enumsWritten = null;

	public Language() {
		enumsWritten = new TreeSet<String>();
	}
	public String getTag(String inVal) {
		String[] sp = inVal.split(":");
		if (sp.length > 0) {
			return sp[0];
		}
		return inVal;
	}
	public String getText(String inVal) {
		String[] sp = inVal.split(":");
		if (sp.length > 1) {
			return sp[1];
		}
		return "";
	}

	public boolean isValid(String inVal) {
		String goodVal = getTag(inVal);
		for (LanguageE v : LanguageE.values()) {
			if (goodVal.equals(v.toString())) {
				return true;
			}
		}
		return false;
	}
	public String getTable() {
		return TnProp.TABLE_LANGUAGES;
	}
	public String getColumn() {
		return TnProp.COLUMN_LANGUAGES;
	}
	public String getToTable() {
		return TnProp.TABLE_ALTLANGUAGES2MEANINGS;
	}
	public String getToColumn() {
		return TnProp.COLUMN_MEANING_ID;
	}
	public boolean isWritten(String inVal) {
		if (enumsWritten.contains(inVal)) {
			return true;
		}
		return false;
	}
	public void written(String inVal) {
		enumsWritten.add(inVal);
	}
}
