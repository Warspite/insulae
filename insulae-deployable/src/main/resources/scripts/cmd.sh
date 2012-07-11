SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

usage() {
	echo "-h: Display this help."
	echo "-b: Turns on batch mode and will not wait for output."
	echo "-i: The instance name to send the command to. Defaults to 'default'."
	echo "-w: Seconds to wait for output. Defaults to 5."
	echo "The actual command(s) to execute are piped into the script."
}

run() {
	pushd "${SCRIPT_DIR}/.." >/dev/null

	cmdId=$RANDOM
	path="runtime/${appInstance}/cmd"

	if [ ! -d "$path" ]; then
    	echo "Command input directory $path does not exist. Please make sure there is an instance named '$appInstance' running."
    	exit 1
	fi

	touch "${path}/${cmdId}.lck" 
	touch "${path}/${cmdId}.in"
	
	for cmd in "${cmds[@]}"
	do
		echo $cmd >> "${path}/${cmdId}.in"
	done
	
	rm "${path}/${cmdId}.lck" 

	if ! $batchMode; then
		waitForOutput
	fi

	popd >/dev/null
}

verifyArguments() {
	if [ ${#cmds[@]} -eq 0 ]; then
		echo "Error: Received no piped input."
		usage
		exit 1
	fi
}

readPipedInput() {
	while read -t 1 line; do
		cmds=( "${cmds[@]}" "$line" )
	done	
}

waitForOutput() {
	while [ $wait -gt 0 ]; do
		if ( [[ -e "${path}/${cmdId}.out" ]] && [[ ! -e "${path}/${cmdId}.lck" ]] ); then
			cat "${path}/${cmdId}.out"
			rm "${path}/${cmdId}.out"
			return
		fi
		echo "Waiting for unlocked output... ($wait seconds)"
		let wait=$wait-1
		sleep 1
	done

	echo "No output received :("
}

batchMode=false
cmds=()
appInstance="default"
let wait=5
while getopts “hbi:w:” OPTION
do
     case $OPTION in
         h)
             usage
             exit 0
             ;;
         b)
             batchMode=true
             ;;
         w)
             let wait=$OPTARG
             ;;
         i)
             appInstance=$OPTARG
             ;;
         ?)
             usage
             exit 0
             ;;
     esac
done

readPipedInput
verifyArguments
run