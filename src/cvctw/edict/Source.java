/**
 * 
 */
package cvctw.edict;

/**
 * <p>
 * This enum represents the three sources by which entries can be added to the TransNook dictionary database.
 * </p>
 * <p>
 * The three possible sources are
 * <ul>
 * <li>
 * E, that is the JMdict/EDICT file
 * </li>
 * <li>
 * F, that is a formatted file submitted to the EdictToDatabase application.
 * The format of the file is that of the JMdict/EDICT file
 * </li>
 * <li>
 * W, that is the web page maintained by the CVCTW TransNook project
 * </li>
 * </ul>
 * </p>
 * 
 * @author minge
 *
 */
public enum Source {
	/**
	 * The source is the JMdict/EDICT file
	 */
	E,	// Japanese Edict
	/**
	 * The source is a formatted file submitted to the EdictToDatabase application.
	 */
	F,	// formatted file
	/**
	 * The source is the web page maintained by the CVCTW TransNook project
	 */
	W	// Web page
}
