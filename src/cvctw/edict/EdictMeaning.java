package cvctw.edict;

import java.util.ArrayList;

public class EdictMeaning {
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
		String ret = "defId=" + defId + ", id=" + id + ", meaningOrder=" + meaningOrder + 
				", meaning=" + meaning;
		return ret;
	}

}
