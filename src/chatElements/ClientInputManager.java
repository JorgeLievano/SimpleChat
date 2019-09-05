package chatElements;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientInputManager implements Runnable {

	private ChatClient client;
	private DataInputStream input;
	
	public ClientInputManager(ChatClient client,DataInputStream input)  {
		this.client=client;
		this.input= input;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!client.IsFinisshed()) {
			
			String word="";
			try {
				word= input.readUTF();
				
			}catch(IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			if(!client.IsConnected() && word.contains(DefaultOptions.SERVER_ID_ASSIGNATION)) {
				client.ShowSuscefullConnection(word);
			}else if(word.contains(DefaultOptions.START_CHAT)){
				int key=Integer.parseInt(word.split(":")[1],16);
				
				client.SetConnected(key);
			}else {
			
				client.ProccesMessage(word);
			}
		}
		
		
		
		try {
			client.CloseAll();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

}
