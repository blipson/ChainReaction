package com.mca.chainreaction;

import java.io.*;
import java.net.*;

public class Backend {

	// class variables

	/** an int for determining the size of the buff **/

	static final int maxinBuff = 1000;

	// main function

	/** gets the connection from the socket and accepts and prints a message from there **/

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			System.out.println("Initializing for network communication... ");
			ServerSocket servSock = new ServerSocket(port);
			while (true) {
				System.out.println("Waiting for an incoming connection... ");
				Socket inSock = servSock.accept();
				Worker w = new Worker(inSock);
				Thread t = new Thread(w);
				t.start();
			}
		}
		catch (IOException e) {
			System.err.println("Receiver failed.");
			System.err.println(e.getMessage());
			System.exit(1);  // an error exit status
			return;
		}
	}
}

// Worker class
// Stubs only

class Worker implements Runnable {
	//state variables
	Socket sock;
	//constructor, sets the value of sock
	public Worker(Socket s) {
		sock = s;
	}

	//run method
	/** creates an inputstream object to get the message, and prints that message while the contents are not exit **/

	public void run() {
		try {
			InputStream inStream = sock.getInputStream();
			OutputStream outStream = sock.getOutputStream();
			Message ack = new Message("ACK", "hullo");
			Message m = new Message(inStream);
			if(m.type.equals("FWRITE")) {
				//System.out.print(m.contents);
				File outFile = new File("com.mca.collision.scores.txt");
				FileOutputStream out = new FileOutputStream(outFile);
				out.write((m.contents+" ").getBytes());
				out.close();
			}
			/*else if(m.type.equals("FREAD")) {
				//System.out.print(m.contents);
				File inFile = new File("com.mca.collision.scores.txt");
				FileInputStream in = new FileInputStream(inFile);
				byte[] buff = new byte[4096];
				int bytesRead = 0;
				while ((bytesRead=in.read(buff))>-1) {
					// TODO: lots
				}
				in.read((m.contents+" ").getBytes());
			}*/
			else if(m.type.equals("NACK")) {
				System.out.println("error");
			}
			while(!m.contents.equals("exit")) {
				System.out.println("Message received: " + m);
				//ack.send(outStream);
				m = new Message(inStream);
			}
		}
		catch (IOException e) {
			System.err.println("Receiver failed.");
			System.err.println(e.getMessage());
			System.exit(1);
			return;
		}
	}
}