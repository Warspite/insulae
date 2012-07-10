executeTestScripts() {
	scriptsRoot=$1
	targetSubDir=$2
	
	if [ ! -d "$scriptsRoot/$targetSubDir" ] ; then
		echo "Test directory "$scriptsRoot/$targetSubDir" does not exist, no scripts to run."
		exit 0
	fi

	pushd "$scriptsRoot/.." $@ > /dev/null

	executions=0
	failures=0
	for f in `find $scriptsRoot/$targetSubDir -type f` ; do
		echo "Executing $f"
		echo "----------------"
		eval $f
		retcode=$?
		echo "----------------"
	
		if [ $retcode != 0 ]; then
			let failures=failures+1
			echo "Test script execution failed!"
		fi
	
		echo ""
	
		let executions=executions+1
	done

	popd $@ > /dev/null

	echo "$executions test scripts executed."

	if [ $failures != 0 ]; then
		echo "THERE WERE $failures FAILURES!"
		exit 1
	fi

}



