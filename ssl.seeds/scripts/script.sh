#!/bin/sh

# input graph file must be sorted (and appended? with b-->a)
java -jar /home/shilpa/git/ssl/ssl.seeds/runnable-jar/SeedSelection.jar genClutoInput /home/shilpa/git/ssl/ssl.seeds/ 

./cluto-2.1.2/Linux-x86_64/vcluster -clmethod=graph -mincomponent=20 -sim=dist -rowmodel=none -colmodel=none -clabelfile=/home/shilpa/git/ssl/ssl.seeds/data/bigFileChunckscol.mat.clabel -rlabelfile=/home/shilpa/git/ssl/ssl.seeds/data/bigFileChuncksrow.mat.rlabel -clustfile /home/shilpa/git/ssl/ssl.seeds/data/bigsolution.sol /home/shilpa/git/ssl/ssl.seeds/data/bigFileChuncks.mat 500


java -jar /home/shilpa/git/ssl/ssl.seeds/runnable-jar/SeedSelection.jar genTopK /home/shilpa/git/ssl/ssl.seeds/ 







