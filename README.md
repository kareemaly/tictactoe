TicTacToe
------
Multi-player tictactoe game with an AI player.

## Prerequisite
- Install Docker [https://docs.docker.com/install/](https://docs.docker.com/install/)

## Game Configurations
You need to copy and paste `game-config.example.txt` to `game-config.txt`.

## Building image
While on the project root folder, run:
```
docker build -t tictactoe:latest .
```

## Running The image
```
docker run --rm -it -p 127.0.0.1:8888:9999 tictactoe:latest
```

Change port `8888` to whichever port you like.

Now the game server has started and players can start connecting to play against each
other.

## Connecting To The Game - For Players
Using Netcat linux command
```
nc 127.0.0.1 8888
```
