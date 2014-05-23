#!/bin/sh

java -jar /home/shilpa/workspace/ssl.seeds/runnable-jar/SeedSelection.jar genClutoInput /home/shilpa/workspace/ssl.seeds/ 

./cluto-2.1.2/Linux-x86_64/vcluster -clmethod=graph -sim=dist -rowmodel=none -colmodel=none -clabelfile=/home/shilpa/workspace/ssl.seeds/seeddata/bigFileChunckscol5k.mat.clabel -rlabelfile=/home/shilpa/workspace/ssl.seeds/seeddata/bigFileChuncksrow5k.mat.rlabel -clustfile /home/shilpa/workspace/ssl.seeds/seeddata/solution1.sol /home/shilpa/workspace/ssl.seeds/seeddata/bigFileChuncks5k.mat 50

java -jar /home/shilpa/workspace/ssl.seeds/runnable-jar/SeedSelection.jar genTopk /home/shilpa/workspace/ssl.seeds/ 







