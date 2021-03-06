/**
 * 
 */
package cvctw.edict;

import java.util.ArrayList;

import cvctw.db.transnook.TnProp;

/**
 * This class represents Entries, one of the four Java "entities" 
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
public class EdictEntry {
	public Integer id;
	// The entire, unmodified entry
	public String entry;
	// Language of the terms, not the language of the definitions
	public String language;
	public ArrayList<EdictTerm> terms;
	public ArrayList<EdictDefinition> definitions;
	public String eDictId;
	public Source.SourceE sourceE;

	public EdictEntry() {
	}

	public void init(EdictEntry e) {
		e.entry = null;
		e.terms = new ArrayList<EdictTerm>();
		e.definitions = new ArrayList<EdictDefinition>();
		e.id = null;
		e.language = TnProp.DEFAULT_TERM_LANGUAGE;
		e.sourceE = Source.SourceE.E;
	}

	public EdictEntry(String entry) {
		init(this);
		this.entry = entry;
		ArrayList<EdictTerm> termL = EdictParser.getTerms(entry);
		for (EdictTerm t : termL) {
			this.terms.add(t);
		}
		ArrayList<EdictTerm> altTerms = EdictParser.getAltTerms(entry);
		if (altTerms != null) {
			for (EdictTerm t : altTerms) {
				this.terms.add(t);
			}
		}
		ArrayList<EdictDefinition> defs = EdictParser.getDefinitions(entry);
		for (EdictDefinition d : defs) {
//			ArrayList<EdictMeaning> meanings = EdictParser.getMeanings(d.definition);
//			for (EdictMeaning m : meanings) {
//				d.meanings.add(m);
//			}
			this.definitions.add(d);
		}
		this.eDictId = EdictParser.getEdictId(entry);
	}

	public String entryOnly() {
		String ret = "id=" + id + ", entry=" + entry + 
				", language=" + language + ", eDictId=" + eDictId +
				", sourceE=" + sourceE;
		return ret;
	}

	public String fullToString() {
		String ret = "entry=" + entry + "\n";
		for (EdictTerm t : terms) {
			ret = ret + "term=" + t.term + ",";
		}
		ret = ret + "\n";
		for (EdictDefinition d : definitions) {
			ret += "defOrder=" + d.defOrder + ", ";
			ret = ret + "def=" + d.definition;
			for (EdictMeaning m : d.meanings) {
				ret += "meaningOrder=" + m.meaningOrder + ", ";
				ret = ret + "meaning=" + m.meaning;
				if (m.contexts != null) {
					for (String c : m.contexts) {
						ret += ", contexts=" + c;
					}
				}
				if (m.partsOfSpeech != null) {
					for (String p : m.partsOfSpeech) {
						ret += ", partsOfSpeech=" + p;
					}
				}
				if (m.senses != null) {
					for (String s : m.senses) {
						ret += ", senses=" + s;
					}
				}
				if (m.dialects != null) {
					for (String s : m.dialects) {
						ret += ", dialects=" + s;
					}
				}
				if (m.altLanguages != null) {
					for (String s : m.altLanguages) {
						ret += ", altLanguages=" + s;
					}
				}
				if (m.waseis != null) {
					for (String s : m.waseis) {
						ret += ", waseis=" + s;
					}
				}
				if (m.antonyms != null) {
					for (String s : m.antonyms) {
						ret += ", antonyms=" + s;
					}
				}
				if (m.xrefs != null) {
					for (String s : m.xrefs) {
						ret += ", xrefs=" + s;
					}
				}
				ret += "\n";
			}
			ret += "\n";
		}
		ret = ret + "eDictId=" + eDictId;
		return ret;
	}

	static public String getTable() {
		return TnProp.TABLE_ENTRIES;
	}

	static public String getColumnList() {
		return " (entry,language,eDictId,source)";
	}

}
