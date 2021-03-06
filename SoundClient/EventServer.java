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

        while (true) {
            Thread.sleep(100); // sleep 100ms.

            // loop through the events...
            for (Event e : EventList) {
                // execute the event
                String str = e.Execute();

                // if the event did anything...
                if (str != "") {
                    // send it to the TCP Server
                    tcpServer.add(str);
                }
            }
        }
    }
}