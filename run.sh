#!/bin/bash
echo "Running the program"
echo ""

cd hibernateExcerciseBooks

mvn clean compile

mvn -q exec:java