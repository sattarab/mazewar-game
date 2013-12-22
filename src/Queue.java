import java.util.Vector;


public class Queue {

	private Vector _messages;

	public Queue(){
		_messages = new Vector();
	}

	public void enQueue(Object object){
		synchronized(_messages){
			_messages.add(object);
			_messages.notifyAll();
		}
	}

	public Object deQueue() throws InterruptedException{
		synchronized(_messages){
			if(_messages.size() == 0){
				_messages.wait();
			}
			return _messages.remove(0);
		}
	}
}
