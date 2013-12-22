

public class MoveRequest extends BaseRequest {

	private int _movePosX;
	private int _movePosY;
	
	public MoveRequest(String clientName, int posX, int posY , int rotPos, int movePosX, int movePosY) {
		super(clientName, posX, posY, rotPos);
		_movePosX = movePosX;
		_movePosY = movePosY;
		// TODO Auto-generated constructor stub
	}
	
	public int getMovePosX(){
		return _movePosX;
	}
	
	public int getMovePosY(){
		return _movePosY;
	}
	
	

}
