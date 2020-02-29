/**
 * 
 */
package cvctw.db.transnook;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cvctw.edict.Alphabet;
import cvctw.edict.Alphabet.AlphabetE;
import cvctw.edict.EdictDefinition;
import cvctw.edict.EdictEntry;
import cvctw.edict.EdictMeaning;
import cvctw.edict.EdictTerm;

/**
 * In progress, proposed class to provide methods to perform patron-requested searches
 * of the CVCTW Transnook database 
 * @author minge
 *
 */
public class TnSearches {

	public enum ReturnValues {
		ENTRY_ID,
		TERM_ID,
		DEFINITION_ID,
		MEANING_ID
	}

	public enum MatchType {
		EQUALS,
		STARTS_WITH,
		ENDS_WITH,
		CONTAINS
	}

	private static TnConnection tnConn = null;

	TnSearches() throws FileNotFoundException, IOException {
		if (tnConn == null) {
			tnConn = TnConnection.getInstance();
		}
	}

	/**
	 * 
	 * @param term Term to match
	 * @param retVal What you want returned from the search
	 * @param match How you want to match (equal, starts-with, ends-with, contains)
	 * @return Array of IDs
	 * @throws SQLException
	 */
	public ArrayList<Integer> tnMatchTerm(String term, ReturnValues retVal, MatchType match) throws SQLException, Exception {
		ArrayList<Integer> ret = null;
		ArrayList<Integer> termIds = new ArrayList<Integer>();
		String newTerm = term.replaceAll("'", "''");
		String key = "id";
		String table = TnProp.SCHEMA + "." + TnProp.TABLE_TERMS;
		String column = "term";
		String condition = "";
		switch(match) {
		case EQUALS:
			condition = " = '" + newTerm + "'";
			break;
		case STARTS_WITH:
			condition = " LIKE '" + newTerm + "%'";
			break;
		case ENDS_WITH:
			condition = " LIKE '%" + newTerm + "'";
			break;
		case CONTAINS:
			condition = " LIKE '%" + newTerm + "%'";
			break;
		default:
			throw new Exception("Unrecognized MatchType: " + match);
		}

		// Term.id is the preliminary search result.
		// If that's what the caller wants, we're done.
		// Otherwise we need to find these Term.id's in other tables.
		String query = TnProp.SQL_SELECT + key + TnProp.SQL_FROM + table + TnProp.SQL_WHERE + column + condition;
		ResultSet rs = tnConn.tnExecuteQuery(query);
		while (rs.next()) {
			termIds.add(rs.getInt(1));
		}
		tnConn.tnCloseResultSet();
		switch (retVal) {
		case ENTRY_ID:
			break;
		case TERM_ID:
			// We're done.
			ret = termIds;
			break;
		case DEFINITION_ID:
			break;
		case MEANING_ID:
			break;
		default:
			throw new Exception("Unrecognized ReturnValues: " + retVal);
		}
		return ret;
	}

	private void tnSearchSimple(String query, boolean isString) throws SQLException {
		ResultSet rs = tnConn.tnExecuteQuery(query);
		if (isString == true) {
			while (rs.next()) {
				String result = rs.getString(1);
				System.out.println(result);
			}
		} else {
			while (rs.next()) {
				Integer result = rs.getInt(1);
				System.out.println(result);
			}
		}
		tnConn.tnCloseResultSet();
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, 
			SQLException, Exception {
		if (args.length < 1) {
			System.err.println("Missing argument: propertiesFilePath");
			System.exit(-1);
		}
		System.out.println("TnSearches starting: " + args[0]);
		TnConnection tnConn = TnConnection.getInstance(args[0]);
		tnConn.tnConnDataSource();
		AlphabetE aa = Alphabet.AlphabetE.valueOf("latin");
		System.out.println("AlphabetE.valueOf(none)" + aa);
		TnSearches tS = new TnSearches();
		String query = "SELECT id FROM transnook.terms WHERE term LIKE '%ok%'";
		tS.tnSearchSimple(query, true);
		String query1 = "select term from transnook.terms";
		tS.tnSearchSimple(query1, true);
		TnRowReader r = new TnRowReader();
		for (int i = 150; i < 200; i++) {
			Integer eId = i;
			EdictEntry e = r.readEntryFull(eId);
			if (e != null) {
				System.out.println("entry " + eId + ": " + e.entryOnly());
				for (EdictTerm t : e.terms) {
					System.out.println("term " + t.id + ": " + t.term);
				}
				for (EdictDefinition d : e.definitions) {
					System.out.println("def " + d.defOrder + ": " + d.definitionOnly());
					for (EdictMeaning m : d.meanings) {
						System.out.println("meaning " + m.meaningOrder + ": " + m.meaning);
					}
				}
				System.out.println();
			}
		}
//		String searchTerm = "鬣ｺ";
//		ArrayList<Integer> ret = tS.tnMatchTerm(searchTerm, ReturnValues.TERM_ID, MatchType.CONTAINS);
////	public ArrayList<Integer> tnMatchTerm(String term, ReturnValues retVal, MatchType match) throws SQLException, Exception {
//		for (Integer i : ret) {
//			System.out.println("i=" + i);
//		}
		System.out.println("done");
	}

}
