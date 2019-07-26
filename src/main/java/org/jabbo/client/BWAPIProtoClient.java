package org.jabbo.client;

import bwapi.init.Init;
import bwapi.message.MessageOuterClass;
import org.apache.commons.math3.random.MersenneTwister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Deque;

public class BWAPIProtoClient {

    private SocketChannel tcpSocket; // blocking
    private Deque<MessageOuterClass.Message> messageQueue;
    private MersenneTwister mt;
    private int connectionPort;

    public BWAPIProtoClient() {
        this.mt = new MersenneTwister(System.nanoTime()); // Proper seed?
        this.connectionPort = 1025;
    }

    public void lookForServer(int apiVersion, String bwapiVersion, boolean tournament) throws IOException {
        if (isConnected()) return;
        System.out.println("Looking for Server, apiVersion: " + apiVersion + ", bwapiVersion: " + bwapiVersion + ", tournamentMode: " + tournament);
        // should be non blocking but its blocking, timeout should be set and handled
        DatagramSocket udpSocket = new DatagramSocket();
        udpSocket.setBroadcast(true);
        Init.ClientBroadcast broadcast = Init.ClientBroadcast.newBuilder()
                .setApiVersion(apiVersion)
                .setBwapiVersion(bwapiVersion)
                .setTournament(tournament).build();
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder().setInitBroadcast(broadcast).build();
        byte[] data = message.toByteArray();
        DatagramPacket packet
                = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 1024);
        System.out.println("Sending UDP InitBroadcast message");
        udpSocket.send(packet);
        byte[] buffer = new byte[message.getSerializedSize()];
        DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
        udpSocket.receive(resp);
        byte[] response = new byte[resp.getLength()];
        System.arraycopy(resp.getData(), 0, response, resp.getOffset(), resp.getLength());
        MessageOuterClass.Message currentMessage = MessageOuterClass.Message.parseFrom(response);
        if (!currentMessage.hasInitResponse()) {
            udpSocket.close();
            return;
        }
        System.out.println("Response from server\n" + currentMessage.toString());
        connectionPort = currentMessage.getInitResponse().getPort();
        System.out.println("Connecting to server using TCP socket and port " + connectionPort + "...");
        tcpSocket = SocketChannel
                .open(new InetSocketAddress("localhost", connectionPort));
        //tcpSocket.connect(new InetSocketAddress("localhost", connectionPort));
        if(!tcpSocket.isConnected()) System.out.println("Connection Failed");
        System.out.println("Connected to server");
        udpSocket.close();
    }


    public void transmitMessages(){
        if (!isConnected()) return;
    }

    public void receiveMessages() throws IOException {
        if (!isConnected()) return;
        while(true){
            ByteBuffer buf = ByteBuffer.allocate(1000);
            int numBytesRead = tcpSocket.read(buf);

            System.out.println(numBytesRead);
            if (numBytesRead <= 0) {
                System.out.println("Finished reading stream");
                return;
            }
            byte[] test = new byte[numBytesRead];
            for(int ii=0; ii < numBytesRead; ii++){
                test[ii] = buf.get(ii);
            }
            System.out.println(Arrays.toString(buf.array()));
            System.out.println(Arrays.toString(test));
            try{
                MessageOuterClass.Message received = MessageOuterClass.Message.parseFrom(test);
                System.out.println(received.toString());
            } catch(com.google.protobuf.InvalidProtocolBufferException ignored){
                System.out.println("Cant parse message to protobuf");
            }

        }
    }

    public void disconnect() throws IOException {
        if (!isConnected()) return;
        tcpSocket.close();
    }

    public void queueMessage(MessageOuterClass.Message newMessage){
        this.messageQueue.add(newMessage);
    }

    public MessageOuterClass.Message getNextMessage(){
        return this.messageQueue.pop();
    }

    public final boolean isConnected(){
        return this.tcpSocket != null && this.tcpSocket.isConnected();
    }

    public final int messageQueueSize(){
        return this.messageQueue.size();
    }

    // TODO test?
    private int getRandomInteger(int min, int max){
        return mt.nextInt(max - min + 1) + min;
    }

}
