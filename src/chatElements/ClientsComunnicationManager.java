package chatElements;

import java.util.*;
import java.io.*;

public class ClientsComunnicationManager implements Runnable{

	private ChatServer server;
	
	
	private long idSourceClient;
	private long idDestinationClient;
	
	private boolean exit=false;
	
	private DataInputStream input;
	private DataOutputStream output;
	
	public ClientsComunnicationManager(ChatServer server, long idSourceClient,DataInputStream input,DataOutputStream output) throws IOException {
		this.server=server;
		this.idSourceClient=idSourceClient;
		this.idDestinationClient=-1;
		this.input= input;
		this.output=output;
		
		
	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(!exit) {
			try {
				String str =input.readUTF();
				
				if(idDestinationClient==-1){
					idDestinationClient=server.GetPair(idSourceClient);
				}
				if(str.equals(DefaultOptions.CLIENTS_LIST)) {
					output.writeUTF(DefaultOptions.SERVER_MESSAGE+">> Lista de Clientes");
					ArrayList<String> list =server.getClientsListStr(idSourceClient);
					for(String client : list) {
						output.writeUTF(DefaultOptions.SERVER_MESSAGE+client);
					}
					
				}else if(str.contains(DefaultOptions.CONNECT)) {
					long destinationclient=Long.parseLong(str.split(":")[1]);
					server.NewChat(idSourceClient, destinationclient);
					idDestinationClient=destinationclient;
				}else {
					
					if(idDestinationClient!=-1) {
						//str=idSourceClient+ " : "+str;
						server.EnviarMensaje(idDestinationClient, str);
					}else {
						output.writeUTF(">>No se encuentra conectado con ningun usuario");
					}
					
				}
				
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
