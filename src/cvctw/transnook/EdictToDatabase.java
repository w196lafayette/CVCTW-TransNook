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
import cvctw.edict.Dictionary;
import cvctw.edict.EdictDefinition;
import cvctw.edict.EdictEntry;
import cvctw.edict.EdictMeaning;
import cvctw.edict.EdictReader;
import cvctw.edict.EdictTerm;

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
			putTerm(t, entryId);
		}
		// Insert child Definition records
		for (EdictDefinition d : dE.definitions) {
			Integer defId = putDefinition(d, entryId);
			// Insert child Meaning records
			for (EdictMeaning m : d.meanings) {
				Integer meaningId = putMeaning(m, entryId, defId);
				putMeaningAttributes(m.contexts, meaningId, 
						TnProp.TABLE_CONTEXTS, TnProp.COLUMN_CONTEXTS, TnProp.TABLE_CONTEXTS2MEANINGS);
				putMeaningAttributes(m.partsOfSpeech, meaningId, 
						TnProp.TABLE_PARTSOFSPEECH, TnProp.COLUMN_PARTSOFSPEECH, TnProp.TABLE_PARTSOFSPEECH2MEANINGS);
				putMeaningAttributes(m.senses, meaningId, 
						TnProp.TABLE_SENSES, TnProp.COLUMN_SENSES, TnProp.TABLE_SENSES2MEANINGS);
				putMeaningAttributes(m.dialects, meaningId, 
						TnProp.TABLE_DIALECTS, TnProp.COLUMN_DIALECTS, TnProp.TABLE_DIALECTS2MEANINGS);
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
	private void putTerm(EdictTerm t, Integer entryId) throws SQLException, Exception {
		// Escape any apostrophes in the term (unlikely, but possible
		String newTerm = t.term.replaceAll("'", "''");
		t.term = newTerm;
		// If the alphabet is missing in its static table, insert it
		staticTable.insertIfMissing(TnProp.TABLE_ALPHABETS, t.alphabet.toString());
		// Get the parent Entry's id
		t.entryId = entryId;
		// Insert Term
		rowWriter.writeTerm(t);
	}

	/**
	 * 
	 * @param d
	 * @param entryId
	 * @return
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
	 * @return
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

	/**
	 * 
	 * @param attributesArray
	 * @param meaningId
	 * @param table
	 * @param column
	 * @param att2defTable
	 * @throws SQLException
	 * @throws Exception
	 */
	private void putMeaningAttributes(ArrayList<String> attributesArray, Integer meaningId,
			String table, String column, String att2defTable) throws SQLException, Exception {
		// If the meaning has none of these Attributes, no problem.  Just return happy.
		if (attributesArray == null) {
			return;
		}
		// Insert the Definition's child Attribute records
		for (String attribute : attributesArray) {
			// If the Attribute is missing in its static table, insert it
			staticTable.insertIfMissing(table, attribute);
			// Link this Attribute to this Definition
			rowWriter.writeAttrToMeaning(att2defTable, column, attribute, meaningId);
		}
	}

	/**
	 * Split entry into one or, optionally, two parts
	 * @param text
	 * @param delimiter
	 * @return
	 */
	private ArrayList<String> splitAndTrim(String text, String delimiter) {
		ArrayList<String> ret = new ArrayList<String>();
		// If there could be more than two parts, force all into only two
		String [] tS = text.split(delimiter, 2);
		ret.add(tS[0]);
		if (tS.length > 1) {
			String tS1 = tS[1].trim();
			ret.add(tS1);
		}
		return ret;
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
			String columnList = null;
			String valuesList = null;
			ArrayList<String> altParts = splitAndTrim(alt, ":");
			String lang = altParts.get(0);
			String term = "";
			if (altParts.size() > 1) {
				term = altParts.get(1).replaceAll("'", "''");
			}
			columnList = "language,meaningId,term";
			valuesList = "'" + lang + "'," + meaningId + ",'" + term + "'";
			// Add language to Languages table, if missing
			staticTable.insertIfMissing(TnProp.TABLE_LANGUAGES, lang);
			rowWriter.writeRow(TnProp.TABLE_ALTLANGUAGES2MEANINGS, columnList, valuesList, false);
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
			ArrayList<String> waseiParts = splitAndTrim(w, ":");
			String term = "";
			if (waseiParts.size() > 1) {
				term = waseiParts.get(1).replaceAll("'", "''");
			}
			columnList = "wasei,meaningId";
			valuesList = "'" + term + "'," + meaningId;
			rowWriter.writeRow(TnProp.TABLE_WASEI2MEANINGS, columnList, valuesList, false);
		}
	}
 
	private void putMeaningXrefs(ArrayList<String> xrefArray, Integer meaningId) throws SQLException, Exception {
		// If the meaning has none of these Attributes, no problem.  Just return happy.
		if (xrefArray == null) {
			return;
		}
		for (String x : xrefArray) {
			String columnList = null;
			String valuesList = null;
			ArrayList<String> xrefParts = null;
			if (x.startsWith("cf. ")) {
				xrefParts = splitAndTrim(x, "\\.");
			} else if (x.startsWith("See ")){
				xrefParts = splitAndTrim(x, " ");
			} else {
				System.err.println("ERROR in parsing.  This Xref is neither 'cf. ' nor 'See '.");
				break;
			}
			String xref = xrefParts.get(0);
			String xrefText = "";
			if (xrefParts.size() > 1) {
				xrefText = xrefParts.get(1).replaceAll("'", "''");
			}
			columnList = "xref,xrefText,meaningId";
			valuesList = "'" + xref + "','" + xrefText + "'," + meaningId;
			// Add xref to Xrefs table, if missing
			staticTable.insertIfMissing(TnProp.TABLE_XREFS, xref);
			rowWriter.writeRow(TnProp.TABLE_XREFS2MEANINGS, columnList, valuesList, false);
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
			String columnList = "antonym,meaningId";
			String valuesList = "'" + antonym + "'," + meaningId;
			rowWriter.writeRow(TnProp.TABLE_ANTONYMS2MEANINGS, columnList, valuesList, false);
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
