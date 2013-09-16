
public class SwitchButton extends SOMButton{
	
	public SwitchButton(Maps p, float r, float g, float b, int pos, String name) {
		super(p, r, g, b, pos, name);
		// TODO Auto-generated constructor stub
	}
	
	public void tuioCursorAdded(long sessionID, int cursorX, int cursorY) {
		if((cursorX > (xPos - 12) && cursorX < (xPos + 12)) && (cursorY > (p.screenH+6) && cursorY < (p.screenH+44))){
			p.bSwitch = p.bSwitch?false:true;
			p.udt.updateText("Switch");  
		}		
	}

}
