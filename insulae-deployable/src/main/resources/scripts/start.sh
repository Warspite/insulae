SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
pushd "${SCRIPT_DIR}/.."
java -classpath "./configuration:./libs/*" com.warspite.insulae.EntryPoint $1
popd