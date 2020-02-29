package cvctw.edict;

import java.util.ArrayList;

import cvctw.db.transnook.TnProp;

/**
 * 
 * This class represents Meanings, one of the four Java "entities" 
 * of the JMdict/EDICT Japanese-English dictionary.
 * <br>
 * The four "entities" are Entry, Term, Definition and Meaning.
 * <br>
 * Term "entities" may possess two optional "attributes" or features, KanjiInfo and ReadingInfo.
 * <br>
 * Meaning "entities" may possess eight optional "attributes" or features,
 * Context, PartOfSpeech, Sense, Dialect, AltLanguage, Wasei, Xref and Antonym.
 * 
 * @author minge
 *
 */
public class EdictMeaning {
	public Integer entryId;
	public Integer defId;
	public Integer id;
	public Integer meaningOrder;
	public String meaning;
	public ArrayList<String> contexts;
	public ArrayList<String> partsOfSpeech;
	public ArrayList<String> senses;
	public ArrayList<String> dialects;
	public ArrayList<String> altLanguages;
	public ArrayList<String> waseis;
	public ArrayList<String> xrefs;
	public ArrayList<String> antonyms;

	public EdictMeaning() {
	}

	public EdictMeaning(Integer order, String def) {
		this.meaningOrder = order;
		this.meaning = def;
		this.entryId = null;
		this.id = null;
		this.contexts = null;
		this.partsOfSpeech = null;
		this.senses = null;
		this.dialects = null;
		this.altLanguages = null;
		this.waseis = null;
		this.xrefs = null;
		this.antonyms = null;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public void setContext(ArrayList<String> context) {
		this.contexts = context;
	}

	public void setPartOfSpeech(ArrayList<String> partOfSpeech) {
		this.partsOfSpeech = partOfSpeech;
	}

	public void setSense(ArrayList<String> sense) {
		this.senses = sense;
	}

	public void setDialect(ArrayList<String> dialect) {
		this.dialects = dialect;
	}

	public void setAltLanguage(ArrayList<String> altLanguage) {
		this.altLanguages = altLanguage;
	}

	public void setWasei(ArrayList<String> wasei) {
		this.waseis = wasei;
	}

	public void setXref(ArrayList<String> xref) {
		this.xrefs = xref;
	}

	public void setAntonym(ArrayList<String> antonym) {
		this.antonyms = antonym;
	}

	public String meaningOnly() {
		String ret = "entryId" + entryId + ", defId=" + defId + ", id=" + id + ", meaningOrder=" + meaningOrder + 
				", meaning=" + meaning;
		return ret;
	}

	static public String getTable() {
		return TnProp.TABLE_MEANINGS;
	}

	static public String getParentTable() {
		return TnProp.TABLE_DEFINITIONS;
	}

	static public String getColumnList() {
		return " (entryId,defId,meaningOrder,meaning)";
	}

}
