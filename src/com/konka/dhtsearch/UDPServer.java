package com.konka.dhtsearch;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
	public static void main(String args[]) throws Exception {
		System.out.println("cgp-----start");
		DatagramSocket socket = new DatagramSocket(55555);
		byte buf[] = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		System.out.println("Received from:" + packet.getSocketAddress());
		System.out.println("Data is:" + new String(packet.getData(), 0, packet.getLength()));
		socket.close();
		System.out.println("cgp-----end");
	}
}
