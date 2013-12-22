

public class BaseRequest {

	private String _clientName;
	private int  _posX;
	private int  _posY;
	private int  _rotPos;
	
	public BaseRequest(String clientName, int posX, int posY, int rotPos){
		_clientName = clientName;
		_posX = posX;
		_posY = posY;
		_rotPos = rotPos;
		
	}
	
	public String getClientName(){
		return _clientName;
	}
	
	public int getPosX(){
		return _posX;
	}
	
	public int getPosY(){
		return _posY;
	}
	
	public int getRotPos(){
		return _rotPos;
	}

}

