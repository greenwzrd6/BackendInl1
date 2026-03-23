#!/bin/bash
echo "Running the program"
echo ""

cd hibernateExcerciseBooks

mvn -q clean compile

mvn -q exec:java