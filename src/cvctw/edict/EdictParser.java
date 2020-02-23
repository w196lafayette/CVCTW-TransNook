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

	static private final String WASEI = "wasei";
	static private final String ANTONYM = "ant";

	// In the eDict the initial terms are always kanji.
	// Alternate terms are other alphabets
	static public ArrayList<EdictTerm> getTerm(String entry) {
		ArrayList<EdictTerm> ret = new ArrayList<EdictTerm>();
		String[] tokens = entry.split(" ");
		if (tokens.length > 1) {
			// if ";" not found, returns an array of one entry
			String[] terms = tokens[0].split(";");
			for (int i = 0; i < terms.length; i++) {
				ret.add(new EdictTerm(terms[i], Alphabet.katakana));
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
			// TO DO: detect the alphabet
			ret.add(new EdictTerm(t.replace("]", ""), Alphabet.katakana));
		}
		return ret;
	}

	/**
	 * Parts of speech may be multi-value, delimited by commas
	 * @param allP
	 * @param entry
	 * @return
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
			} else if (PartOfSpeech.isPartOfSpeech(p) == true) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	static public ArrayList<String> getContext(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (Context.isContext(p) == true) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	static public ArrayList<String> getSense(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (Sense.isSense(p) == true) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				ret.add(p);
			}
		}
		return ret;
	}

	static public ArrayList<String> getDialect(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (p.contains(":")) {
				String[] tD = p.split(":");
				if (tD != null && tD.length > 0) {
					p = tD[0];
				}
			}
			if (Dialect.isDialect(p) == true) {
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
			if (p.contains(":")) {
				String lang = null;
				String[] tD = p.split(":");
				if (tD != null && tD.length > 0) {
					lang = tD[0];
				}
				if (lang != null && Language.isLanguage(lang) == true) {
					if (ret == null) {
						ret = new ArrayList<String>();
					}
					if (tD.length > 1) {
						String saveP = p;
						ret.add(saveP);
					} else {
						ret.add(lang);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Wasei terms may have multiple values, delimited by a comma
	 * @param allP
	 * @param entry
	 * @return
	 */
	static public ArrayList<String> getWasei(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (p.startsWith(WASEI + ":")) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				if (p.contains(",")) {
					String[] tW = p.split(",");
					for (int i = 0; i < tW.length; i++) {
						ret.add(tW[i].replace(WASEI + ":", "").trim());
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
			if (Xref.isXref(p) == true) {
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
	 * @return
	 */
	static public ArrayList<String> getAntonym(ArrayList<String> allP, String entry) {
		ArrayList<String> ret = null;
		for (String p : allP) {
			if (p.startsWith(ANTONYM + ": ")) {
				if (ret == null) {
					ret = new ArrayList<String>();
				}
				p = p.replace(ANTONYM + ": ", "");
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
//			System.out.println("encl=" + encl);
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
	 * @return
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
//				System.out.println("last entry=" + entry + "\n prev=" + prev + ", next=" + next + 
//						", beginIdx=" + markersIdx.get(markerIdx) + ", beginIdx[+1]=" + slashIdx.get(slashIdx.size() - 2));
			} else {
				// tricky: find the slash before the next "marker"
				int nextM = markersIdx.get(markerIdx + 1);
				next = previousSlash(entry, nextM + 1);
//				System.out.println("entry=" + entry + "\n prev=" + prev + ", next=" + next + ", beginIdx=" + markersIdx.get(markerIdx) + ", beginIdx[+1]=" + markersIdx.get(markerIdx+1));
			}
			String def = entry.substring(prev + 1, next);
			ret.add(def);
		}
		return ret;
	}

	static private EdictDefinition createDefinition(int order, String def) {
		EdictDefinition ret = new EdictDefinition(order, def);
		ret.defOrder = order;
//		ArrayList<String> allP = getEnclosed(def, "(", ")");
//		ArrayList<String> context = getContext(allP, def);
//		ret.setContext(context);
//		ArrayList<String> partOfSpeech = getPartOfSpeech(allP, def);
//		ret.setPartOfSpeech(partOfSpeech);
//		ArrayList<String> sense = getSense(allP, def);
//		ret.setSense(sense);
//		ArrayList<String> dialect = getDialect(allP, def);
//		ret.setDialect(dialect);
//		ArrayList<String> altLanguage = getAltLanguage(allP, def);
//		ret.setAltLanguage(altLanguage);
//		ArrayList<String> wasei = getWasei(allP, def);
//		ret.setWasei(wasei);
//		ArrayList<String> xref = getXref(allP, def);
//		ret.setXref(xref);
//		ArrayList<String> antonym = getAntonym(allP, def);
//		ret.setAntonym(antonym);
//
		return ret;
	}

	static private EdictMeaning createMeaning(int order, String meaning) {
		EdictMeaning ret = new EdictMeaning(order, meaning);

		ArrayList<String> allP = getEnclosed(meaning, "(", ")");
		ArrayList<String> context = getContext(allP, meaning);
		ret.setContext(context);
		ArrayList<String> partOfSpeech = getPartOfSpeech(allP, meaning);
		ret.setPartOfSpeech(partOfSpeech);
		ArrayList<String> sense = getSense(allP, meaning);
		ret.setSense(sense);
		ArrayList<String> dialect = getDialect(allP, meaning);
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

//	static public ArrayList<EdictMeaning> getMeanings(String def) {
//		ArrayList<EdictMeaning> ret = new ArrayList<EdictMeaning>();
//		ArrayList<String> meanings = splitMeanings(def);
//		for (int i = 0; i < meanings.size(); i++) {
//			ret.add(createMeaning(i + 1, meanings.get(i)));
//		}
//		return ret;
//	}

	static public String getEdictId(String entry) {
		String ret = null;
		String [] tokens = splitSlash(entry);
		if (tokens.length > 1) {
			ret = tokens[tokens.length - 1];
		}
		return ret;
	}
}
