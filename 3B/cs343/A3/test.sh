echo "Testing no arguments"
./tokenring 2> myout
./q1 2> out
diff myout out

echo "Testing too many arguments"
./tokenring 1 2 3 4 5 6 7 2> myout
./q1 1 2 3 4 5 6 7 2> out
diff myout out

echo "Testing bad input file"
./tokenring 1 xxxxxxx 2> myout
./q1 1 xxxxxxx 2> out
diff myout out

echo "Testing bad number of stations"
echo "Please specify 1-100 stations" > out
./tokenring 0 t_empty 2> myout
diff myout out
./tokenring -1 t_empty 2> myout
diff myout out
./tokenring 1000 t_empty 2> myout
diff myout out
./tokenring abcd t_empty 2> myout
diff myout out

echo "Testing invalid requests"
./tokenring 5 t_invalid > myout 2> /dev/null
./q1 5 t_invalid > out 2> /dev/null
diff myout out

echo "Testing no requests"
./tokenring 5 t_empty > myout 2> /dev/null
./q1 5 t_empty > out 2> /dev/null
diff myout out

echo "Testing standard funtionality"
./tokenring 8 t_test > myout 2> /dev/null
./q1 8 t_test > out 2> /dev/null
diff myout out

echo "Testing random request order in input"
./tokenring 8 t_bad_order > myout 2> /dev/null
./q1 8 t_bad_order > out 2> /dev/null
diff myout out

echo "Testing huge specification"
python tester.py 200 100 > t_huge
./tokenring 100 t_huge > myout 2> /dev/null
./q1 100 t_huge > out 2> /dev/null
diff myout out

echo "Finished all tests"
