package com.mca.chainreaction;

import java.io.*;
import java.net.*;

// Sender class
public class Sender {
	// State variabls
	static final int maxline = 100;

	// main function
	/** Creates a string to put into the message, and sends that message through the socket to the other person if it doesnt equal exit. Also accepts a message onto inputstream and prints it as long as it's of they ACK. **/

	public static void main(String[] args) {
		try {
			String myString = " ";
			String host = args[0];
			String whatType = " ";
			int port = Integer.parseInt(args[1]);
			Socket outSock = new Socket(host, port);
			OutputStream outStream = outSock.getOutputStream();
			System.out.println("Initializing for netxwork communication... ");
			while (!myString.equals("exit")) {
				System.out.print("Pick a type of message: ");
				byte[] outBuff2 = new byte[maxline];
				int count2;
				count2 = System.in.read(outBuff2);
				whatType = new String(outBuff2, 0, (count2-1));
				// System.out.print("Type set to" + whatType);
				/* assert:  socket and stream initialized */
				byte[] outBuff = new byte[maxline];
				System.out.println("Enter a line of input:");
				int count;  // to hold number of bytes read                                                         
				count = System.in.read(outBuff);
				myString = new String(outBuff, 0, (count-1));
				Message m;
				System.out.println(whatType);
				if(whatType.equals("GENERIC")) {
					System.out.print("entered the generic block");
					m = new Message("GENERIC",myString);
					//m.send(outStream);
				}
				else if(whatType.equals("getFile")) {
					System.out.print("enters the file block");
					m = new Message("getFile", myString);
					//m.send(outStream);
				}
				else {
					m = new Message("NACK", "...");
					//m.send(outStream);
				}
				if(myString.equals("exit")) {
					break;
				}
				InputStream inStream = outSock.getInputStream();
				Message m2 = new Message(inStream);
				if (m2.type.equals("ACK")) {
					System.out.println("Acknowledgement message received.");
				}
				else {
					System.out.println("No acknowledgement message received.");
				}
			}
			// System.out.println("Initializing for netxwork communication... ");
			outSock.close();
		}
		catch (IOException e) {
			System.err.println("Sender failed.");
			System.err.println(e.getMessage());
			System.exit(1);  // an error exit status                                                            
			return;
		}
	}
}