package cvctw.edict;

import java.util.ArrayList;

import cvctw.db.transnook.TnProp;
/**
 * This class represents Definitions, one of the four Java "entities" 
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
public class EdictDefinition {
	public Integer entryId;
	public Integer id;
	public Integer defOrder;
	// Language of the definition, not the language of the terms
	public String language;
	public String definition;
	public ArrayList<EdictMeaning> meanings;

	public EdictDefinition() {
	}

	public EdictDefinition(Integer order, String def) {
		this.defOrder = order;
		this.definition = def;
		this.entryId = null;
		this.id = null;
		this.language = "eng";
		this.meanings = new ArrayList<EdictMeaning>();
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String definitionOnly() {
		String ret = "entryId=" + entryId + ", id=" + id + ", defOrder=" + defOrder + 
				", language=" + language + ", definition=" + definition;
		return ret;
	}

	static public String getTable() {
		return TnProp.TABLE_DEFINITIONS;
	}

	static public String getParentTable() {
		return TnProp.TABLE_ENTRIES;
	}

	static public String getColumnList() {
		return " (entryId,defOrder,definition,language)";
	}
}
