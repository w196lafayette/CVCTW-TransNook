/**
 * 
 */
package cvctw.edict;

import java.util.ArrayList;

/**
 * @author minge
 *
 */
public class EdictParser {

	static private Context eContext = new Context();
	static private ReadingInfo eReadingInfo = new ReadingInfo();
	static private KanjiInfo eKanjiInfo = new KanjiInfo();
	static private Sense eSense = new Sense();
	static private Dialect eDialect = new Dialect();
	static private Language eLanguage = new Language();
	static private PartOfSpeech ePartOfSpeech = new PartOfSpeech();
	static private Xref eXref = new Xref();
	static private Wasei eWasei = new Wasei();
	static private Antonym eAntonym = new Antonym();

	static public EdictTerm getAnyTerm(String entry) {
		// TO DO
		// TO DO: detect the alphabetE
		// TO DO
		EdictTerm ret = new EdictTerm(entry, Alphabet.AlphabetE.katakana);
		ArrayList<String> allP = getEnclosed(entry, "(", ")");
		ArrayList<String> riArray = getAttributes(eReadingInfo, allP);
		if (riArray != null) {
			ret.readingInfo = riArray;
		}
		ArrayList<String> kArray = getAttributes(eKanjiInfo, allP);
		if (kArray != null) {
			ret.kanjiInfo = kArray;
		}
		return ret;
	}

	// In the eDict the initial terms are always kanji.
	// Alternate terms are other alphabets
	static public ArrayList<EdictTerm> getTerms(String entry) {
		ArrayList<EdictTerm> ret = new ArrayList<EdictTerm>();
		String[] tokens = entry.split(" ");
		if (tokens.length > 1) {
			// if ";" not found, returns an array of one entry
			String[] terms = tokens[0].split(";");
			for (int i = 0; i < terms.length; i++) {
				EdictTerm t = getAnyTerm(terms[0]);
				t.type = EdictTerm.EdictTermType.major;
				ret.add(t);
			}
		}
		return ret;
	}

	static public ArrayList<EdictTerm> getAltTerms(String entry) {
		ArrayList<EdictTerm> ret = null;
		String[] tokens = entry.split(" ");
		if (tokens.length < 3) {
			return ret;
		}
		// alternate terms begin with square bracket
		String alt = tokens[1];
		if (! alt.startsWith("[")) {
			return ret;
		}
		// remove opening bracket
		alt = alt.substring(1);
		// alternate terms end with closing bracket OR with a space
		for (int i = 1; i < alt.length() - 1; i++) {
			String altT = alt.substring(i, i + 1);
			if (altT.equals(" ") || altT.equals("]")) {
				alt = alt.substring(0, i);
				break;
			}
		}
		ret = new ArrayList<EdictTerm>();
		// alternate terms are delimited by semicolon
		// if ";" not found, returns an array of one entry
		String[] tok = alt.split(";");
		for (String t : tok) {
			// TO DO: detect the alphabetE
			EdictTerm term = getAnyTerm(t.replace("]", ""));
			term.type = EdictTerm.EdictTermType.alternate;
			ret.add(term);
		}
		return ret;
	}

