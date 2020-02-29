/**
 * 
 */
package cvctw.edict;

import java.util.TreeSet;

/**
 * This class represents the Source "attribute" and implements the methods of the EdictEnum interface.
 * <br>
 * See the EdictEnum javadoc item for a description of the purpose of each method.
 * 
 * @author minge
 *
 */
public class Source implements EdictEnum {
	public enum SourceE {
		/**
		 * The sourceE is the JMdict/EDICT file
		 */
		E,	// Japanese Edict
		/**
		 * The sourceE is a formatted file submitted to the EdictToDatabase application.
		 */
		F,	// formatted file
		/**
		 * The sourceE is the web page maintained by the CVCTW TransNook project
		 */
		W	// Web page
	}

	// List of all enums that have been enumsWritten to the database
	private static TreeSet<String> enumsWritten = null;

	public Source() {
		enumsWritten = new TreeSet<String>();
	}

	@Override
	public String getTag(String inVal) {
		return "";
	}

	@Override
	public String getText(String inVal) {
		return "";
	}

	@Override
	public String getTable() {
		return "";
	}

	@Override
	public String getColumn() {
		return "";
	}

	@Override
	public String getToTable() {
		return "";
	}

	@Override
	public String getToColumn() {
		return "";
	}

	@Override
	public boolean isValid(String inVal) {
		String goodVal = getTag(inVal);
		for (SourceE v : SourceE.values()) {
			if (goodVal.equals(v.toString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isWritten(String inVal) {
		if (enumsWritten.contains(inVal)) {
			return true;
		}
		return false;
	}

	@Override
	public void written(String inVal) {
		enumsWritten.add(inVal);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
