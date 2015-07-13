all:
	rm -Rf foo
	mkdir foo
	cp -r docubricks.app foo/
	genisoimage -o ../docubricks_osx.dmg -V docubricks -r -apple foo
