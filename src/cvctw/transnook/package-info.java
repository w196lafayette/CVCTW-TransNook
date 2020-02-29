/**
 * <p>
 * This package is part of the CVCTW TransNook project, making use of the other packages in the project
 * to read and parse the JMdict/EDICT dictionary file and to create the CVCTW TransNook database.  
 * The package contains only one class, <i>EdictToDatabase</i>
 * </p>
 * <ol>
 * <li>
 * reads the Japanese-English JMdict/EDICT file
 * </li>
 * <li>
 *  converts each JMdict/EDICT entry to a hierarchy of Java instances (under the "parent" EdictEntry instance)
 * </li>
 * <li>
 * writes each Java EdictEntry instance to the database
 * </li>
 * </ol>
 * <p>
 * The cvctw.edict package contains all classes that contribute to reading the eDict file
 * and converting it to Java instances
 * </p>
 * <p>
 * The cvctw.db.transnook package contains all classes that contribute to writing the Java 
 * instances to the database.
 * </p>
 * <p>
 * For any application that makes use of the cvctw.db.transnook classes, the ORDER of instantiation is crucial
 * and is, sadly, not intuitive.  That order goes like this:
 * <ol>
 * <li>
 * create a TnProp instance with TnProp.getInstance(pathOfPropertiesFile).<br>
 * This gets your application access to all of the properties for the application. 
 * </li>
 * <li>
 * create a TnConnection instance with TnConnection.getInstance()
 * </li>
 * <li>
 * connect to the database with tnConn.tnConnDataSource (this one is preferred) <br>
 *      or with tnConn.tnConnDriverMgr (this one contains a required method that is deprecated)
 * </li>
 * </ol>
 * </p>
 * <p>
 * For any application that makes use of the cvctw.edict package's classes, 
 * <ol>
 * <li>
 * the EdictReader class constructor itself reads the eDict file
 * </li>
 * <li>
 * the Dictionary class constructor itself converts the eDict records to Java instances
 * </li>
 * </ol>
 * No need to invoke any methods beyond the constructors, other than to retrieve the results
 * of each with "get" methods.
 * </p>
 */
package cvctw.transnook;
