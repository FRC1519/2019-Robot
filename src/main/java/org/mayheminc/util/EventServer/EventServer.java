package org.mayheminc.util.EventServer;

import java.util.Vector;

// import org.mayheminc.util.EventServer.*;

public class EventServer extends Thread {
    Vector<Event> EventList = new Vector<Event>();
    TCPServer tcpServer = new TCPServer();

    public void add(Event E) {
        EventList.add(E);
    }

    public void run() {
        tcpServer.start();
    }
}