	/**
	 * Parts of speech may be multi-value, delimited by commas
	 * @param allP
	 * @param entry
	 * @return Returns a list of all parts of speech in the entry parameter
	 */
	static public ArrayList<String> getPartOfSpeech(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (p.contains(",")) {
				String[] pos = p.split(",");
				ArrayList<String> nPos = new ArrayList<String>();
				for (int i = 0; i < pos.length; i++) {
					nPos.add(pos[i]);
				}
				ArrayList<String> tRet = getPartOfSpeech(nPos, p);
				if (tRet != null && tRet.size() > 0) {
					ret = tRet;
				}
			} else if (ePartOfSpeech.isValid(p) == true) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	static public ArrayList<String> getAttributes(EdictEnum eEnum, ArrayList<String> allP) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (eEnum.isValid(p) == true) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	static public ArrayList<String> getAltLanguage(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (eLanguage.isValid(p)) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	/**
	 * <p>
	 * Wasei terms may have multiple values, delimited by a comma
	 * </p>
	 * <p>
	 * Wasei are Japanese-language expressions based on English words or parts of word combinations, 
	 * that do not exist in standard English or whose meanings differ from the words from which they were derived. 
	 * Linguistics classifies them as pseudo-loanwords or pseudo-anglicisms (src: Wikipedia)
	 * </p>
	 * @param allP
	 * @param entry
	 * @return Returns a list of all wasei in the entry parameter
	 */
	static public ArrayList<String> getWasei(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (eWasei.isValid(p)) {
				//			if (p.startsWith(WASEI + ":")) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				if (p.contains(",")) {
					String[] tW = p.split(",");
					for (int i = 0; i < tW.length; i++) {
						if (eWasei.isValid(tW[i])) {
							ret.add(eWasei.getText(tW[i]));
						} else {
							ret.add(tW[i]);
						}
					}
				} else {
					ret.add(p);
				}
			}
		}
		return ret;
	}

