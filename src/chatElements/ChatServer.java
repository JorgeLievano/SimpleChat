package chatElements;

import java.net.*;
import java.io.*;
import java.util.*;


public class ChatServer {

	
	
	
	private ServerSocket serverSocket;
	
	private static HashMap<Long, Socket> clientsSockets = new HashMap<Long, Socket>();
	private static HashMap<Long, Long> conections = new HashMap<Long, Long>();
	private static HashMap<Long, DataInputStream> clientsInputs = new HashMap<Long, DataInputStream>();
	private static HashMap<Long, DataOutputStream> clientsOutputs= new HashMap<Long, DataOutputStream>();
	
	
	public ChatServer(int port) throws IOException {
		int connectionPort= (port < 1024)? DefaultOptions.DEFAULT_PORT:port;
		
		this.serverSocket= new ServerSocket(connectionPort);
		
	}
	
	public void LaunchConnection() {
		Runnable connection= new ServerConnectionManager(this);
		new Thread(connection).start();
	}
	
	public void PutClient(long id) throws IOException {
		clientsSockets.put(id, serverSocket.accept());
	}
	
	public Socket getSocketClient(long clientId) {
		return clientsSockets.get(clientId);
		
	}
	
	public void PutInputClient(long id, DataInputStream input) {
		clientsInputs.put(id, input);
	}
	
	public void PutOutputClient(long id, DataOutputStream output) {
		clientsOutputs.put(id, output);
	}
	
	public DataOutputStream GetClientOutput(long id) {
		return clientsOutputs.get(id);
	}
	
	public Set<Map.Entry<Long, Socket>> getClientsEntrySet() {
		return clientsSockets.entrySet();
	}
	
	public ArrayList<String> getClientsListStr(long idRequester) throws IOException {
		ArrayList<String> list= new ArrayList<String>();
		for(Map.Entry<Long,Socket> client : clientsSockets.entrySet()) {
			long idClient = client.getKey();
			if(idClient !=idRequester) list.add(""+idClient);
		}
		
		return list;
	}
	
	public void NewChat(long sourceClient, long destinationClient) throws IOException {
		DataOutputStream sourceOut=clientsOutputs.get(sourceClient);
		DataOutputStream destinationOut;
		if(clientsSockets.containsKey(destinationClient)) {
			destinationOut=clientsOutputs.get(destinationClient);
			
			//Asignar llave
			
			String keyHexa=KeyHexa();
			
			conections.put(sourceClient, destinationClient);
			conections.put(destinationClient, sourceClient);
			
			destinationOut.writeUTF(DefaultOptions.START_CHAT+":"+keyHexa);
			destinationOut.writeUTF(DefaultOptions.SERVER_MESSAGE+"El usurio "+sourceClient+" ha iniciado un chat contigo");
			sourceOut.writeUTF(DefaultOptions.START_CHAT+":"+keyHexa);
			sourceOut.writeUTF(DefaultOptions.SERVER_MESSAGE+"Has iniciado un chat con "+destinationClient);
		}else {
			sourceOut.writeUTF(DefaultOptions.SERVER_MESSAGE+"El usuario no existe");
		}
		
	}
	
	
	public long GetPair(long idSourceClient) {
		
		return(conections.containsKey(idSourceClient))?conections.get(idSourceClient):-1;
		
	}
	
	public void EnviarMensaje(long idDestinationClient,String str) throws IOException {
		clientsOutputs.get(idDestinationClient).writeUTF(str);
	}
	
	public static void main(String[] args) {
		MessagePrinter.ShowText("Establesca el puerto de conexión [puesto 5050 por defecto] : ");
		Scanner scanner=new Scanner(System.in);
		String connectionPortStr= scanner.nextLine();
		int connectionPort=-1;
		scanner.close();
		
		try {
			if 	(connectionPortStr.length()>=4) {
				connectionPort=Integer.parseInt(connectionPortStr);
			}
		
			ChatServer server= new ChatServer(connectionPort);
			server.LaunchConnection();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String KeyHexa() {
		int decimal=(int)(Math.random()*20)+1;
		String keyHexa= Integer.toHexString(decimal);
		return keyHexa;
		
	}
	
	
}
