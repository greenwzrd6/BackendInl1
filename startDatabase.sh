#!/bin/bash
echo "Running database"
echo ""

cd db-derby-10.17.1.0-bin/bin

chmod +x ./NetworkServerControl

./NetworkServerControl -p 50000 start