package com.solvd.laba.football.persistence.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(MySQLConnectionPool.class.getName());
    private static volatile MySQLConnectionPool instance;
    private static final int POOL_SIZE = 5;
    private static final int SLEEP_BETWEEN_CHECKS_MILISECONDS = 200;

    private volatile List<Connection> connections;
    private volatile List<Connection> usedConnections;


    private MySQLConnectionPool(int poolSize) {
        // create pool and fill it with uninitialized objects
        this.connections = new CopyOnWriteArrayList<>(Collections.nCopies(poolSize, null));
        this.usedConnections = new CopyOnWriteArrayList<>();
    }

    public static synchronized MySQLConnectionPool getInstance() {
        if (MySQLConnectionPool.instance == null) {
            LOGGER.info("Creating instance of ConnectionPool");
            MySQLConnectionPool.instance = new MySQLConnectionPool(POOL_SIZE);
        }
        return MySQLConnectionPool.instance;
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
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/exercise_football",
                        "root", "example");
                this.connections.set(i, connection);
                break;
            }
        }

        this.usedConnections.add(connection);
        setConnectionDefaults(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        this.usedConnections.remove(connection);
    }

    protected synchronized boolean connectionsAvailable() {
        return this.usedConnections.size() < this.connections.size();
    }

    protected void setConnectionDefaults(Connection connection) throws SQLException {
        connection.setReadOnly(false);
        connection.setAutoCommit(true);
    }
}
