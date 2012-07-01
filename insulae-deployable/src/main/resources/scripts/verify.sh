SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VERIFICATION_DIR="${SCRIPT_DIR}/verification"

if [ ! -d ${VERIFICATION_DIR} ] ; then
	echo "Directory ${VERIFICATION_DIR} does not exist."
	exit 0
fi

pushd "${SCRIPT_DIR}/.." $@ > /dev/null

executions=0
failures=0
for f in `find ${VERIFICATION_DIR} -type f` ; do
	echo "Executing $f"
	echo "----------------"
	eval $f
	retcode=$?
	echo "----------------"
	
	if [ $retcode != 0 ]; then
		let failures=failures+1
		echo "VERIFICATION SCRIPT FAILED!"
	fi
	
	echo ""
	
	let executions=executions+1
done


popd $@ > /dev/null

echo "$executions verification scripts executed."
if [ $failures != 0 ]; then
	echo "THERE WERE $failures FAILURES!"
	exit 1
fi
