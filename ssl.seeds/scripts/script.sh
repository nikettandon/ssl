#!/bin/sh

# input graph file must be sorted (and appended? with b-->a)
awk 'BEGIN{OFS=FS="\t"} {tmp=$1;$1=$2;$2=tmp;print}' /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/sensitivity.sims >/home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/reverse.txt
cat /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/sensitivity.sims /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/reverse.txt > /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/input.txt
sort -t$'\t' -k1,1 -u /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/input.txt -o /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/sortedinput.txt

cat /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/sortedinput.txt | tr "\t" "\n" |sed '/^0\./d' | sed '/^1\.0/d' | sort | uniq  > /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/uniqueword.txt

java -jar /home/shilpa/git/ssl/ssl.seeds/runnable-jar/SeedSelection.jar genClutoInput /home/shilpa/git/ssl/ssl.seeds/ 

./cluto-2.1.2/Linux-x86_64/vcluster -clmethod=graph -nnbrs=100 -rowmodel=none -colmodel=none -clabelfile=/home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/bigFileChunckscol.mat.clabel -rlabelfile=/home/shilpa/git/ssl/ssl.seeds/data/eval-data/senstivity/bigFileChuncksrow.mat.rlabel -clustfile /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/bigsolution.sol /home/shilpa/git/ssl/ssl.seeds/data/eval-data/sensitivity/bigFileChuncks.mat 300

java -jar /home/shilpa/git/ssl/ssl.seeds/runnable-jar/SeedSelection.jar genTopK /home/shilpa/git/ssl/ssl.seeds/ 







