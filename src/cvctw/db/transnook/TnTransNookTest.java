/**
 * 
 */
package cvctw.db.transnook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

import cvctw.edict.Alphabet;
import cvctw.edict.EdictDefinition;
import cvctw.edict.EdictMeaning;
import cvctw.edict.EdictEntry;
import cvctw.edict.EdictTerm;

/**
 * @author minge
 *
 */
public class TnTransNookTest {
	private Connection conn = null;
	public TnProp prop = null;
	private TnRowReader rowReader = null;
	private TnRowWriter rowWriter = null;

	public TnTransNookTest() throws FileNotFoundException, IOException, SQLException {
		prop = TnProp.getInstance(); //TnProp.readProp("tnProperties.prop");
		makeTools();
	}

	public TnTransNookTest(String path) throws FileNotFoundException, IOException, SQLException {
		prop = TnProp.getInstance(path);
		makeTools();
	}

	private void makeTools() throws SQLException, FileNotFoundException, IOException {
		TnConnection tnConn = TnConnection.getInstance();
//		conn = tnConn.tnConnDriverMgr();
		conn = tnConn.tnConnDataSource();
		rowReader = new TnRowReader();
		rowWriter = new TnRowWriter();
	}
	public void tnSelTerm() throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from transnook.terms");
		EdictTerm t = new EdictTerm();
		while(rs.next()) {
			t = rowReader.rs2Term(rs);
			System.out.println(t);
		}
	}

	public void tnSelEntry() throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from transnook.entries");
		EdictEntry e = new EdictEntry();
		while(rs.next()) {
			e = rowReader.rs2Entry(rs);
			System.out.println(e.entryOnly());
		}
	}

	public void tnSelDefinition() throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from transnook.definitions");
		EdictDefinition d = new EdictDefinition();
		while(rs.next()) {
			d = rowReader.rs2Definition(rs);
			System.out.println(d.definitionOnly());
		}
	
	}

	public void tnReadEntries() throws SQLException {
		ArrayList<EdictEntry> aT = new ArrayList<EdictEntry>();
		aT = rowReader.readEntries();
		for (EdictEntry e : aT) {
			System.out.println(e.entryOnly());
		}
	}

	public void tnReadTerms() throws SQLException {
		ArrayList<EdictTerm> aT = new ArrayList<EdictTerm>();
		aT = rowReader.readTerms();
		for (EdictTerm t : aT) {
			System.out.println(t);
		}
	}

	public void tnReadDefinitions() throws SQLException {
		ArrayList<EdictDefinition> aT = new ArrayList<EdictDefinition>();
		aT = rowReader.readDefinitions();
		for (EdictDefinition d : aT) {
			System.out.println(d.definitionOnly());
		}
	}

	public void tnReadMaxIds() throws SQLException  {
		Integer eCt = rowReader.readMaxId(TnProp.TABLE_ENTRIES);
		Integer tCt = rowReader.readMaxId(TnProp.TABLE_TERMS);
		Integer pCt = rowReader.readMaxId(TnProp.TABLE_DEFINITIONS);
		System.out.println("TABLE COUNTS entries=" + eCt + ", terms=" + tCt + ", definitions=" + pCt);
	}

	public void tnWriteEntry() throws SQLException {
		EdictEntry e = new EdictEntry();
		e.id = 0;
		long tim = LocalTime.now().toNanoOfDay();
		Random rand = new Random(tim);
		Long nLong = rand.nextLong();
		if (nLong < 0) {
			nLong = -nLong;
		}
		e.entry = "new entry " + nLong;
		e.language = "jap";
		Integer nInt = rand.nextInt() % 100001;
		if (nInt < 0) {
			nInt = -nInt;
		}
		e.eDictId = "EntL" + nInt;
		Integer entryId = rowWriter.writeEntry(e, 'E');
		e.id = entryId;
		System.out.println("new entry: " + e.entryOnly());
	}
	public void tnWriteTerm() throws SQLException {
		EdictTerm t = new EdictTerm();
		t.entryId = rowReader.readMaxId(TnProp.TABLE_ENTRIES);
		t.id = rowReader.readMaxIdForEntry(TnProp.TABLE_TERMS, t.entryId) + 1;
		long tim = LocalTime.now().toNanoOfDay();
		Random rand = new Random(tim);
		Long nLong = rand.nextLong();
		if (nLong < 0) {
			nLong = -nLong;
		}
		t.term = "new term " + t.id + " for entry " + t.entryId + " term=" + nLong;
		t.alphabet = Alphabet.katakana;
		Integer newId = rowWriter.writeTerm(t);
		t.id = newId;
		System.out.println("new term: " + t);
	}
	public void tnWriteDefinition() throws SQLException {
		EdictDefinition d = new EdictDefinition();
		d.entryId = rowReader.readMaxId(TnProp.TABLE_ENTRIES);
		d.defOrder = 4;
		d.language = "eng";
		d.definition = "just nothing";
		Integer newId = rowWriter.writeDefinition(d);
		d.id = newId;
		System.out.println("new definition: " + d.definitionOnly());
//		ArrayList<String> dC = new ArrayList<String>();
//		dC.add("biol");
//		dC.add("econ");
//		for (String c : dC) {
//			Integer cId = rowWriter.writeAttrToMeaning(TnProp.TABLE_CONTEXTS2MEANINGS, TnProp.COLUMN_CONTEXTS,
//					c, newId);
//			System.out.println("new contexts 2 meaning: id=" + cId + ", contexts=" + c + ", defId=" + newId);
//		}
	}
	public void tnWriteMeaning() throws SQLException {
		EdictMeaning m = new EdictMeaning();
		m.defId = rowReader.readMaxId(TnProp.TABLE_DEFINITIONS);
		m.meaningOrder = 2;
		m.meaning = "meaning just nothing";
		Integer newId = rowWriter.writeMeaning(m);
		m.id = newId;
		System.out.println("new meaning: " + m.meaningOnly());
		ArrayList<String> dC = new ArrayList<String>();
		dC.add("biol");
		dC.add("econ");
		for (String c : dC) {
			Integer cId = rowWriter.writeAttrToMeaning(TnProp.TABLE_CONTEXTS2MEANINGS, TnProp.COLUMN_CONTEXTS,
					c, newId);
			System.out.println("new contexts 2 meaning: id=" + cId + ", contexts=" + c + ", meaningId=" + newId);
		}
	}
	public void qJap() throws SQLException {
		boolean pS = rowReader.isRowPresent(TnProp.TABLE_LANGUAGES, "language", "jap", true);
		System.out.println("does table languages have a row with language=jap? " + pS);
		boolean pI = rowReader.isRowPresent(TnProp.TABLE_ENTRIES, "id", "1", false);
		System.out.println("does table entries have a row with id=1? " + pI);
	}
	public void staticTable() throws SQLException, Exception {
		TnStaticTable sT = new TnStaticTable(rowReader, rowWriter);
		sT.insertIfMissing("languages", "eng");
		sT.insertIfMissing("languages", "jap");
		sT.insertIfMissing("alphabets", "katakana");
		sT.insertIfMissing("contexts", "econ");
		sT.insertIfMissing("contexts", "biol");
	}
	/**
	 * @param args
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) 
			throws InstantiationException, IllegalAccessException, 
			SQLException, ClassNotFoundException, FileNotFoundException, IOException {

		TnTransNookTest tt = new TnTransNookTest();
		TnConnection tConn = null;
		try {
			tConn = TnConnection.getInstance();
			String query = "select * from transnook.users";
			ResultSet rs = tConn.tnExecuteQuery(query);
			while(rs.next()) {
				String fName = rs.getString("firstName");
				System.out.println("firstName=" + fName);
			}
			tt.qJap();
			tt.staticTable();
			tt.tnWriteEntry();
			tt.tnWriteTerm();
			tt.tnWriteDefinition();
			tt.tnWriteMeaning();
			tt.tnSelEntry();
			tt.tnSelTerm();
			tt.tnSelDefinition();
			tt.tnReadEntries();
			tt.tnReadTerms();
			tt.tnReadDefinitions();
			tt.tnReadMaxIds();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (tConn != null) {
				tConn.tnCloseConn();
			}
		}
	}
}
