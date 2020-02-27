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
			tcm.put(TnProp.TABLE_READING_INFO, TnProp.COLUMN_READING_INFO);
			tcm.put(TnProp.TABLE_KANJI_INFO, TnProp.COLUMN_KANJI_INFO);
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
//	public static Context context = null;

	@SuppressWarnings("unused")
	private TnStaticTable() {
		; // disable this ctor
	}

	public TnStaticTable(TnRowReader r, TnRowWriter w) {
		rowReader = r;
		rowWriter = w;
		t2c = new TableColumnMap();
//		context = Context.getInstance(rowReader, rowWriter);
	}

	public void insertIfMissing(String table, String inVal) throws SQLException, Exception  {
		String column = t2c.column(table);
		boolean isHere = rowReader.isRowPresent(table, column, inVal, true);
		if (isHere != true) {
			rowWriter.writeRow(table, column, inVal, true);
		}
	}
	
//	public Integer insertIfMissing(String table, String column1, String inVal1, String column2, String inVal2) 
//			throws SQLException, Exception  {
//		Integer rowId = null;
//		boolean isHere = rowReader.isRowPresent(table, column1, inVal1, column2, inVal2);
//		if (isHere != true) {
//			String columnList = column1 + "," + column2;
//			String valueList = inVal1 + "','" + inVal2;
//			rowId = rowWriter.writeRow(table, columnList, valueList, true);
//		}
//		return rowId;
//	}
}
