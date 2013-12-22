

public class RequestFactory {

	//Request Form: {A0ction:,RequestParams:{Client:}}

	public BaseRequest unMarshallRequest(String Request){
		String requestParams [] = Request.split(",");
		if(requestParams[1].equals("action:register")){
			return createNewRegisterRequest(requestParams[0],
					requestParams[2],requestParams[3],requestParams[4]);
			
		}else if(requestParams[1].equals("action:action")){
			return createNewActionRequest(requestParams[0],requestParams[2], 
					requestParams[3], requestParams[4],
					requestParams[5]);
		}else{
			return null;
		}
	}
	
	
	public ActionRequest createNewActionRequest(String clientName, String posX, String posY, 
			String orientation, String action ){
		clientName = clientName.split(":")[1];
		posX = posX.split(":")[1];
		posY = posY.split(":")[1];
		orientation = orientation.split(":")[1];
		action = action.split(":")[1];
		
		return new ActionRequest(clientName, Integer.parseInt(posX), 
				Integer.parseInt(posY), Integer.parseInt(orientation), Integer.parseInt(action));
	}
	
	
	public String marshallRequest(BaseRequest request){
		if(request instanceof RegisterRequest){
			return marshallRegisterRequest((RegisterRequest)request);
		}else if (request instanceof MoveRequest){
			return marshallMoveRequest((MoveRequest)request);
		}else if (request instanceof ActionRequest){
			return marshallActionRequest((ActionRequest)request);
		}
		return null;
	}
	
	private String marshallActionRequest(ActionRequest request){
		String clientName = request.getClientName();
		String posX = new Integer(request.getPosX()).toString();
		String posY = new Integer(request.getPosY()).toString();
		String rotPos = new Integer(request.getRotPos()).toString();
		String action = new Integer(request.getAction()).toString();
		String marshalledRequest = "client:"+clientName+",action:action,"
				+"posx:"+posX+",posy:"+posY+",rotpos:"+rotPos+",action:"+action;
		return marshalledRequest;
	}
	

	private String marshallMoveRequest(MoveRequest request){
		String clientName = request.getClientName();
		String movePosX = new Integer(request.getMovePosX()).toString();
		String movePosY = new Integer(request.getMovePosY()).toString();
		//client:<clientName,action:move,posX:<posx>,posy:<posy>,curposx:<curPosX>,curposy:<curPosY>,currotpos:<rotPos>
		String marshalledRequest = "client:"+clientName+",action:move,"
				+"movex:"+movePosX+",movey:"+movePosY+
				",curposx:"+request.getPosX()+",curposy:"+request.getPosY()+",currotpos:"+request.getRotPos();
		return marshalledRequest;
		
	}
	
	private String marshallRegisterRequest(RegisterRequest request){
		String clientName = request.getClientName();
		String posX = new Integer(request.getPosX()).toString();
		String posY = new Integer(request.getPosY()).toString();
		String rotPos = new Integer(request.getRotPos()).toString();
		//client:<clientName>,action:register,posx:<posx>,posy:<posy>,rotpos:<rotpos>
		String marshalledRequest = "client:"+clientName+",action:register,"
				+"posx:"+posX+",posy:"+posY+",rotpos:"+rotPos;
		return marshalledRequest;
		
	}
	private BaseRequest createNewRegisterRequest(String clientNameParam, String posXParam,
			String posYParam, String rotPosParam) {
		String clientName = clientNameParam.split(":")[1];
		String posX = posXParam.split(":")[1];
		String posY = posYParam.split(":")[1];
		String rotpos = rotPosParam.split(":")[1];
		//String actualAction = actualActionParam.split(":")[1];
		RegisterRequest request = new RegisterRequest(clientName,Integer.parseInt(posX),
				Integer.parseInt(posY),Integer.parseInt(rotpos));
		//request.setAction(actualAction);
		return request;
	}
	
	private BaseRequest createMoveRequest(String clientNameParam, String posXParam,
			String posYParam, String curPosXParam, String curPosYParam, String curRotPosParam) {
		String clientName = clientNameParam.split(":")[1];
		String posX = posXParam.split(":")[1];
		String posY = posYParam.split(":")[1];
		String curPosX = curPosXParam.split(":")[1];
		String curPosY = curPosYParam.split(":")[1];
		String curRotPos = curRotPosParam.split(":")[1];
		return new MoveRequest(clientName,
				Integer.parseInt(curPosX), 
				Integer.parseInt(curPosY), Integer.parseInt(curRotPos), 
				Integer.parseInt(posX),Integer.parseInt(posY));
	}
	
}