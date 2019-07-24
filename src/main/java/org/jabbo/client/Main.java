package org.jabbo.client;

public class Main {


    public static void main(String[] args) {
        BWAPIProtoClient client = new BWAPIProtoClient();
        while (true)
        {
            System.out.println("Connecting...");
            client.lookForServer(0, "x", false);
            if (client.isConnected()) {
                System.out.println("Connected");
                return;
            }
            return; // Remove when finished
        }
    }
}
