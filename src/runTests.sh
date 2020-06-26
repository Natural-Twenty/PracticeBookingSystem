#!/bin/sh
javac -cp ".:../lib/json.jar" unsw/venues/*.java
for i in 0
do
    java -ea -cp ".:../lib/json.jar" unsw/venues/VenueHireSystem < ../input$i.json > outp
    diff outp ../output$i.json
done
rm out
