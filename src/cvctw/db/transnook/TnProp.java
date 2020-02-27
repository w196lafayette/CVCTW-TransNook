package cvctw.db.transnook;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * This is a SINGLETON class.  All "clients" will read the same properties' values.
 * 
 * @author minge
 *
 */
public class TnProp {
	public static final String SCHEMA = "transnook";

	public static final String DEFAULT_TERM_LANGUAGE = "jap";
	public static final String DEFAULT_DEFINITION_LANGUAGE = "eng";

	public static final String TABLE_ENTRIES = "entries";
	public static final String TABLE_TERMS = "terms";
	public static final String TABLE_READING_INFO = "readingInfo";
	public static final String COLUMN_READING_INFO = "info";
	public static final String TABLE_READING_INFO2TERMS = "readingInfo2terms";
	public static final String TABLE_KANJI_INFO = "kanjiInfo";
	public static final String COLUMN_KANJI_INFO = "info";
	public static final String TABLE_KANJI_INFO2TERMS = "kanjiInfo2terms";
	public static final String TABLE_DEFINITIONS = "definitions";
	public static final String TABLE_MEANINGS = "meanings";
	public static final String TABLE_LANGUAGES = "languages";
	public static final String COLUMN_LANGUAGES = "language";
	public static final String TABLE_CONTEXTS = "contexts";
	public static final String COLUMN_CONTEXTS = "context";
	public static final String TABLE_CONTEXTS2MEANINGS = "contexts2meanings";
	public static final String TABLE_ALPHABETS = "alphabets";
	public static final String COLUMN_ALPHABETS = "alphabet";
	public static final String TABLE_PARTSOFSPEECH = "partsOfSpeech";
	public static final String COLUMN_PARTSOFSPEECH = "part";
	public static final String TABLE_PARTSOFSPEECH2MEANINGS = "partsOfSpeech2meanings";
	public static final String TABLE_DIALECTS = "dialects";
	public static final String COLUMN_DIALECTS = "dialect";
	public static final String TABLE_DIALECTS2MEANINGS = "dialects2meanings";
	public static final String TABLE_ALTLANGUAGES2MEANINGS = "altLanguages2meanings";
	public static final String TABLE_SENSES = "senses";
	public static final String COLUMN_SENSES = "sense";
	public static final String TABLE_SENSES2MEANINGS = "senses2meanings";
	public static final String COLUMN_WASEI = "wasei";
	public static final String TABLE_WASEI2MEANINGS = "wasei2meanings";
	public static final String TABLE_XREFS = "xrefs";
	public static final String COLUMN_XREFS = "xref";
	public static final String TABLE_XREFS2MEANINGS = "xrefs2meanings";
	public static final String COLUMN_ANTONYMS = "antonym";
	public static final String TABLE_ANTONYMS2MEANINGS = "antonyms2meanings";
	public static final String COLUMN_MEANING_ID = "meaningId";
	public static final String COLUMN_TERM_ID = "termId";

	public static final String PROP_PROTOCOL = "tnConnProtocol";
	public static final String PROP_HOST = "tnConnHost";
	public static final String PROP_PORT = "tnConnPort";
	public static final String PROP_SCHEMA = "tnConnSchema";
	public static final String PROP_USER = "tnConnUser";
	public static final String PROP_PWD = "tnConnPwd";
	public static final String PROP_MAX_FAILURES = "tnMaxEntryFailures";

	public static final String SQL_SELECT = "SELECT ";
	public static final String SQL_FROM = " FROM ";
	public static final String SQL_WHERE = " WHERE ";
	public static final String SQL_HAVING = " HAVING ";
	public static final String SQL_GROUP_BY = " GROUP BY ";
	
	private static String connProtocol = null;
	private static String connHost = null;
	private static String connPort = null;
	private static String connSchema = null;
	private static String connUser = null;
	private static String connPwd = null;
	private static Integer dbMaxEntryFailures = null;

	// Signal that it's been instantiated or not
	private static TnProp m_instance = null;

	private TnProp() {
		// disallow
	}
	private TnProp(String path) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileReader(path));
		connProtocol = p.getProperty(PROP_PROTOCOL);
		connHost = p.getProperty(PROP_HOST);
		connPort = p.getProperty(PROP_PORT);
		connSchema = p.getProperty(PROP_SCHEMA);
		connUser = p.getProperty(PROP_USER);
		connPwd = p.getProperty(PROP_PWD);
		dbMaxEntryFailures = Integer.valueOf(p.getProperty(PROP_MAX_FAILURES));
	}

	public static synchronized TnProp getInstance() throws FileNotFoundException, IOException {
		return getInstance("tnProperties.prop");
	}

	public static synchronized TnProp getInstance(String path) throws FileNotFoundException, IOException {
		if (m_instance == null) {
			m_instance = new TnProp(path);
		}
		return m_instance;
	}
	public String getConnProtocol() {
		return connProtocol;
	}

	public String getConnHost() {
		return connHost;
	}

	public String getConnPort() {
		return connPort;
	}

	public String getConnSchema() {
		return connSchema;
	}

	public String getConnUser() {
		return connUser;
	}

	public String getConnPwd() {
		return connPwd;
	}

	public Integer getDbMaxEntryFailures() {
		return dbMaxEntryFailures;
	}
}
