/**
 * 
 */
package cvctw.edict;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author minge
 *
 */
public class Dictionary {

	ArrayList<EdictEntry> entries;
	ArrayList<String> contents;
	
	@SuppressWarnings("unused")
	private Dictionary() {
		// disable this contructor
	}

	public Dictionary(ArrayList<String> contents) {
		this.contents = contents;
		this.entries = new ArrayList<EdictEntry>();
		for (String line : this.contents) {
			EdictEntry e = new EdictEntry(line);
			entries.add(e);
		}
	}

	public ArrayList<EdictEntry> getEntries() {
		return entries;
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Missing argument: filePath");
			System.exit(-1);
		}
		EdictReader r = new EdictReader(args[0]);
		ArrayList<String> c = r.getContents();
		System.out.println("EdictReader contents.size=" + c.size());
		Dictionary d = new Dictionary(c);
		ArrayList<EdictEntry> dE = d.getEntries();
		System.out.println("Dictionary entries.size=" + dE.size());
		for (EdictEntry e : dE) {
			System.out.println("\nENTRY: " + e.entryOnly());
			for (EdictTerm t : e.terms) {
				System.out.println("TERM: " + t.toString());
			}
			for (EdictDefinition def : e.definitions) {
				System.out.println("DEF: " + def.definitionOnly());
				for (EdictMeaning m : def.meanings) {
					System.out.println("MEANING: " + m.meaningOnly());
					if (m.contexts != null) {
						for (String a : m.contexts) {
							System.out.println("CONTEXT: " + a);
						}
					}
					if (m.partsOfSpeech != null) {
						for (String a : m.partsOfSpeech) {
							System.out.println("PART-OF-SPEECH: " + a);
						}
					}
					if (m.senses != null) {
						for (String a : m.senses) {
							System.out.println("SENSE: " + a);
						}
					}
					if (m.dialects != null) {
						for (String a : m.dialects) {
							System.out.println("DIALECT: " + a);
						}
					}
					if (m.altLanguages != null) {
						for (String a : m.altLanguages) {
							System.out.println("ALT-LANGUAGE: " + a);
						}
					}
					if (m.waseis != null) {
						for (String a : m.waseis) {
							System.out.println("WASEI: " + a);
						}
					}
					if (m.xrefs != null) {
						for (String a : m.xrefs) {
							System.out.println("XREF: " + a);
						}
					}
					if (m.antonyms != null) {
						for (String a : m.antonyms) {
							System.out.println("ANTONYM: " + a);
						}
					}
				}
			}
		}
	}
}
