WHERE=.
TESTS=$WHERE/tests/


for test in `find $TESTS -name '*.488'`
do
    echo "Running test: $test"
    java -jar $WHERE/dist/compiler488.jar  $test
done
