#
# file:		javadoc.sh
#
# purpose:	create Javadoc for the TransNook project
#
# assumes:	your current directory is the project root
#			AND
#			the project's "src" directory is in your current directory
#
javadoc -overview src/overview.html -sourcepath src -private -d doc cvctw.db.transnook cvctw.edict cvctw.transnook
