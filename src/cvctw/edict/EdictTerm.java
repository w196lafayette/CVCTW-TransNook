package cvctw.edict;

public class EdictTerm {
	public Integer entryId;
	public Integer id;
	public String term;
	public Alphabet alphabet;
	
	public EdictTerm() {
		entryId = null;
		id = null;
		term = null;
		alphabet = null;
	}

	public EdictTerm(String term, Alphabet alphabet) {
		this.term = term;
		this.alphabet = alphabet;
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
				", term=" + term + ", alphabet=" + alphabet;
		return ret;
	}

}
