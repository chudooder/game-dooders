package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	
	Socket serverSocket;
	OutputStream out;
	InputStream in;
	public volatile ArrayList<byte[]> messages;
	
	public Client() {
		messages = new ArrayList<>();
		try {
			System.out.println("trying to connect");
			serverSocket = new Socket(
					java.net.InetAddress.getLocalHost().getHostName(), 
					5678);
			System.out.println("success");
			in = serverSocket.getInputStream();
			out = serverSocket.getOutputStream();
			
			Thread serverIn = new Thread() {
				public void run() {
					try {
						byte[] line = new byte[16];
						while(in.read(line) != -1) {
							processInput(line);
						}
					
						in.close();
						out.close();
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			serverIn.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processInput(byte[] line) {
		// Place messages in a backlog until the game retrieves them
//		System.out.print("CLIENT RECIEVE: ");
//		for(byte c : line) {
//			System.out.print(c + " ");
//		}
//		System.out.println();
		if(line.length > 0)
			messages.add(line);
	}
	
	public ArrayList<byte[]> getMessages() {
		return messages;
	}
	
	public void sendMessage(byte[] message) {
		try {
			out.write(message);
		} catch (IOException e) {
			System.err.println("CLIENT Unable to send message!");
		}
	}

}
