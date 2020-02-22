/**
 * The cvctw.transnook package contains one class, EdictToDatabase.
 * 
 * The EdictToDatabase class executes all functionality of the TransNook project, namely
 * 1) reads the Japanese eDict file
 * 2) converts each eDict entry to a hierarchy of Java instances (under the "parent" EdictEntry instance)
 * 3) writes each Java EdictEntry instance to the database
 * 
 * The cvctw.edict package contains all classes that contribute to reading the eDict file
 * and converting it to Java instances
 * 
 * The cvctw.db.transnook package contains all classes that contribute to writing the Java 
 * instances to the database.
 * 
 * For any application that makes use of the cvctw.db.transnook classes, the ORDER of instantiation is crucial
 * and is, sadly, not intuitive.  That order goes like this:
 * 1) create a TnProp instance with TnProp.getInstance(pathOfPropertiesFile)
 *     This gets your application access to all of the properties for the application. 
 * 2) create a TnConnection instance with TnConnection.getInstance()
 * 3) connect to the database with tnConn.tnConnDataSource (this one is preferred)
 *      or with tnConn.tnConnDriverMgr (this one contains a required method that is deprecated)
 *      
 * For any application that makes use of the cvctw.edict package's classes, 
 * 1) the EdictReader class constructor itself reads the eDict file
 * 2) the Dictionary class constructor itself converts the eDict records to Java instances
 * No need to invoke any methods beyond the constructors, other than to retrieve the results
 * of each with "get" methods.
 */
package cvctw.transnook;
