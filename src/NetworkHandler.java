

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;


public class NetworkHandler implements ClientListener, MazeListener{
	
	String _clientName;
	private HashMap<String, RemoteClient> _map;
	private ServerStub _stub;
	private Queue _clientEventQueue;
	private Maze _maze;
	private MessageReader _messageReader;
	private MessageWriter _messageWriter;
	private static final RequestFactory requestFactory = new RequestFactory();
	
	public NetworkHandler(String clientName, int posx, int posy, int rotpos, Maze maze) 
			throws UnknownHostException, IOException{
		_clientName = clientName;
		_map = new HashMap<String, RemoteClient>();
		_stub = new ServerStub(clientName);
		//register with the server
		_stub.registerSelf(posx, posy, rotpos);
		
		//initialize clientEvent queue;
		_clientEventQueue = new Queue();
		//start message reader to read messages from queue
		_messageReader = new MessageReader();
		_messageReader.start();
		
		//start messageWriter to start writing messages
		_messageWriter = new MessageWriter();
		_messageWriter.start();
		
		_maze = maze;
		
		_maze.addMazeListener(this);
	}
	
	public void dequeueClientEvent(){
		//TODO: dequeue client event, decode request and send it to network
	}
	
	private class MessageWriter extends Thread{
		public void run(){
			//read incoming requests from client event
			while(1==1){
				try {
					ClientEventEx event = (ClientEventEx) _clientEventQueue.deQueue();
					//form an action request based on client event
					ActionRequest request = 
							new ActionRequest(_clientName,event.posX, 
									event.posY, event.orientation.direction,
									event.event.event);
					String requestString = requestFactory.marshallRequest(request);
					_stub.writeToSocket(requestString);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class MessageReader extends Thread{
		public void run(){
			try {
				while(1==1){
					//read message and decode it in a loop
					String message = _stub.readMessage();
					BaseRequest request = requestFactory.unMarshallRequest(message);
					decodeMessage(request);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void decodeMessage(BaseRequest request){
		String clientName = request.getClientName();
		ActionRequest actionRequest = (ActionRequest) request;
		if(!_map.containsKey(clientName)){
			
			RemoteClient client = new RemoteClient(clientName);
			_map.put(clientName,client);
			_maze.addClient(client, new Point(request.getPosX(),request.getPosY()), 
					new Direction(actionRequest.getRotPos()));
		}
		
		else{
			RemoteClient client = _map.get(clientName);
			int action = actionRequest.getAction();
			if(action == ClientEvent.MOVE_FORWARD){
				client.moveForwardEx();
			}
			else if(action == ClientEvent.MOVE_BACKWARD){
				client.moveBackwardEx();
			}
			else if(action == ClientEvent.TURN_RIGHT){
				client.turnRightEx();
			}if(action == ClientEvent.TURN_LEFT){
				client.turnLeftEx();
			}if(action == ClientEvent.FIRE){
				client.fireEx();
			}
		}
		
	}

	public void clientUpdate(Client client, ClientEvent clientevent) {
		_clientEventQueue.enQueue(new ClientEventEx(clientevent, client.getPoint().getX(), 
				client.getPoint().getY(), client.getOrientation()));	
	}
	
	
	private class ClientEventEx{
		public int posX;
		public int posY;
		public Direction orientation;
		public ClientEvent event;
		public ClientEventEx(ClientEvent event, int posX, int posY, Direction orientation){
			this.event = event;
			this.posX = posX;
			this.posY = posY;
			this.orientation = orientation;
		}
	}

	@Override
	public void mazeUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientKilled(Client source, Client target) {
		if(target.getName().equals(_clientName)){
			Client client = (Client)_map.get(target.getName());
			if(client != null){
				_maze.removeClient(client);
				client.unregisterMaze();
			}
		}
		
	}

	@Override
	public void clientAdded(Client client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientFired(Client client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientRemoved(Client client) {
		// TODO Auto-generated method stub
		
	}

}
