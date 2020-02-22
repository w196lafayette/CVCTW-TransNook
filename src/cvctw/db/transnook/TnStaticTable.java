/**
 * 
 */
package cvctw.db.transnook;

import java.sql.SQLException;
import java.util.TreeMap;

/**
 * @author minge
 *
 */
public class TnStaticTable {

	public class TableColumnMap {
		TreeMap<String, String> tcm = new TreeMap<String, String>();
		TableColumnMap() {
			tcm.put(TnProp.TABLE_CONTEXTS, TnProp.COLUMN_CONTEXTS);
			tcm.put(TnProp.TABLE_DIALECTS, TnProp.COLUMN_DIALECTS);
			tcm.put(TnProp.TABLE_LANGUAGES, TnProp.COLUMN_LANGUAGES);
			tcm.put(TnProp.TABLE_PARTSOFSPEECH, TnProp.COLUMN_PARTSOFSPEECH);
			tcm.put(TnProp.TABLE_SENSES, TnProp.COLUMN_SENSES);
			tcm.put(TnProp.TABLE_ALPHABETS, TnProp.COLUMN_ALPHABETS);
			tcm.put(TnProp.TABLE_XREFS, TnProp.COLUMN_XREFS);
		}
		String column(String table) throws Exception {
			String ret = tcm.get(table);
			if (ret == null) {
				throw new Exception("Table " + table + " not found");
			}
			return ret;
		}
	}

	private TableColumnMap t2c = null;
	private TnRowReader rowReader = null;
	private TnRowWriter rowWriter = null;

	@SuppressWarnings("unused")
	private TnStaticTable() {
		; // disable this ctor
	}

	public TnStaticTable(TnRowReader r, TnRowWriter w) {
		rowReader = r;
		rowWriter = w;
		t2c = new TableColumnMap();
	}

	public void insertIfMissing(String table, String inVal) throws SQLException, Exception  {
		String column = t2c.column(table);
		boolean isHere = rowReader.isRowPresent(table, column, inVal, true);
		if (isHere != true) {
			rowWriter.writeRow(table, column, inVal, true);
		}
	}
}
