/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * This class represents the Part of Speech "attribute" and implements the methods of the EdictEnum interface.
 * <br>
 * See the EdictEnum javadoc item for a description of the purpose of each method.
 * @author minge
 *
 */
public class PartOfSpeech implements EdictEnum {

	public enum PartOfSpeechE {
		adj_f,
		adj_i,
		adj_ix,
		adj_kari,
		adj_ku,
		adj_na,
		adj_nari,
		adj_no,
		adj_pn,
		adj_shiku,
		adj_t,
		adv,
		adv_to,
		aux,
		aud_adj,
		aux_v,
		conj,
		cop,
		ctr,
		exp,
		Int,
		n,
		n_adv,
		n_pr,
		n_pref,
		n_suf,
		n_t,
		num,
		pn,
		pref,
		prt,
		suf,
		unc,
		v_unspec,
		v1,
		v1_s,
		v2a_s,
		v2b_k,
		v2b_s,
		v2d_k,
		v2d_s,
		v2g_k,
		v2g_s,
		v2h_k,
		v2h_s,
		v2k_k,
		v2k_s,
		v2m_k,
		v2m_s,
		v2n_s,
		v2r_k,
		v2r_s,
		v2s_s,
		v2t_k,
		v2t_s,
		v2w_s,
		v2y_k,
		v2y_s,
		v2z_s,
		v4b,
		v4g,
		v4h,
		v4k,
		v4m,
		v4n,
		v4r,
		v4s,
		v4t,
		v5aru,
		v5b,
		v5g,
		v5k,
		v5k_s,
		v5m,
		v5n,
		v5r,
		v5r_i,
		v5s,
		v5t,
		v5u,
		v5u_s,
		v5uru,
		vi,
		vk,
		vn,
		vr,
		vs,
		vs_c,
		vs_i,
		vs_s,
		vt,
		vz
	}
    // List of all enums that have been enumsWritten to the database
    private static TreeSet<String> enumsWritten = null;

    public PartOfSpeech() {
            enumsWritten = new TreeSet<String>();
    }
	public String getText(String inVal) {
		return "";
	}
	public String getTag(String inVal) {
		return inVal;
	}

    public boolean isValid(String inVal) {
            for (PartOfSpeechE v : PartOfSpeechE.values()) {
                    if (inVal.equals(v.toString())) {
                            return true;
                    }
            }
            return false;
    }
    public String getTable() {
            return TnProp.TABLE_PARTSOFSPEECH;
    }
    public String getColumn() {
            return TnProp.COLUMN_PARTSOFSPEECH;
    }
    public String getToTable() {
            return TnProp.TABLE_PARTSOFSPEECH2MEANINGS;
    }
    public String getToColumn() {
            return TnProp.COLUMN_MEANING_ID;
    }
    public boolean isWritten(String inVal) {
    	String fixed = inVal.replaceAll("-", "_");
    	if (enumsWritten.contains(fixed)) {
    		return true;
    	}
    	return false;
    }
    public void written(String inVal) {
       	String fixed = inVal.replaceAll("-", "_");
    	enumsWritten.add(fixed);
    }
}
