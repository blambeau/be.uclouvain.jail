export CLASSPATH=./algo:./dialect:./core
autogram -t agpeg -l java -o dialect/be/uclouvain/jail/dialect/commons dialect/be/uclouvain/jail/dialect/commons/UTILS.ag
#autogram -t agast -l java -o dialect/be/uclouvain/jail/dialect/dot dialect/be/uclouvain/jail/dialect/dot/DOT.ag
#autogram -t agast -l java -o dialect/be/uclouvain/jail/dialect/seqp dialect/be/uclouvain/jail/dialect/seqp/SEQP.ag
autogram -t agast -l java -o core/be/uclouvain/jail/vm/autogram core/be/uclouvain/jail/vm/autogram/Jail.ag
#autogram -t agast -l java -o algo/be/uclouvain/jail/algo/graph/copy/match algo/be/uclouvain/jail/algo/graph/copy/match/GMatch.ag
