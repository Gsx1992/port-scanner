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

		String targetIp = "";
		Port startPort = null;
		Port endPort = null;

		System.out.print("Enter IP address of target: ");

		try {
			targetIp = reader.readLine();
		} catch (IOException e) {
			System.out.println("Unable to read the entered IP address "+ e.toString());
		}

		boolean validPort = false;

		while(!validPort)
		{
			System.out.print("Please the beginning port: ");
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

		while(!validPort)
		{
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

		while(port <= endPort.getPort()) 
		{
			try {

				Socket portSocket = new Socket(targetIp,port); //attempt to open socket at entered IP and current port value
				System.out.println(String.format("Port %d is open and listening!", port));
				portSocket.close();

			} catch(UnknownHostException e1) //invalid host
			{
				System.out.println("Unknown host exception " + e1.toString());
			}
			catch(IOException e2) //port is not open
			{
				System.out.println(String.format("Port %d is not open!", port)); // print what port is opened
			}
			catch(Exception e) 
			{
				System.out.println(e.toString()); 
			}
			port++;  //increment port number and loop again
		}



	}

	public static boolean isValid(int port)
	{
		if(port >= 0 && port <= 65536)
		{
			return true;
		}

		return false;
	}

	public static void invalidPort()
	{
		System.out.println("Port entered is invalid, value must range from 0 - 65536");
	}

}
