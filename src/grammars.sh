export CLASSPATH=./dialect:./core
#autogram -t agpeg -l java -o be/uclouvain/jail/io/commons be/uclouvain/jail/io/commons/UTILS.ag
#autogram -t agast -l java -o be/uclouvain/jail/io/dot be/uclouvain/jail/io/dot/DOT.ag
autogram -t agast -l java -o core/be/uclouvain/jail/vm/autogram core/be/uclouvain/jail/vm/autogram/Jail.ag
