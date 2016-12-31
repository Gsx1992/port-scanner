package com.gsx.remoteport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class PortScannerRemote {

	public static void main(String[] args) {

		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(in);

		String targetIP = "";
		Port startPort = null;
		Port endPort = null;

		System.out.print("Enter IP address of target: ");

		try {
			targetIP = reader.readLine();
			
			while(!validIP(targetIP)) //checks if IP is valid
			{
				
				System.out.print("Enter IP address of target: ");
				targetIP = reader.readLine();
			}
			
			
		} catch (IOException e) {
			System.out.println("Unable to read the entered IP address "+ e.toString());
		}


		boolean validPort = false;

		while(!validPort){
			
			System.out.print("Please enter the beginning port: ");
			try {
				String port = reader.readLine();
				int portInt = Integer.parseInt(port);

				if(isValid(portInt)) //check port is within valid range
				{
					validPort = true;
					startPort = new Port(portInt);
				}

				else
				{
					invalidPort();
				}

			} catch (IOException e1) {
				System.out.println("Cannot read the entered input" +e1.toString());
			} catch (NumberFormatException e2) {
				System.out.println("Please enter a valid number!");
			}
		}

		validPort = false;

		while(!validPort){
			
			System.out.print("Please enter the ending port: ");
			try {
				String port = reader.readLine();
				int portInt = Integer.parseInt(port);

				if(isValid(portInt)) //check port is within valid range
				{
					if(portInt >= startPort.getPort()) //check that value entered(second port) is greater than or equal to first port
					{
						validPort = true;
						endPort = new Port(portInt);
					}

					else
					{
						System.out.println("The second port should be greater than, or the same as the first port!");
					}
				}

				else
				{
					invalidPort();
				}
			} catch (IOException e1) {
				System.out.println("Cannot read the entered input" +e1.toString());
			} catch (NumberFormatException e2) {
				System.out.println("Please enter a valid number!");
			}
		}

		int port = startPort.getPort(); //get the value of the first port to begin scanning

		while(port <= endPort.getPort()){
			try {

				Socket portSocket = new Socket(targetIP,port); //attempt to open socket at entered IP and current port value
				System.out.println(String.format("Port %d is open and listening!", port));
				portSocket.close();

			} catch(UnknownHostException e1){ //invalid host
			
				System.out.println("Unknown host exception " + e1.toString());
			} catch(IOException e2) { //port is not open
			
				System.out.println(String.format("Port %d is not open!", port)); // print what port is opened
			} catch(Exception e) {
				System.out.println(e.toString()); 
			}
			port++;  //increment port number and loop again
		}
	}

	public static boolean isValid(int port)
	{
		if(port >= 0 && port <= 65536) //checks if port is in valid range
		{
			return true;
		}

		return false;
	}

	public static void invalidPort()
	{
		System.out.println("Port entered is invalid, value must range from 0 - 65536"); // simple error message
	}
	
	public static boolean validIP(String IP)
	{
		
		Process process;
		BufferedReader inputStream;
		try {
			process = Runtime.getRuntime().exec("ping "+IP); //pings the entered IP address
			inputStream = new BufferedReader(new InputStreamReader(process.getInputStream())); //reads input from ping
			String inMessage = "";
			int replyNumber = 0;
			
			while((inMessage = inputStream.readLine()) != null )
			{
				replyNumber ++; //counts the number of replies from the server
				
				if (inMessage.contains("transmit failed")) //check to see if ping failed(should be improved!) 
				{
					System.out.println("IP address is unreachable");
					return false;	
				}
			}
			
			if(replyNumber < 2) //a second check, this is used for non-ip addresses such as letters and words
			{
				System.out.println("IP address is unreachable");
				return false;
			}
			
			System.out.println("IP address is reachable!");
			return (replyNumber > 1) ? true:false;
		} catch (IOException e) {
			
			System.out.println("Unable to read the entered IP address "+ e.toString());
		}
		
		return false;
	}

}
