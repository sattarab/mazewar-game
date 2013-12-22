

public class RegisterRequest extends BaseRequest{
	
	public static final int ROT_POS_UP = 0;
	public static final int ROT_POS_RIGHT=1;
	public static final int ROT_POS_DOWN=2;
	public static final int ROT_POS_LEFT=3;
	
	private int _posX;
	private int _posY;
	private int rot_pos; //rotation position 
	
	public RegisterRequest(String clientName, int posX, int posY, int ROT_POS) {
		super(clientName, posX, posY, ROT_POS);
		_posX = posX;
		_posY = posY;
		rot_pos = ROT_POS;
		// TODO Auto-generated constructor stub
	}
	
	public int getPosX() {
		return _posX;
	}

	public int getPosY() {
		return _posY;
	}
	
	public int getRotPos(){
		return rot_pos;
	}
	
	
}
