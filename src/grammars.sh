export CLASSPATH=./algo:./dialect:./core

#autogram -t agast -l java -o core/be/uclouvain/jail/vm/autogram/v2 core/be/uclouvain/jail/vm/autogram/v2/JailUtils.ag
#autogram -t agast -l java -o core/be/uclouvain/jail/vm/autogram/v2 core/be/uclouvain/jail/vm/autogram/v2/Jail.ag


#autogram -t agpeg -l java -o dialect/be/uclouvain/jail/dialect/utils dialect/be/uclouvain/jail/dialect/utils/UTILS.ag
#autogram -t agast -l java -o core/be/uclouvain/jail/vm/autogram core/be/uclouvain/jail/vm/autogram/Jail.ag
#autogram -t agast -l java -o dialect/be/uclouvain/jail/dialect/dot dialect/be/uclouvain/jail/dialect/dot/DOT.ag
#autogram -t agast -l java -o dialect/be/uclouvain/jail/dialect/seqp dialect/be/uclouvain/jail/dialect/seqp/SEQP.ag
#autogram -t agast -l java -o dialect/be/uclouvain/jail/dialect/upv2 dialect/be/uclouvain/jail/dialect/upv2/UPV2.ag
autogram -t agast -l java -o algo/be/uclouvain/jail/algo/graph/copy/match algo/be/uclouvain/jail/algo/graph/copy/match/GMatch.ag