	static public ArrayList<String> getXref(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (eXref.isValid(p) == true) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	/**
	 * Antonyms may have multiple values, delimited by a comma
	 * This method removes the "ant: " prefix, leaving only the katakana term
	 * 
	 * @param allP
	 * @param entry
	 * @return list of all Antonyms found in this Meaning
	 */
	static public ArrayList<String> getAntonym(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (eAntonym.isValid(p)) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				p = eAntonym.getText(p);
				if (p.contains(",")) {
					String[] tA = p.split(",");
					for (int i = 0; i < tA.length; i++) {
						ret.add(tA[i]);
					}
				} else {
					ret.add(p);
				}
			}
		}
		return ret;
	}

	static public ArrayList<String> getEnclosed(String entry, String open, String close) {
		ArrayList<String> ret = new ArrayList<String>();
		int startIdx = 0;
		while (true) {
			startIdx = entry.indexOf(open, startIdx);
			if (startIdx < 0) {
				break;
			}
			int endIdx = entry.indexOf(close, startIdx + open.length());
			if (endIdx < 0) {
				break;
			}
			String encl = entry.substring(startIdx + 1, endIdx);
			ret.add(encl);
			startIdx = endIdx + close.length();
		}
		return ret;
	}

	static public ArrayList<Integer> slashIdx(String entry) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (int i = 1; i < entry.length(); i++) {
			if (entry.substring(i, i + 1).equals("/")) {
				ret.add(i);
			}
		}
		return ret;
	}
	static public String[] splitSlash (String entry) {
		String[] ret = entry.split("/");
		return ret;
	}

	static public int previousSlash(String entry, int beginIdx) {
		int ret = -1;
		for (int i = beginIdx - 1; i >=0; i--) {
			if (entry.substring(i, i + 1).equals("/")) {
				ret = i;
				break;
			}
		}
		return ret;
	}

	/**
	 * Calculates the index of all "marker" tokens
	 * @param entry
	 * @return array of "marker" indices
	 */
	static private ArrayList<Integer> defMarkers(String entry) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		// Find all tokens enclosed in parentheses.  Some will possibly be numeric markers,
		// and some will possibly be contexts or partsOfSpeech
		ArrayList<String> allP = getEnclosed(entry, "(", ")");
		// integer value of next marker
		int markerVal = 1;
		// Find the begin idx of all meaning numeric "meaningOrder" markers
		for (String t : allP) {
			try {
				Integer order = Integer.parseInt(t);
				if (order.equals(markerVal)) {
					int oIdx = entry.indexOf("(" + t + ")");
					ret.add(oIdx);
					markerVal++;
				// Some definitions have multiple markers with the same number, like (2) (2)
				} else if (order.equals(markerVal - 1)) {
					int oIdx = entry.indexOf("(" + t + ")");
					ret.add(oIdx);
				}
			} catch (Exception e) {
				; // nothing to do, move to next enclosed token
			}
		}
		return ret;
	}

	// Multiple definitions are delimited by a number in parentheses
	static private ArrayList<String> splitDefs(String entry) {
		ArrayList<String> ret = new ArrayList<String>();
		// Indices of the numeric "markers", not the delimiting slashes
		ArrayList<Integer> markersIdx = defMarkers(entry);
		// Indices of all slashes in the entry
		ArrayList<Integer> slashIdx = slashIdx(entry);

		// if there's only one meaning, grab it now and get out
		if (markersIdx.size() < 1) {
			int slashCt = slashIdx.size();
			if (slashCt > 2) {
				String def = entry.substring(slashIdx.get(0) + 1, slashIdx.get(slashCt - 2));
				ret.add(def);
				return ret;
			}
		}
		// for each numeric "marker", search back for the previous slash
		// and forward for the next marker's previous slash
		int markerCt = markersIdx.size();
		for (int markerIdx = 0; markerIdx < markerCt; markerIdx++) {
			int prev = previousSlash(entry, markersIdx.get(markerIdx));
			int next = 0;
			// if this is the last one, make it simple and stop
			if (markerIdx == markerCt - 1) {
				next = slashIdx.get(slashIdx.size() - 2);
			} else {
				// tricky: find the slash before the next "marker"
				int nextM = markersIdx.get(markerIdx + 1);
				next = previousSlash(entry, nextM + 1);
			}
			String def = entry.substring(prev + 1, next);
			ret.add(def);
		}
		return ret;
	}

	static private EdictDefinition createDefinition(int order, String def) {
		EdictDefinition ret = new EdictDefinition(order, def);
		ret.defOrder = order;
		return ret;
	}

	static public EdictMeaning createMeaning(int order, String meaning) {
		EdictMeaning ret = new EdictMeaning(order, meaning);

		// enclosed in parentheses
		ArrayList<String> allP = getEnclosed(meaning, "(", ")");
		// enclosed in curly brackets
		ArrayList<String> allB = getEnclosed(meaning, "{", "}");
	
		ArrayList<String> context = getAttributes(eContext, allB);
		ret.setContext(context);
		ArrayList<String> partOfSpeech = getPartOfSpeech(allP, meaning);
		ret.setPartOfSpeech(partOfSpeech);
		ArrayList<String> sense = getAttributes(eSense, allP);
		ret.setSense(sense);
		ArrayList<String> dialect = getAttributes(eDialect, allP);
		ret.setDialect(dialect);
		ArrayList<String> altLanguage = getAltLanguage(allP, meaning);
		ret.setAltLanguage(altLanguage);
		ArrayList<String> wasei = getWasei(allP, meaning);
		ret.setWasei(wasei);
		ArrayList<String> xref = getXref(allP, meaning);
		ret.setXref(xref);
		ArrayList<String> antonym = getAntonym(allP, meaning);
		ret.setAntonym(antonym);

		return ret;
	}

	static public ArrayList<EdictDefinition> getDefinitions(String entry) {
		ArrayList<EdictDefinition> ret = new ArrayList<EdictDefinition>();
		ArrayList<String> defs = splitDefs(entry);
		for (int i = 0; i < defs.size(); i++) {
			EdictDefinition def = createDefinition(i + 1, defs.get(i));
			String[] meanings = splitSlash(defs.get(i));
			for (int j = 0; j < meanings.length; j++) {
				EdictMeaning meaning = createMeaning(j + 1, meanings[j]);
				def.meanings.add(meaning);
			}
			ret.add(def);
		}
		return ret;
	}

	static public String getEdictId(String entry) {
		String ret = null;
		String [] tokens = splitSlash(entry);
		if (tokens.length > 1) {
			ret = tokens[tokens.length - 1];
		}
		return ret;
	}
}
