package chatElements;
 
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {

	private static Socket socket;
	
	private static boolean finisshed=false;
	private static boolean connection =false;
	
	private  DataOutputStream output;
	private DataInputStream input;
	
	
	private String host;
	private int port;
	
	private static int key;
	private String name;
	private long id;
	private Scanner scanner;
	
	
	public ChatClient(String host, int connectionport,String name,Scanner scanner)  {
		this.host=host;
		this.port=(connectionport < 1024)? DefaultOptions.DEFAULT_PORT:connectionport;
		this.name= name; 
		this.scanner=scanner;
		try {
		socket=new Socket(this.host,port);
		this.input= new DataInputStream(socket.getInputStream());
		this.output=new DataOutputStream(socket.getOutputStream());
		}catch(IOException e) {
			e.printStackTrace();
			
		}
		
		

		
	}
	
	public void ShowSuscefullConnection(String assign) {
		String[] parts= assign.split(":");
		this.id=Long.parseLong(parts[1]);
		this.name= (name.equals(null)||name.equals(""))?""+id:name;
		MessagePrinter.ShowText("Bienvenido "+name);
		MessagePrinter.ShowText("Se ha conectado al servidor mediante el puerto: " + port);
		MessagePrinter.ShowText("Comandos");
		MessagePrinter.ShowText("si desea ver la lista de usuarios conectados escriba: "+DefaultOptions.CLIENTS_LIST);
		MessagePrinter.ShowText("Para chatear con otro usuario use "+DefaultOptions.CONNECT+":"+"id Usuario Destino");
		MessagePrinter.ShowText("Para salir use "+DefaultOptions.EXIT);
		
	}
	
	public static void main(String[] args) {
		Scanner scanner= new Scanner(System.in);
		MessagePrinter.ShowText("Ingresa Su IP [Localhost por defecta]");
		String ip= scanner.nextLine();
		if(ip.length()<=0) ip=DefaultOptions.DEFAULT_HOST;
		
		MessagePrinter.ShowText("Establesca el puerto de conexión [puesto 5050 por defecto] : ");
		String connectionPortStr= scanner.nextLine();
		int connectionPort=-1;
		
		MessagePrinter.ShowText("Establesca su nickname");
		String nickname= scanner.nextLine();
		
			if 	(connectionPortStr.length()>=4) {
				connectionPort=Integer.parseInt(connectionPortStr);
				
			}
			
			ChatClient client=new ChatClient(ip,connectionPort,nickname,scanner);
			client.LaunchInput();
			
			client.LaunchOutput();	
			
			
		
	}
	
	
	
	public void LaunchInput()  {
		Runnable inputThread= new ClientInputManager(this,input);
		new Thread(inputThread).start();
	}
	
	public void LaunchOutput() {
		Runnable outputThread =new ClientOutputManager(this,output,scanner);
		new Thread(outputThread).start();
		
	}
	
	
	public void ProccesMessage(String message) {
		if(message.contains(DefaultOptions.SERVER_MESSAGE)) {
			MessagePrinter.ShowText(message);
			}
		else {
			// cambiar por el tratamiento a los mensajes ajenos al servidor
			MessagePrinter.ShowText(message);
		}
	}
	
	public String getEncryptedMessage(String message) {
		message= id+": "+message;
		return message;
	}
	
	public boolean IsFinisshed() {
		return finisshed;
	}
	
	public void finish() {
		finisshed=true;
	}
	
	public boolean IsConnected() {
		return connection;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void SetConnected(int key) {
		this.key=key;
		connection=true;
	}
	
	public void CloseAll() throws IOException {
		scanner.close();
		socket.close();
		input.close();
		output.close();
	}
	
}
