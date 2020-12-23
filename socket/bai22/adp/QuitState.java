package bai22.adp;

public class QuitState implements State {
	private ServerProcess sp;

	public QuitState(ServerProcess sp) {
		this.sp = sp;
	}

	@Override
	public void handle() {
		sp.println("Bye");
		return;
	}

}
