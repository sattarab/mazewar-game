

public class ActionRequest extends BaseRequest{

	public int _action; 
	
	public ActionRequest(String clientName, int posX, int posY, int rotPos, int action) {
		super(clientName, posX, posY, rotPos);
		// TODO Auto-generated constructor stub
		_action = action;
	}
	
	public int getAction(){
		return _action;
	}

}
