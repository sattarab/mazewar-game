
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
 
public class MazeWarServer {
	
	private Vector _clientHandlers;
	
	public MazeWarServer(){
		_clientHandlers = new Vector();
	}
	
    public static void main(String[] args) {
    	MazeWarServer server = new MazeWarServer();
        ServerSocket serverSocket = null;
        boolean listening = true;
 
        try {
            serverSocket = new ServerSocket(4442);
            while (listening){
            	Socket socket = serverSocket.accept();
            	ClientHandler clientHandler = new ClientHandler(server,socket);
            	server.registerClient(clientHandler);
            	clientHandler.start();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4441");
            System.exit(-1);
        }
    }
    
    //register client handlers with eachother
    private void registerClient(ClientHandler newHandler){
    	for(int i = 0; i < _clientHandlers.size(); i++){
    		ClientHandler handler = (ClientHandler) _clientHandlers.get(i);
    		handler.addNewClientHandler(newHandler);
    		newHandler.addNewClientHandler(handler);
    	}
    	_clientHandlers.add(newHandler);
    }
    
    
}
