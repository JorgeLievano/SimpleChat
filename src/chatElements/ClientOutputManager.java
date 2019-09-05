package chatElements;

import java.io.*;
import java.util.Scanner;


public class ClientOutputManager implements Runnable {

	private ChatClient client;
	private DataOutputStream output;
	
	private Scanner scanner;
	
	public   ClientOutputManager(ChatClient client,DataOutputStream out,Scanner scan)  {
		// TODO Auto-generated constructor stub
		this.client=client;
		this.output= out;
		scanner=scan;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(!client.IsFinisshed()) {
			
			String word=scanner.nextLine();
			
			if(word.equals(DefaultOptions.EXIT)) {
				client.finish();
			}else if(client.IsConnected() && !word.equals(DefaultOptions.CLIENTS_LIST) && !word.contains(DefaultOptions.CONNECT)) {
				word=client.getEncryptedMessage(word);
			}
			
				
			try {
				output.writeUTF(word);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		
	}
}
