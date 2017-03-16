all:

loc:
	wc -l \
	*/*/*java \
	*/*/*/*java \
	*/*/*/*/*java

jars: 
	cd ../bin; jar cfvm ../docubricks.jar ../src/MANIFEST.STARTQTCLIENT \
	*/*/*.class \
	*/*/*.css \
	*/*/*.xml \
	*/*/*/*.class \
	*/*/*/*.png

lib:
	cd ../bin; jar cfv ../docubricksLIB.jar \
	*/*/*.class \
	*/*/*/*.class
	cd ../bin; cp ../docubricksLIB.jar ../../docubricksSite/WebContent/WEB-INF/lib/

############################################################################################
############################ osx ###########################################################
############################################################################################

macport: jars
	rm -Rf ../release/mac
	cp -r ../ports/mac ../release/mac
	cp ../docubricks.jar ../lib/*jar ../lib/mac/*.jar ../release/mac/docubricks.app/
#	cp translations/* ../release/mac/docubricks.app/translations/
	cd ../release/mac/; make


############################################################################################
############################ linux #########################################################
############################################################################################

ubuntuport: jars
	sudo rm -f ../release/docubricks*deb
	cd ..; sudo java -cp bin port.ubuntu.Main
	sudo mv ../release/docubricks*deb ../release/docubricks.deb

linuxport32: jars
	mkdir -p ../release/linux32
	rm -Rf ../release/linux32/docubricks
	cd ..; cp -r ports/linux release/linux32/docubricks
	cd ..; cp -r docubricks.jar lib/*jar lib/linux32/*jar release/linux32/docubricks/
	#cp translations/* ../release/linux32/docubricks/translations/

linuxport64: jars
	mkdir -p ../release/linux64
	rm -Rf ../release/linux64/docubricks
	cd ..; cp -r ports/linux release/linux64/docubricks
	cd ..; cp -r docubricks.jar lib/*jar lib/linux64/*jar release/linux64/docubricks/
	#cp -r translations/* ../release/linux64/docubricks/translations/

linuxport: linuxport32 linuxport64 ubuntuport



############################################################################################
############################ windows #######################################################
############################################################################################

winportjre: jars
	rm -Rf ../release/winjre/
	mkdir -p ../release/winjre/docubricks
	cd ..; cp ports/win/starter/docubricks_bundledjre.exe release/winjre/docubricks/start.exe
	cd ..; cp ports/win/installer/* release/winjre/
	cd ..; cp docubricks.jar lib/*jar lib/win32/*jar release/winjre/docubricks/
	#cp -r translations ../release/winjre/docubricks/
	cd ..; cp -r ../support_programs/jre release/winjre/
	cd ../release/winjre; "/home/mahogny/.wine/drive_c/Program Files (x86)/NSIS/makensis.exe" bundledjre.nsi
	cd ../release/winjre; mv install.exe ../docubricks_winjre.exe


winportzip: jars
	rm -Rf ../release/docubricks_win/
	mkdir -p ../release/docubricks_win
	cd ..; cp ports/win/starter/docubricks_bundledjre.exe release/docubricks_win/docubricks.exe
	cd ..; cp docubricks.jar lib/*jar lib/win32/*jar release/docubricks_win/
	cd ..; cp -r ../support_programs/jre/* release/docubricks_win/



winport: winportzip                  # winportjre # winport32 winport64 winportjre


############################################################################################
############################ release and upload ############################################
############################################################################################

release: macport linuxport winport #docs
	mkdir -p ../release
	rm -f ../release/docubricks*zip 
	cd ../release/linux64; zip -r ../docubricks_linux64.zip  docubricks
	cd ../release/linux32; zip -r ../docubricks_linux32.zip  docubricks
	cd ../release/; zip -r docubricks_win docubricks_win
	#cd ports; zip -r ../../release/docs.zip            documentation
	cd ..; cp -f src/docubricks/gui/version.txt release/qtversion.txt

cleanports:
	cd ports; rm docubricks.app/*.jar
#	cd ports; rm linux/*.jar
#	cd ports; rm win32/*.jar
	cd ports; rm documentation/*


upload:
	cd ../release; scp *.dmg *.zip *.deb qtversion.txt mahogny@www.endrov.net:/home/mahogny/hp/temp

uploadwin:
	cd ../release; scp docubricks_win.zip mahogny@www.endrov.net:/home/mahogny/hp/temp


timestamp:
	cd ..; date +%s > bin/docubricks/gui/timestamp.txt

uploadbackup:
	cd ../..; scp -r labnote3.git 177655_mahogny@ssh.binero.se:/storage/content/55/177655/



############################################################################################
############################ misc ##########################################################
############################################################################################

lupdate:
	lupdate -no-obsolete -extensions java \
	*/*java \
	org/ecobima/labnote/*/*java \
	org/ecobima/labnote/*/*/*java \
	org/ecobima/labnote/*/*/*/*java \
	org/ecobima/labnote/*/*/*/*/*java \
	-ts translations/*ts





gitaddall:
	git add --all \
	*/*java \
	*/*/*java \
	*/*/*/*java \
	*/*/*png

