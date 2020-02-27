/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

import cvctw.db.transnook.TnProp;

/**
 * <p>
 * This class 
 * <ul>
 * <li>
 * declares the valid Sense recognized by the JMdict/EDICT file
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
public class Sense implements EdictEnum {

	public enum SenseE {
		abbr,
		aphorism,
		arch,
		chn,
		col,
		company,
		dated,
		derog,
		eK,
		fam,
		fem,
		given,
		hist,
		hon,
		hum,
		id,
		joc,
		litf,
		m_sl,
		male,
		net_sl,
		obs,
		obsc,
		on_mim,
		organization,
		person,
		place,
		poet,
		pol,
		product,
		proverb,
		quote,
		rare,
		sens,
		sl,
		station,
		surname,
		uk,
		unclass,
		vulg,
		work,
		X,
		yoji
	}
   // List of all enums that have been enumsWritten to the database
    private static TreeSet<String> enumsWritten = null;

    public Sense() {
            enumsWritten = new TreeSet<String>();
    }
	public String getTag(String inVal) {
		return inVal;
	}
	public String getText(String inVal) {
		return "";
	}

    public boolean isValid(String inVal) {
    	for (SenseE v : SenseE.values()) {
    		if (inVal.equals(v.toString())) {
    			return true;
    		}
    	}
    	return false;
    }
    public String getTable() {
    	return TnProp.TABLE_SENSES;
    }
    public String getColumn() {
    	return TnProp.COLUMN_SENSES;
    }
    public String getToTable() {
    	return TnProp.TABLE_SENSES2MEANINGS;
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
