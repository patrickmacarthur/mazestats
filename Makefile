# Time-stamp: <08 Sep 2008 at 11:32:17 by charpov on copland.cs.unh.edu>

# if PACKAGE = guess:
# 'make guess' compiles the package
# 'make html' generates the documentation
# 'make guess.jar' produces a jar file with bytecode only
# 'make guess-full.jar' produces a jar file that includes sources and doc

PACKAGES = maze
MAIN_PACKAGE = maze
RESOURCE =

CLASSPATH = .
JAVAC = javac -Xlint
SOURCE = $(foreach PACKAGE,$(PACKAGES),$(filter-out $(PACKAGE)/package-info.java,$(wildcard $(PACKAGE)/*.java)))
BYTECODE = $(patsubst %.java,%.class,$(SOURCE))
JVMARGS = -ea

CLASSPATH_TEST = ${CLASSPATH}:test:junit4.jar:charpov.jar
TEST_SOURCE = $(wildcard test/*.java)
TEST_BYTECODE = $(patsubst %.java,%.class,$(TEST_SOURCE))
TEST_CLASSES = $(patsubst test/%.java,%,$(TEST_SOURCE))
TEST_JVMARGS = $(JVMARGS)

all: $(MAIN_PACKAGE).jar $(MAIN_PACKAGE)-full.jar

$(MAIN_PACKAGE): $(BYTECODE)

test/%.class: test/%.java
	$(JAVAC) -cp '${CLASSPATH_TEST}' $<

%.class: %.java
	$(JAVAC) $<

html/index.html: $(SOURCE) $(foreach PACKAGE,$(PACKAGES),$(PACKAGE)/package-info.java)
	test -d html/ || mkdir html
	javadoc -private -d html $(subst /,.,$(PACKAGES))

$(MAIN_PACKAGE).jar: $(BYTECODE) manifest
	jar cmf manifest $(MAIN_PACKAGE).jar\
		$(foreach PACKAGE,$(PACKAGES),$(PACKAGE)/*.class) $(RESOURCE)

$(MAIN_PACKAGE)-full.jar: $(BYTECODE) manifest html/index.html
	jar cmf manifest $(MAIN_PACKAGE)-full.jar\
		$(foreach PACKAGE,$(PACKAGES),$(PACKAGE)/*.class)\
		$(foreach PACKAGE,$(PACKAGES),$(PACKAGE)/*.java)\
		html $(RESOURCE)

html: html/index.html

test: $(PACKAGES) $(TEST_BYTECODE) test/outErr
	java $(JVMARGS) -cp '$(CLASSPATH_TEST)' org.junit.runner.JUnitCore $(TEST_CLASSES)

test/outErr: test/outErr.o
test/outErr.o: test/outErr.c

clean:
	/bin/rm -rf $(foreach PACKAGE,$(PACKAGES),$(PACKAGE)/*.class) test/*.class html

distclean: clean
	/bin/rm -f $(MAIN_PACKAGE).jar $(MAIN_PACKAGE)-full.jar

.PHONY : clean distclean test

