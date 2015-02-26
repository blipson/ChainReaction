package com.mca.chainreaction;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Message {

	// class variables

	/** A string for delimiting the parts of a Message when represented as a byte I.E. "\001" **/

	static String delimiter = "\001";
	static final int maxBuff = 1000;

	// state variables

	/** Two Strings representing the type of message and the content of the message
                I.E. "greeting/001", "hello/001" **/
	String type;
	String contents;

	// Constructors

	/** Message(String, String) itnitializes the two state variables to the two
		argumentse type of message and the content of the message
                I.E. type = "greeting", contents = "hello" **/

	//Message(String, String) itnitializes the two state variables

	public Message(String type_of, String contents_of) {
		type = type_of;
		contents = contents_of;
	}


	/** Message(InputStream) reads a message represented as a byte array and initilizes the state variables from elements of that array. Uses delimiter to determine where the values end **/

	public Message(InputStream i) {
		byte[] buffer = new byte[maxBuff];
		int counter = -5;
		try {
			counter = i.read(buffer);
		} catch (IOException e) {System.out.print("IOException caught in read of buffer"); }
		String newMessage = new String(buffer, 0 , counter);
		StringTokenizer st = new StringTokenizer(newMessage, delimiter);
		type = st.nextToken();
		contents = st.nextToken();
	}

	// Methods

	/** send(OutputStream) writes a byte array onto the OutputStream, no return value**/

	public void send(String host, int port) {
		try {
			Socket outSock = new Socket(host, port);
			OutputStream outStream = outSock.getOutputStream();
			outStream.write(getBytes());
		} catch (IOException e) {System.out.print("IOException caught!");}
	}

	/** toString() makes a printable string from your state variables, returns a String**/

	public String toString() {
		String s = "type = " + type + ", content = " + contents;
		return s;
	}

	//getBytes() returns a byte[] from the state variables
	public byte[] getBytes() {
		String full = type + delimiter + contents + delimiter;
		return full.getBytes();
	}
}