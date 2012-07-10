SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $SCRIPT_DIR/lib/executeTestScripts.sh

executeTestScripts $SCRIPT_DIR system-test