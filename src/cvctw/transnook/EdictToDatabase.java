/**
 * 
 */
package cvctw.transnook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import cvctw.db.transnook.TnConnection;
import cvctw.db.transnook.TnProp;
import cvctw.db.transnook.TnRowReader;
import cvctw.db.transnook.TnRowWriter;
import cvctw.db.transnook.TnStaticTable;
import cvctw.edict.Antonym;
import cvctw.edict.Context;
import cvctw.edict.Dialect;
import cvctw.edict.Dictionary;
import cvctw.edict.EdictDefinition;
import cvctw.edict.EdictEntry;
import cvctw.edict.EdictEnum;
import cvctw.edict.EdictMeaning;
import cvctw.edict.EdictReader;
import cvctw.edict.EdictTerm;
import cvctw.edict.KanjiInfo;
import cvctw.edict.Language;
import cvctw.edict.PartOfSpeech;
import cvctw.edict.ReadingInfo;
import cvctw.edict.Sense;
import cvctw.edict.Wasei;
import cvctw.edict.Xref;

/**
 * @author minge
 *
 */
public class EdictToDatabase {

	private ArrayList<String> eDictContents = null;
	private ArrayList<EdictEntry> dictEntries = null;
	private TnRowReader rowReader = null;
	private TnRowWriter rowWriter = null;
	private TnStaticTable staticTable = null;
	private TnConnection tnConnection = null;
	private Integer entryFailures = 0;
	private Integer maxEntryFailures = 0;
	private static Context eContext = new Context();
	private static ReadingInfo eReadingInfo = new ReadingInfo();
	private static KanjiInfo eKanjiInfo = new KanjiInfo();
	private static Dialect eDialect = new Dialect();
	private static Sense eSense = new Sense();
	private static PartOfSpeech ePartOfSpeech = new PartOfSpeech();
	private static Language eLanguage = new Language();
	private static Wasei eWasei = new Wasei();
	private static Xref eXref = new Xref();
	private static Antonym eAntonym = new Antonym();


	EdictToDatabase(String eDictPath, String propPath) throws IOException, FileNotFoundException, SQLException {
		EdictReader r = new EdictReader(eDictPath);
		eDictContents = r.getContents();
		System.out.println("EdictReader contents.size=" + eDictContents.size());
		Dictionary d = new Dictionary(eDictContents);
		dictEntries = d.getEntries();
		System.out.println("Dictionary entries.size=" + dictEntries.size());
		makeTools(propPath);
}

	private void makeTools(String propPath) throws SQLException, FileNotFoundException, IOException {
		tnConnection = TnConnection.getInstance(propPath);
//		tnConnection.tnConnDriverMgr();
		tnConnection.tnConnDataSource();
		rowReader = new TnRowReader();
		rowWriter = new TnRowWriter();
		staticTable = new TnStaticTable(rowReader, rowWriter);
		maxEntryFailures = TnProp.getInstance().getDbMaxEntryFailures();
	}

	/**
	 * Handles a Transaction for a single Entry and its many, many "child" records
	 * 
	 * @throws SQLException
	 * @throws Exception
	 */
	public void dictToDb() throws SQLException, Exception {
		for (EdictEntry dE : dictEntries) {
			try {
				tnConnection.tnExecuteUpdate(TnConnection.START_TRANSACTION);
				putEntry(dE);
			} catch (Exception e) {
				e.printStackTrace();
				tnConnection.tnExecuteUpdate(TnConnection.ROLLBACK);
				entryFailures++;
				if (entryFailures > maxEntryFailures) {
					System.err.println(entryFailures + " exceeds the max " + maxEntryFailures + ".  Exiting");
					throw e;
				}
			}
			tnConnection.tnExecuteUpdate(TnConnection.COMMIT);
		}
	}

