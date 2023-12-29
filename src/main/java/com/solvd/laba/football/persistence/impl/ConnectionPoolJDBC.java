package com.solvd.laba.football.persistence.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionPoolJDBC {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPoolJDBC.class.getName());
    private static volatile ConnectionPoolJDBC instance;
    private static final int POOL_SIZE = 5;
    private static final int SLEEP_BETWEEN_CHECKS_MILISECONDS = 200;

    private volatile List<Connection> connections;
    private volatile List<Connection> usedConnections;


    private ConnectionPoolJDBC(int poolSize) {
        // create pool and fill it with uninitialized objects
        this.connections = new CopyOnWriteArrayList<>(Collections.nCopies(poolSize, null));
        this.usedConnections = new CopyOnWriteArrayList<>();
    }

    public static synchronized ConnectionPoolJDBC getInstance() {
        if (ConnectionPoolJDBC.instance == null) {
            LOGGER.info("Creating instance of ConnectionPool");
            ConnectionPoolJDBC.instance = new ConnectionPoolJDBC(POOL_SIZE);
        }
        return ConnectionPoolJDBC.instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        // wait for avaliable connection
        while (!connectionsAvailable()) {
            try {
                Thread.sleep(SLEEP_BETWEEN_CHECKS_MILISECONDS);
            } catch (InterruptedException e) {
                // not important here
            }
        }

        // try to find opened connection
        Connection connection = null;
        for (Connection c : this.connections) {
            if (c == null || this.usedConnections.contains(c)) {
                continue;
            }
            connection = c;
            break;
        }

        // if opened connection not found, open one
        if (connection == null) {
            for (int i = 0; i < this.connections.size(); i++) {
                Connection c = this.connections.get(i);
                if (c != null && this.usedConnections.contains(c)) {
                    continue;
                }
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exercise_football", "root", "example");
                this.connections.set(i, connection);
                break;
            }
        }

        this.usedConnections.add(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        this.usedConnections.remove(connection);
    }

    protected synchronized boolean connectionsAvailable() {
        return this.usedConnections.size() < this.connections.size();
    }
}
