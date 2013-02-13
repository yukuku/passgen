# build script for callcentre
#
# environment vars that needs to be defined before running this
#
# SIGN_KEYSTORE = where the keystore is
# SIGN_ALIAS = key alias
# SIGN_PASSWORD = (string)

SUPER_PROJECT_NAME=passgen
MAIN_PROJECT_NAME=PassgenApp

#############################################

THIS_SCRIPT_FILE=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)/`basename "${BASH_SOURCE[0]}"`
THIS_SCRIPT_DIR=`dirname $THIS_SCRIPT_FILE`

if [ "$SIGN_KEYSTORE" == "" -o \! -r "$SIGN_KEYSTORE" ] ; then
	echo 'SIGN_KEYSTORE not defined or not readable'
	exit 1
fi

if [ "$SIGN_ALIAS" == "" ] ; then
	echo 'SIGN_ALIAS not defined'
	exit 1
fi

if [ "$SIGN_PASSWORD" == "" ] ; then
	echo 'SIGN_PASSWORD not defined'
	exit 1
fi

if [ \! \( -d "$MAIN_PROJECT_NAME" \) ] ; then
	echo "Must be run from $SUPER_PROJECT_NAME dir, which contains $MAIN_PROJECT_NAME, etc directories"
	exit 1
fi

# get the value of an xml attribute (of arbritrary tag)
get_attr() {
	FILE="$1"
	ATTR="$2"
	cat "$FILE" | grep -E "$ATTR=\"[^\"]*\"" | sed -E "s/^.*$ATTR=\"([^\"]*)\".*$/\1/"
}

# replace '0000000' on the specified filename with the last commit hash of the git repo
write_last_commit_hash() {
	FILE="$1"
	LAST_COMMIT_HASH=`git log -1 --format='format:%h'`
	echo 'Setting last commit hash: '$LAST_COMMIT_HASH' to '$FILE
	sed -i '' "s/0000000/$LAST_COMMIT_HASH/g" "$FILE"
}

overlay() {
	P_SRC="$1"
	P_DST="$2"

	SRC="$THIS_SCRIPT_DIR/build/overlay/$PKGDIST/$P_SRC"
	DST="$BUILD_MAIN_PROJECT_DIR/$P_DST"

	echo "Overlaying $P_DST with $P_SRC..."

	if [ \! -e `dirname "$DST"` ] ; then
		echo 'Making dir for overlay destination...'
		mkdir -p "`dirname "$DST"`"
	fi

	cp "$SRC" "$DST" || read
}

overlay_optional() {
	P_SRC="$1"

	SRC="$BUILD_MAIN_PROJECT_DIR/publikasi/overlay/$PKGDIST/$P_SRC"

	if [ \! -e "$SRC" ] ; then
		echo "Overlay file $P_SRC not available but still OK because it's optional."
	else
		overlay "$1" "$2"
	fi
}

# supports multiline regex due to the complicated sed script
replace_in_file() {
	FILE="$1"
	FIND="$2"
	REPLACE="$3"

	if python "$THIS_SCRIPT_DIR/build/tools/findinfile.py" "$FILE" "$FIND" ; then
		echo "Replacing in '$FILE'..."
		python "$THIS_SCRIPT_DIR/build/tools/replinfile.py" "$FILE" "$FIND" "$REPLACE"
	fi
}

# START BUILD-SPECIFIC

if [ "$BUILD_PACKAGE_NAME" == "" ] ; then
	echo 'BUILD_PACKAGE_NAME not defined'
	exit 1
fi

if [ "$BUILD_DIST" == "" ] ; then
	echo 'BUILD_DIST not defined'
	exit 1
fi

# END BUILD-SPECIFIC


echo 'Creating 500 MB ramdisk...'

BUILD_NAME=$SUPER_PROJECT_NAME-build-`date "+%Y%m%d-%H%M%S"`
diskutil erasevolume HFS+ $BUILD_NAME `hdiutil attach -nomount ram://1024000`

BUILD_DIR=/Volumes/$BUILD_NAME

echo 'Build dir:' $BUILD_DIR

if [ ! -d $BUILD_DIR ] ; then
	echo 'Build dir not mounted correctly'
	exit 1
fi

echo 'Copying yuku-android-util...'
mkdir $BUILD_DIR/yuku-android-util
cp -R ../yuku-android-util/ $BUILD_DIR/yuku-android-util/

echo "Copying $SUPER_PROJECT_NAME..."
mkdir $BUILD_DIR/$SUPER_PROJECT_NAME
cp -R ./ $BUILD_DIR/$SUPER_PROJECT_NAME/

echo 'Going to' $BUILD_DIR/$SUPER_PROJECT_NAME
pushd $BUILD_DIR/$SUPER_PROJECT_NAME

	BUILD_MAIN_PROJECT_DIR=$BUILD_DIR/$SUPER_PROJECT_NAME/$MAIN_PROJECT_NAME

	pushd $MAIN_PROJECT_NAME

		# START BUILD-SPECIFIC

		PKGDIST="$BUILD_PACKAGE_NAME-$BUILD_DIST"

		echo '========================================='
		echo 'Build Config for THIS build:'
		echo '  BUILD_PACKAGE_NAME    = ' $BUILD_PACKAGE_NAME
		echo '  BUILD_DIST            = ' $BUILD_DIST
		echo '  PKGDIST               = ' $PKGDIST
		echo '========================================='

		# END BUILD-SPECIFIC


		MANIFEST_PACKAGE_NAME=`get_attr AndroidManifest.xml package`
		MANIFEST_VERSION_CODE=`get_attr AndroidManifest.xml versionCode`
		MANIFEST_VERSION_NAME=`get_attr AndroidManifest.xml versionName`

		echo '========================================='
		echo 'From AndroidManifest.xml:'
		echo '  Package name    = ' $MANIFEST_PACKAGE_NAME
		echo '  Version code    = ' $MANIFEST_VERSION_CODE
		echo '  Version name    = ' $MANIFEST_VERSION_NAME
		echo ''
		echo 'SIGN_KEYSTORE   = ' $SIGN_KEYSTORE
		echo 'SIGN_ALIAS      = ' $SIGN_ALIAS
		echo 'SIGN_PASSWORD   = ' '.... =)'
		echo '========================================='

		if [ -e res/values/last_commit.xml ] ; then
			write_last_commit_hash res/values/last_commit.xml
		fi

		ant clean
		ant -Dkey.store="$SIGN_KEYSTORE" -Dkey.store.password="$SIGN_PASSWORD" -Dkey.alias="$SIGN_ALIAS" -Dkey.alias.password="$SIGN_PASSWORD" release

		if [ \! -r $BUILD_MAIN_PROJECT_DIR/bin/$MAIN_PROJECT_NAME-release.apk ] ; then
			echo $BUILD_MAIN_PROJECT_DIR/bin/$MAIN_PROJECT_NAME-release.apk ' not found. '
			echo 'Ant FAILED'
			exit 1
		fi

		OUTPUT_APK=$BUILD_DIR/$MAIN_PROJECT_NAME-$MANIFEST_VERSION_CODE-$MANIFEST_VERSION_NAME-$PKGDIST.apk

		mv $BUILD_MAIN_PROJECT_DIR/bin/$MAIN_PROJECT_NAME-release.apk "$OUTPUT_APK"

		echo 'BUILD SUCCESSFUL.'
		echo 'Output APK:' $OUTPUT_APK
	popd
popd