	/**
	 * Writes the hierarchy of records that make up an Entry
	 * 
	 * @param dE
	 * @throws SQLException
	 * @throws Exception
	 */
	private void putEntry(EdictEntry dE) throws SQLException, Exception {
		// Escape any apostrophes in the entry
		String newEntry = dE.entry.replaceAll("'", "''");
		dE.entry = newEntry;
		// Convert enum "source" to char
		char source = dE.source.toString().charAt(0);
		// If the language is missing in its static table, insert it
		staticTable.insertIfMissing(TnProp.TABLE_LANGUAGES, dE.language);
		// Insert parent Entry record, capture its new ID
		Integer entryId = rowWriter.writeEntry(dE, source);
		// Insert child Term records
		for (EdictTerm t : dE.terms) {
			Integer termId = putTerm(t, entryId);
			putAttributes(t.readingInfo, eReadingInfo, termId);
			putAttributes(t.kanjiInfo, eKanjiInfo, termId);
		}
		// Insert child Definition records
		for (EdictDefinition d : dE.definitions) {
			Integer defId = putDefinition(d, entryId);
			// Insert child Meaning records
			for (EdictMeaning m : d.meanings) {
				Integer meaningId = putMeaning(m, entryId, defId);

				putAttributes(m.contexts, eContext, meaningId);
				putAttributes(m.partsOfSpeech, ePartOfSpeech, meaningId);
				putAttributes(m.senses, eSense, meaningId);
				putAttributes(m.dialects, eDialect, meaningId);
				putMeaningAltLanguages(m.altLanguages, meaningId);
				putMeaningWaseis(m.waseis, meaningId);
				putMeaningXrefs(m.xrefs, meaningId);
				putMeaningAntonyms(m.antonyms, meaningId);
			}
		}
	}

	/**
	 * 
	 * @param t
	 * @param entryId
	 * @throws SQLException
	 * @throws Exception
	 */
	private Integer putTerm(EdictTerm t, Integer entryId) throws SQLException, Exception {
		// Escape any apostrophes in the term (unlikely, but possible
		String newTerm = t.term.replaceAll("'", "''");
		t.term = newTerm;
		// If the alphabet is missing in its static table, insert it
		staticTable.insertIfMissing(TnProp.TABLE_ALPHABETS, t.alphabet.toString());
		// Get the parent Entry's id
		t.entryId = entryId;
		// Insert Term
		Integer id = rowWriter.writeTerm(t);
		return id;
	}

	/**
	 * 
	 * @param d
	 * @param entryId
	 * @return the auto-generated key of the new record
	 * @throws SQLException
	 * @throws Exception
	 */
	private Integer putDefinition(EdictDefinition d, Integer entryId) throws SQLException, Exception {
		// Escape any apostrophes in the meaning
		String newDef = d.definition.replaceAll("'", "''");
		d.definition = newDef;
		// If the Definition's language is missing in its static table, insert it
		staticTable.insertIfMissing(TnProp.TABLE_LANGUAGES, d.language);
		// Get the parent Entry's id
		d.entryId = entryId;
		// Insert Definition and save its new id
		Integer defId = rowWriter.writeDefinition(d);
		return defId;
	}

	/**
	 * 
	 * @param m
	 * @param defId
	 * @return the auto-generated key of the new record
	 * @throws SQLException
	 * @throws Exception
	 */
	private Integer putMeaning(EdictMeaning m, Integer entryId, Integer defId) throws SQLException, Exception {
		// Escape any apostrophes in the meaning
		String newMeaning = m.meaning.replaceAll("'", "''");
		m.meaning = newMeaning;
		// Get the grand-parent Entry's id
		m.entryId = entryId;
		// Get the parent Definition's id
		m.defId = defId;
		// Insert Meaning and save its new id
		Integer meaningId = rowWriter.writeMeaning(m);
		return meaningId;
	}

	private void putAttributeTag(EdictEnum eEnum, String tag) throws SQLException {
		String table = eEnum.getTable();
		String column = eEnum.getColumn();
		if (! eEnum.isWritten(tag)) {
			boolean present = rowReader.isRowPresent(table, column, tag, true);
			if (! present) {
				rowWriter.writeRow(table, column, tag, true);
				eEnum.written(tag);
			}
		}
	}
	private void putAttributes(ArrayList<String> attrArray, EdictEnum eEnum, Integer parentId) throws SQLException {
		// If the attribute array is null, just return happy
		if (attrArray == null) {
			return;
		}
		String column = eEnum.getColumn();
		String toTable = eEnum.getToTable();
		String toColumn = eEnum.getToColumn();
		for (String tag : attrArray) {
			tag = eEnum.getTag(tag);
			putAttributeTag(eEnum, tag);
			String columnList = column + "," + toColumn;
			String valuesList = "'" + tag + "'," + parentId;
			rowWriter.writeRow(toTable, columnList, valuesList, false);
		}
	}

