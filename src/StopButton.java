
public class StopButton extends SOMButton{

	public StopButton(Maps p, float r, float g, float b, int pos, String name) {
		super(p, r, g, b, pos, name);
		// TODO Auto-generated constructor stub
	}
	
	public void tuioCursorAdded(long sessionID, int cursorX, int cursorY) {
		if((cursorX > (xPos - 12) && cursorX < (xPos + 12)) && (cursorY > (p.screenH+6) && cursorY < (p.screenH+44))){
			p.player.draw = false;
			p.udt.updateText("Stop!");
			if(p.song.isPlaying()){
				p.song.close();
			}
			p.song = p.minim.loadFile(p.data.songs.get(p.som.nodes[0][0].trueVectorLocation).name + ".wav");
		}		
	}

}
