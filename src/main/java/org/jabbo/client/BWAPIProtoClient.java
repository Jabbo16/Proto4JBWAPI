package org.jabbo.client;

import bwapi.message.MessageOuterClass;
import org.apache.commons.math3.random.MersenneTwister;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Deque;

public class BWAPIProtoClient {

    public BWAPIProtoClient(){
        this.mt = new MersenneTwister(System.nanoTime()); // Proper seed?
        this.connected = false;
        this.connectionPort = 1025;
        this.udpbound = false;
    }

    public void checkForConnection(int apiVersion, String engineType, String engineVersion) throws IOException {
        if (isConnected()) return;
        String sender = "0.0.0.0";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        if (!udpbound){
            udpSocket.bind(new InetSocketAddress(sender, 1024));
            udpbound = true;
        }
        int port = udpSocket.socket().getLocalPort();
        int bytesRead = udpSocket.read(byteBuffer);
        MessageOuterClass.Message currentMessage = MessageOuterClass.Message.parseFrom(byteBuffer);
        System.out.println(currentMessage.toString());

        if (!currentMessage.hasInitBroadcast()) return;
        // TODO finish
    }

    public void lookForServer(int apiVersion, String bwapiVersion, boolean tournament){
        if (isConnected()) return;
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
