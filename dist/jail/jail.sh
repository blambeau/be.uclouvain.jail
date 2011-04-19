export AWT_TOOLKIT=MToolkit
<<<<<<< HEAD:dist/jail/jail.sh
JAIL_HOME=/home/blambeau/work/development/java/be.uclouvain.jail/dist/jail
=======
JAIL_HOME=`pwd` 
>>>>>>> 1abfcb6... Make jail.sh independent of absolute paths:dist/jail/jail.sh
export CLASSPATH=.:$JAIL_HOME/lib
for i in $JAIL_HOME/lib/*.jar 
do
    export CLASSPATH=$CLASSPATH:$i
done
java -Xmx512m -Xms512m -ea be.uclouvain.jail.Jail $*					 
