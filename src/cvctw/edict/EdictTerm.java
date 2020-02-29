package cvctw.edict;

import java.util.ArrayList;

import cvctw.db.transnook.TnProp;

/**
 * This class represents Terms, one of the four Java "entities" 
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
public class EdictTerm {
	public enum EdictTermType {
		major,
		alternate
	}
	public Integer entryId;
	public Integer id;
	public String term;
	public Alphabet.AlphabetE alphabetE;
	public EdictTermType type;
	public ArrayList<String> readingInfo;
	public ArrayList<String> kanjiInfo;
	
	public EdictTerm() {
		entryId = null;
		id = null;
		term = null;
		alphabetE = null;
		type = null;
	}

	public EdictTerm(String term, Alphabet.AlphabetE alphabetE) {
		this.term = term;
		this.alphabetE = alphabetE;
		entryId = null;
		id = null;
	}

	public void setEntryId(Integer parentId) {
		this.entryId = parentId;
	}

	public void setId(Integer termId) {
		this.id = termId;
	}

	public String toString() {
		String ret = "entryId=" + entryId + ", id=" + id + 
				", term=" + term + ", alphabetE=" + alphabetE + ", type=" + type;
		return ret;
	}

	static public String getTable() {
		return TnProp.TABLE_TERMS;
	}

	static public String getParentTable() {
		return TnProp.TABLE_ENTRIES;
	}

	static public String getColumnList() {
		return " (entryId,term,alphabet,termType)";
	}

}
