package org.jabbo.client;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        BWAPIProtoClient client = new BWAPIProtoClient();
        while (true)
        {
            System.out.println("Connecting...");
            client.lookForServer(0, "5.0", false);
            if (client.isConnected()) {
                System.out.println("Connected");
                return;
            }
            return; // Remove when finished
        }
    }
}
