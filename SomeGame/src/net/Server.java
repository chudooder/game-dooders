package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * The one game to rule them all. Not really.
 * When the host starts a game, it automatically
 * starts a server linked to the host's copy of
 * the game. All commands from all clients,
 * even from the host, is run through this
 * Server.
 * 
 * All clients (including the host) then receive
 * the inputs from the server, and replicate it 
 * on their copy of the game. This ensures that
 * all versions of the game are in the same state.
 * 
 * During a game round, the server will send out two
 * types of messages:
 * - Input messages Inputs the most recent mercs.
 * 			Once every frame.
 * - Update messages: Absolute positions for the mercs.
 * 			This is in case some packets get dropped, and
 * 			prevents desync. Once per 7 frames.
 * - End-of-round update: At the end of a round, the server
 * 			sends out the complete input record for the 
 * 			previous round. If any frames were dropped, this
 * 			should resolve desync issues.
 * 
 * To summarize:
 * - One Server, running on host computer
 * - Two Clients, running on host and remote,
 * 		connected to the server.
 * - Two TimeLapses, running on host and remote,
 * 		linked to their respective clients 
 */
public class Server {
	ServerSocket serverSocket;
	Socket connectSocket;
	OutputStream out;
	InputStream in;
	static final char INPUT = 0;
	static final char PRINT = 1;
	
	
	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("SERVER: WAITING FOR CONNECTION");
			connectSocket = serverSocket.accept();
			
			in = connectSocket.getInputStream();
			out = connectSocket.getOutputStream();
			
			
			Thread clientIn = new Thread() {
				public void run() {
					try {
						System.out.println("SERVER: START");
						byte[] line = new byte[16];
						while(in.read(line) != -1) {
							processInput(line);
						}
						
						System.out.println("SERVER: EXIT");
					
						in.close();
						out.close();
						connectSocket.close();
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
			clientIn.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(byte[] line) {
//		System.out.print("SERVER SEND: ");
//		for(byte c : line) {
//			System.out.print((int)c + " ");
//		}
//		System.out.println();
		try {
			out.write(line);
		} catch (IOException e) {
			System.err.println("SERVER Unable to send message!");
		}
	}
	
	public void processInput(byte[] line) {
		if(line.length > 0) {
			int player = line[0];
			switch(line[1]) {
			case INPUT :
				sendMessage(line);
				break;
			case PRINT :
				System.out.println(String.valueOf(line));
				break;
				
			}
		}
	}
}
