


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ServerStub {

	private Socket _socket;
	private String _clientName;
	private BufferedWriter _writer;
	private BufferedReader _in;
	private static final RequestFactory requestFactory = new RequestFactory();
	
	public ServerStub(String clientName) throws UnknownHostException, IOException{
		_clientName = clientName;
		
	}
	
	public void registerSelf(int posx, int posy, int rotpos) throws UnknownHostException, IOException{
		_socket  = new Socket("localhost",4442);
		_socket.setKeepAlive(true);
		
		//writer for this socket
		PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
		_writer = new BufferedWriter(out);
		
		//reader for this socket
		_in = new BufferedReader(
                new InputStreamReader(
                _socket.getInputStream()));
		
		
		RegisterRequest request = new RegisterRequest(_clientName,posx,posy, rotpos);
		String registerRequest = requestFactory.marshallRequest(request);
		writeToSocket(registerRequest);
		//out.flush();
	}
	
	public void moveX(int moveX, int curPosX, int curPosY, int rotPos) throws UnknownHostException, IOException{
		MoveRequest request = new MoveRequest(_clientName,curPosX, curPosY, rotPos, moveX, 0);
		String registerRequest = requestFactory.marshallRequest(request);
		writeToSocket(registerRequest);
		//out.flush();
	}
	
	public void moveY(int moveY, int curPosX, int curPosY, int rotPos) throws UnknownHostException, IOException{
		MoveRequest request = new MoveRequest(_clientName,curPosX, curPosY, rotPos, 0, moveY);
		String registerRequest = requestFactory.marshallRequest(request);
		writeToSocket(registerRequest);
		//out.flush();
	}
	
	public void writeToSocket(String string) throws IOException{
		_writer.write(string);
		_writer.newLine();
		_writer.flush();
	}
	
	public String readMessage() throws IOException{
		return _in.readLine();
	}
	
	
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
		ServerStub stub = new ServerStub("abdul");
		ServerStub stub2 = new ServerStub("abdul2");
		stub.registerSelf(0, 0, RegisterRequest.ROT_POS_UP);
		stub2.registerSelf(0, 0, RegisterRequest.ROT_POS_DOWN);
		Thread.sleep(500);
		stub.moveX(1,0,0,0);
		stub.moveX(1,0,0,0);
		stub.moveX(1,0,0,0);
		stub.moveX(1,0,0,0);
		stub.moveY(1, 0, 0, 0);
		//stub2.moveX();
		//stub2.moveX();
		String readMessage = stub2.readMessage();
		String readMessage2 = stub2.readMessage();
		String readMessage3 = stub2.readMessage();
		String readMessage4 = stub2.readMessage();
		
		
		while(1==1);
	}
}
