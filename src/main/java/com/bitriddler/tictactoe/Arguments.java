package com.bitriddler.tictactoe;

class Arguments {
    private int port;
    private String configurationFilename;

    Arguments(String configurationFilename, int port) {
        this.port = port;
        this.configurationFilename = configurationFilename;
    }

    int getPort() {
        return port;
    }

    String getConfigurationFilename() {
        return configurationFilename;
    }

    static Arguments build(String args[]) throws InvalidArgumentsException {
        if (args.length != 2) {
            throw new InvalidArgumentsException("You must pass configuration file and port in arguments");
        }

        String configurationFilename = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            throw new InvalidArgumentsException("Provided port is incorrect");
        }

        if (port < 1024 || port > 65535) {
            throw new InvalidArgumentsException("Port must be between 1024 and 65535");
        }

        return new Arguments(configurationFilename, port);
    }
}
