import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;



public class ClientHandler extends Thread{

	private MazeWarServer _server;
	private boolean _initialized;
	private  Socket _clientSocket;
	private  Vector _otherClientHandlers;
	private Queue _messageQueue;
	private BufferedReader _in;
	private RequestReader _reader;
	private RequestWriter _writer;
	private BufferedWriter _socketWriter;
	private static RequestFactory requestFactory = new RequestFactory(); 
	
	
	public ClientHandler(MazeWarServer server, Socket socket) throws SocketException{
		_clientSocket = socket;
		_clientSocket.setKeepAlive(true);
		_otherClientHandlers = new Vector();
		_messageQueue = new Queue();
		_initialized = false;
	}
	
	
	
	public void enqueueRequest(BaseRequest request){
		_messageQueue.enQueue(request);
	}
	
	public Object dequeueRequest() throws InterruptedException{
		return _messageQueue.deQueue();
	}
		
	private void notifySiblingsOfRequest(BaseRequest request){
		ClientHandler siblings[];
		synchronized(_otherClientHandlers){
			siblings = new ClientHandler[_otherClientHandlers.size()];
			_otherClientHandlers.copyInto(siblings);
		}
		System.out.println("notifying siblings "+siblings.length +"of request:"+requestFactory.marshallRequest(request));
		//_otherClientHandlers.copyInto(anArray)
		for(int i = 0; i < siblings.length; i++ ){
			siblings[i].enqueueRequest(request);
		}
	}
		
	public void addNewClientHandler(ClientHandler handler){
		synchronized(_otherClientHandlers){
			_otherClientHandlers.add(handler);
		}
	}
	
	public void init() throws IOException{
		_in = new BufferedReader(
                new InputStreamReader(
                _clientSocket.getInputStream()));
		
		PrintWriter out = new PrintWriter(_clientSocket.getOutputStream(), true);
		_socketWriter = new BufferedWriter(out);
		
		String registerMessage = readRequest();
		//String registerMessage2 = in.readLine();
		RegisterRequest request = (RegisterRequest) 
				requestFactory.unMarshallRequest(registerMessage);
		
		
		//start request reader that will read incoming requests from client
		_reader = new RequestReader();
		_reader.start();
		
		//start writer which will receive requests from other clients and write to the queue
		_writer = new RequestWriter();
		_writer.start();
		
	}

	public String readRequest() throws IOException{
		String request = _in.readLine();
		return request;
	}
	
	public void  writeRequest(String request) throws IOException{
		_socketWriter.write(request);
		_socketWriter.newLine();
		_socketWriter.flush();
	}
	
	
	public void run(){
		if(!_initialized){
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * Writes request received from other clients to the client of this client handler
	 *
	 */
	private class RequestWriter extends Thread{
		public void run(){
			int i = 0;
			while(1==1){
				BaseRequest request;
				try {
					request = (BaseRequest) ClientHandler.this.dequeueRequest();
					String marshalledRequest = requestFactory.marshallRequest(request);
					System.out.println("writing request:"+marshalledRequest);
					writeRequest(marshalledRequest);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//handleClientRequest(requestModel);
			}
			
		}
	}
	
	/**
	 * Reads incoming request from client, unmarshalls it and handles it
	 *
	 */
	private class RequestReader extends Thread{
		
		public void run(){
			try {
				while(1==1){
					String request = ClientHandler.this.readRequest();
					System.out.println("reading Request:"+request);
					BaseRequest requestModel = requestFactory.unMarshallRequest(request);
					notifySiblingsOfRequest(requestModel);
				}	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
