package org.jabbo.client;

import bwapi.init.Init;
import bwapi.message.MessageOuterClass;
import org.apache.commons.math3.random.MersenneTwister;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Deque;

public class BWAPIProtoClient {

    public BWAPIProtoClient(){
        this.mt = new MersenneTwister(System.nanoTime()); // Proper seed?
        this.connected = false;
        this.connectionPort = 1025;
        this.udpbound = false;
    }

    public void lookForServer(int apiVersion, String bwapiVersion, boolean tournament) throws IOException {
        if (isConnected()) return;
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        Init.ClientBroadcast broadcast = Init.ClientBroadcast.newBuilder()
                .setApiVersion(apiVersion)
                .setBwapiVersion(bwapiVersion)
                .setTournament(tournament).build();
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder().setInitBroadcast(broadcast).build();
        //MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder().setInitBroadcast(Init.ClientBroadcast.getDefaultInstance()).build();
        byte[] data = message.toByteArray();
        DatagramPacket packet
                = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 1024);
        socket.send(packet);
        //byte[] buffer = new byte[1024];
        //DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
        //socket.receive(resp);
        //System.out.println(Arrays.toString(resp.getData()));
        socket.close();
        // TODO finish

    }

    public void transmitMessages(){

    }

    public void receiveMessages(){

    }

    public void disconnect(){

    }

    public void initListen(){

    }

    public void stopListen(){

    }

    public void queueMessage(MessageOuterClass.Message newMessage){

    }

    public MessageOuterClass.Message getNextMessage(){
        return null;
    }

    public final boolean isConnected(){
        return false;
    }

    public final int messageQueueSize(){
        return 0;
    }

    private SocketChannel udpSocket; // non blocking
    private Socket tcpSocket; // blocking
    private Deque<MessageOuterClass.Message> messageQueue;
    private MersenneTwister mt;
    private boolean connected;
    private boolean udpbound;
    private short connectionPort;

    // TODO test?
    private int getRandomInteger(int min, int max){
        return mt.nextInt(max - min + 1) + min;
    }

}
