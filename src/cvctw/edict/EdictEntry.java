/**
 * 
 */
package cvctw.edict;

import java.util.ArrayList;

import cvctw.db.transnook.TnProp;

/**
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
	public Source source;

	public EdictEntry() {
	}

	public void init(EdictEntry e) {
		e.entry = null;
		e.terms = new ArrayList<EdictTerm>();
		e.definitions = new ArrayList<EdictDefinition>();
		e.id = null;
		e.language = TnProp.DEFAULT_TERM_LANGUAGE;
		e.source = Source.E;
	}

	public EdictEntry(String entry) {
		init(this);
		this.entry = entry;
		ArrayList<EdictTerm> termL = EdictParser.getTerm(entry);
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
				", source=" + source;
		return ret;
	}

	public String toString() {
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

}
