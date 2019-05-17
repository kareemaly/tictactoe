FROM openjdk

RUN mkdir -p /tictactoe
WORKDIR /tictactoe

ADD . /tictactoe

RUN mkdir -p target/classes
RUN javac -Xlint:deprecation src/main/java/com/bitriddler/tictactoe/Main.java -cp src/main/java/ -d target/classes

CMD java -cp target/classes/ com.bitriddler.tictactoe.Main game-config.txt 9999
