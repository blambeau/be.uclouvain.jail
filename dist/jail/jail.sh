export AWT_TOOLKIT=MToolkit
JAIL_HOME=/home/blambeau/Work/devel/java/be.uclouvain.jail/dist/jail
export CLASSPATH=.:$JAIL_HOME/lib
for i in $JAIL_HOME/lib/*.jar 
do
    export CLASSPATH=$CLASSPATH:$i
done
java -Xmx512m -Xms512m -ea be.uclouvain.jail.Jail $*					 
