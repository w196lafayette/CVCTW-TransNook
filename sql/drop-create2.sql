DROP DATABASE IF EXISTS transnook;
CREATE DATABASE transnook;
USE transnook;
-- 
-- table=LANGUAGES
-- 
-- DROP TABLE IF EXISTS languages;
CREATE TABLE languages 
(
language VARCHAR(64) NOT NULL,
PRIMARY KEY(language)
);
-- 
-- table=ALPHABETS
-- 
-- DROP TABLE IF EXISTS alphabets;
CREATE TABLE alphabets 
(
alphabet VARCHAR(64) NOT NULL,
PRIMARY KEY(alphabet)
);
-- 
-- table=CONTEXTS
-- 
-- DROP TABLE IF EXISTS contexts;
CREATE TABLE contexts
(
context VARCHAR(64),
PRIMARY KEY (context)
);

-- 
-- table=PARTSOFSPEECH
-- 
-- DROP TABLE IF EXISTS partsOfSpeech;
CREATE TABLE partsOfSpeech
(
part VARCHAR(64),
PRIMARY KEY (part)
);
-- 
-- table=ENTRIES
-- 
CREATE TABLE entries
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
-- full dictionary entry
entry VARCHAR(4096) CHARACTER SET binary NOT NULL,
language VARCHAR(64) NOT NULL,
FOREIGN KEY (language) REFERENCES languages (language),
eDictId VARCHAR(64),
UNIQUE INDEX(edictID),
source CHAR(1),
CONSTRAINT source_check CHECK (source in ('E','F','W'))
);
-- 
-- table=TERMS
-- 
-- DROP TABLE IF EXISTS terms;
CREATE TABLE terms
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
entryId INTEGER NOT NULL,
FOREIGN KEY (entryId) REFERENCES entries (id),
term VARCHAR(512) CHARACTER SET binary NOT NULL,
INDEX(term),
alphabet VARCHAR(64) DEFAULT 'latin',
FOREIGN KEY(alphabet) REFERENCES alphabets(alphabet),
-- UNIQUE INDEX(entryId,term,alphabet),
termType CHAR NOT NULL,
-- m=major, a=alternate
CONSTRAINT term_type_check CHECK (termType IN ('m','a'))
);
-- 
-- table=READINGINFO
--
CREATE TABLE readinginfo
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
info VARCHAR(32) NOT NULL,
UNIQUE INDEX (info)
);
-- 
-- table=KANJIINFO
--
CREATE TABLE kanjiinfo
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
info VARCHAR(32) NOT NULL,
UNIQUE INDEX (info)
);
-- 
-- table=READINGINFO2TERMS
-- 
CREATE TABLE readinginfo2terms
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
termId INTEGER NOT NULL,
FOREIGN KEY (termId) REFERENCES terms(id),
info VARCHAR(32) NOT NULL,
FOREIGN KEY (info) REFERENCES readinginfo(info),
UNIQUE INDEX (termId,info)
);
-- 
-- table=KANJIINFO2TERMS
-- 
CREATE TABLE kanjiinfo2terms
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
termId INTEGER NOT NULL,
FOREIGN KEY (termId) REFERENCES terms(id),
info VARCHAR(32) NOT NULL,
FOREIGN KEY (info) REFERENCES kanjiinfo(info),
UNIQUE INDEX (termId,info)
);
-- 
-- table=DEFINITIONS
-- 
-- DROP TABLE IF EXISTS definitions;
CREATE TABLE definitions
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
entryId INTEGER NOT NULL,
FOREIGN KEY (entryId) REFERENCES entries (id),
defOrder INTEGER NOT NULL,
UNIQUE INDEX (defOrder, entryId),
definition VARCHAR(2048) NOT NULL,
language VARCHAR(64) NOT NULL,
FOREIGN KEY (language) REFERENCES languages(language)
);
-- 
-- table=MEANINGS
-- 
-- DROP TABLE IF EXISTS meanings;
CREATE TABLE meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
entryId INTEGER NOT NULL,
FOREIGN KEY (entryId) REFERENCES entries (id),
defId INTEGER NOT NULL,
FOREIGN KEY (defId) REFERENCES definitions (id),
meaningOrder INTEGER NOT NULL,
UNIQUE INDEX (meaningOrder, defId),
meaning VARCHAR(512) NOT NULL,
INDEX (meaning)
) CHARSET=utf8mb4;
-- 
-- table=CONTEXT2MEANINGS
-- 
CREATE TABLE contexts2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
context VARCHAR(64),
FOREIGN KEY (context) REFERENCES contexts (context),
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
UNIQUE INDEX (context, meaningId)
);
CREATE TABLE partsOfSpeech2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
part VARCHAR(64),
FOREIGN KEY (part) REFERENCES partsOfSpeech (part),
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
UNIQUE INDEX (part, meaningId)
);
CREATE TABLE senses
(
sense VARCHAR(64),
PRIMARY KEY (sense)
);
CREATE TABLE senses2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
sense VARCHAR(64),
FOREIGN KEY (sense) REFERENCES senses (sense),
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
UNIQUE INDEX (sense, meaningId)
);
CREATE TABLE dialects
(
dialect VARCHAR(64),
PRIMARY KEY (dialect)
);
CREATE TABLE dialects2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
dialect VARCHAR(64),
FOREIGN KEY (dialect) REFERENCES dialects (dialect),
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
UNIQUE INDEX (dialect, meaningId)
);
CREATE TABLE altLanguages2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
language VARCHAR(64),
FOREIGN KEY (language) REFERENCES languages (language),
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
term VARCHAR(512) CHARACTER SET binary,
UNIQUE INDEX (language,meaningId,term)
);
CREATE TABLE wasei2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
wasei VARCHAR(512),
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id) -- ,
-- UNIQUE INDEX (wasei,meaningId)
);
CREATE TABLE xrefs
(
xref VARCHAR(64),
PRIMARY KEY (xref)
);
CREATE TABLE xrefs2meanings
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
xref VARCHAR(16),
FOREIGN KEY (xref) REFERENCES xrefs (xref),
xrefText VARCHAR(512) CHARACTER SET binary,
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
UNIQUE INDEX (xref,xrefText,meaningId)
);
CREATE TABLE antonyms2meanings
(
antonym VARCHAR(512) CHARACTER SET binary,
meaningId INTEGER,
FOREIGN KEY (meaningId) REFERENCES meanings (id),
PRIMARY KEY (antonym,meaningId)
);
-- 
-- table=USERS
-- 
-- DROP TABLE IF EXISTS users;
CREATE TABLE users 
(
id INTEGER NOT NULL AUTO_INCREMENT,
userId VARCHAR(128) NOT NULL,
firstName VARCHAR(128) NOT NULL,
middleNames VARCHAR(128),
lastName VARCHAR(128) NOT NULL,
password VARCHAR(128) NOT NULL,
email VARCHAR(128),
PRIMARY KEY(id),
UNIQUE INDEX userIdx(userId),
INDEX nameIdx(lastName,firstName)
);
-- 
-- table=TRANSLATORS2LANGUAGES
-- maps users(translators) to the languages they know
-- 
-- DROP TABLE IF EXISTS translators2languages;
CREATE TABLE translators2languages
(
user VARCHAR(128) NOT NULL,
fromLanguage VARCHAR(64) NOT NULL,
toLanguage VARCHAR(64) NOT NULL,
PRIMARY KEY (user, fromLanguage, toLanguage),
FOREIGN KEY (user) REFERENCES users(userID),
FOREIGN KEY (toLanguage) REFERENCES languages(language),
FOREIGN KEY (fromLanguage) REFERENCES languages(language)
);
--
-- table=PROJECTS
--
-- DROP TABLE IF EXISTS projects;
CREATE TABLE projects
(
id INTEGER NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id),
createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
updatedDate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
projectId VARCHAR(128) NOT NULL,
UNIQUE INDEX projIdx (projectId),
requestor VARCHAR(128) NOT NULL,
FOREIGN KEY (requestor) REFERENCES users(userId),
dueDate DATE,
translator VARCHAR(128),
FOREIGN KEY (translator) REFERENCES users(userId),
context VARCHAR(64),
FOREIGN KEY (context) REFERENCES contexts(context),
sourceText LONGTEXT,
INDEX sourceIdx ((SUBSTRING(sourceText, 1, 50))),
sourceLanguage VARCHAR(64),
FOREIGN KEY (sourceLanguage) REFERENCES languages(language),
translatedText LONGTEXT,
translatedLanguage VARCHAR(64),
FOREIGN KEY (translatedLanguage) REFERENCES languages(language),
FOREIGN KEY (translator,sourceLanguage,translatedLanguage) REFERENCES translators2languages(user,fromLanguage,toLanguage)
) CHARSET=utf8mb4;