	/**
	 * Parse the value:  language plus optional text
	 * @param altLangArray
	 * @param meaningId
	 * @throws Exception 
	 * @throws SQLException 
	 */
	private void putMeaningAltLanguages(ArrayList<String> altLangArray, Integer meaningId) throws SQLException, Exception {
		// If the meaning has none of these Attributes, no problem.  Just return happy.
		if (altLangArray == null) {
			return;
		}
		for (String alt : altLangArray) {
			String lang = eLanguage.getTag(alt);
			String term = eLanguage.getText(alt);
			if (term.length() > 1) {
				term = term.replaceAll("'", "''");
			}
			String columnList = "language,meaningId,term";
			String valuesList = "'" + lang + "'," + meaningId + ",'" + term + "'";
			// Add language to Languages table, if missing
			ArrayList<String> tA = new ArrayList<String>();
			tA.add(alt);
			putAttributeTag(eLanguage, lang);
			rowWriter.writeRow(eLanguage.getToTable(), columnList, valuesList, false);
		}
	}

	private void putMeaningWaseis(ArrayList<String> waseiArray, Integer meaningId) throws SQLException, Exception {
		// If the meaning has none of these Attributes, no problem.  Just return happy.
		if (waseiArray == null) {
			return;
		}
		for (String w : waseiArray) {
			String columnList = null;
			String valuesList = null;
			String term = eWasei.getText(w);
			if (term.length() > 1) {
				term = term.replaceAll("'", "''");
			}
			columnList = eWasei.getColumn() + "," + eWasei.getToColumn(); // "wasei,meaningId";
			valuesList = "'" + term + "'," + meaningId;
			rowWriter.writeRow(eWasei.getToTable(), columnList, valuesList, false);
		}
	}
 
	private void putMeaningXrefs(ArrayList<String> xrefArray, Integer meaningId) throws SQLException, Exception {
		// If the meaning has none of these Attributes, no problem.  Just return happy.
		if (xrefArray == null) {
			return;
		}
		for (String x : xrefArray) {
			String xref = eXref.getTag(x); // xrefParts.get(0);
			String xrefText = eXref.getText(x); // "";
			if (xrefText.length() > 1) {
				xrefText = xrefText.replaceAll("'", "''");
			}
			String columnList = eXref.getColumn() + ",xrefText," + eXref.getToColumn(); // "xref,xrefText,meaningId";
			String valuesList = "'" + xref + "','" + xrefText + "'," + meaningId;
			// Add xref to Xrefs table, if missing
			putAttributeTag(eXref, xref);
			rowWriter.writeRow(eXref.getToTable(), columnList, valuesList, false);
		}
	}
 
	/**
	 * The antonyms have already had the "ant: " prefix removed in the EdictParser method.
	 * The multiple terms (delimited by comma) have also been added to this ArrayList.
	 * No parsing or conversion is necessary here.
	 * 
	 * @param antonymArray
	 * @param meaningId
	 * @throws SQLException
	 * @throws Exception
	 */
	private void putMeaningAntonyms(ArrayList<String> antonymArray, Integer meaningId) throws SQLException, Exception {
		// If the meaning has none of these Attributes, no problem.  Just return happy.
		if (antonymArray == null) {
			return;
		}
		for (String ant : antonymArray) {
			String antonym = ant.replaceAll("'", "''");
			String columnList = eAntonym.getColumn() + "," + eAntonym.getToColumn(); //  "antonym,meaningId";
			String valuesList = "'" + antonym + "'," + meaningId;
			rowWriter.writeRow(eAntonym.getToTable(), columnList, valuesList, false);
		}
	}
 
	/**
	 * Principal gateway into the features of the TransNook project.
	 * Requires two parameters:
	 * 1) path of the eDict file to read
	 * 2) path of the Properties file to read
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException, Exception {
		if (args.length < 2) {
			System.out.println("Missing argument: eDictFilePath propertiesFilePath");
			System.exit(-1);
		}
		EdictToDatabase eD = new EdictToDatabase(args[0], args[1]);
		eD.dictToDb();
	}

}
