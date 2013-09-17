
public class SOMMusicPlayer {
	
	Maps p;
	int pixPerNodeW;
	int pixPerNodeH;
	int locationX;
	int locationY;
	boolean draw = false;
	
	SOMMusicPlayer(int h, int w, Maps p){
		this.p = p;
		pixPerNodeW = p.screenW/ h;
		pixPerNodeH = p.screenH/ w;
	}
	
	public void playSong(int cursorX, int cursorY){
		if(cursorX > p.xNodeOffset  && cursorX < (p.screenW - p.xNodeOffset) && cursorY < p.screenH){
			draw = true;
			if(p.song.isPlaying()){
				p.song.close();
			}
			locationX = (cursorX-p.xNodeOffset)/pixPerNodeW;
			if(locationX >= p.numberOfXnodes){
				locationX = (p.numberOfXnodes-1);
			}
			locationY = cursorY/pixPerNodeH;
			if(locationY >= p.numberOfYnodes){
				locationY = (p.numberOfYnodes-1);
			}
			//System.out.println(cursorY + " " + pixPerNodeH + " " + locationY + " " + (cursorY/pixPerNodeH));
			//System.out.println(p.data.songs.get(p.som.nodes[locationX][locationY].trueVectorLocation).name);
			p.udt.updateText(p.data.songs.get(p.som.nodes[locationX][locationY].trueVectorLocation).name);
			p.song = p.minim.loadFile(p.data.songs.get(p.som.nodes[locationX][locationY].trueVectorLocation).name + ".mp3");
			p.song.play();			
		}
		else{

		}
		
		

	}

}
