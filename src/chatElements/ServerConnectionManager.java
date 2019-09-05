package chatElements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;



public class ServerConnectionManager implements Runnable{

	 private ChatServer server;
	
	
	
	public ServerConnectionManager(ChatServer server) {
		// TODO Auto-generated constructor stub
		this.server=server;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			
			try {
				long id = System.nanoTime();
				server.PutClient(id);
				Socket temp = server.getSocketClient(id);
				DataInputStream in =  new DataInputStream(temp.getInputStream());
				DataOutputStream out = new DataOutputStream(temp.getOutputStream());
				server.PutInputClient(id, in);
				server.PutOutputClient(id, out);
				
				//Meter esto en un metodo de server
				System.out.println(">>El usuario con id " + id + " se ha conectado");
				System.out.println(temp.getLocalPort());
				System.out.println(temp.getPort());

				server.GetClientOutput(id).writeUTF(DefaultOptions.SERVER_ID_ASSIGNATION+":"+id);
				for (Map.Entry<Long, Socket> client : server.getClientsEntrySet()) {
					if (client.getKey() != id) {
						server.GetClientOutput(client.getKey()).writeUTF(">>El usuario con id: " + id + " ha ingresado al servidor");
					}
				}
				
				
				Runnable comunicacion = new ClientsComunnicationManager(server, id, in, out);
				new Thread(comunicacion).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
		}
	}

}
