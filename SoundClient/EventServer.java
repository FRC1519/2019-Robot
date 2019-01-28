import java.io.*;
import java.net.*;
import java.util.Vector;

import org.w3c.dom.events.Event;

class EventServer extends Thread {
    Vector<Event> EventList = new Vector<Event>();
    TCPServer tcpServer = new TCPServer();

    public void add(Event E) {
        EventList.add(E);
    }

    public void run() {
        tcpServer.start();
    }
}