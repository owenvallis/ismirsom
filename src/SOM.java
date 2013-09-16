import processing.core.PApplet;
import processing.core.PConstants;

public class SOM {

	Maps p; // The parent PApplet that we will render ourselves onto

	//This is a test
	int numberOfXnodes;
	int numberOfYnodes;
	Node[][] nodes;
	double radius;
	double timeConstant;
	float learnRate = (float) 0.09;
	int inputDimension;

	SOM(int w, int h, int n, Maps p)
	{
		this.p = p;
		numberOfXnodes = w;
		numberOfYnodes = h;
		radius = Math.max(h, w) / 2;
		inputDimension = n;

		nodes = new Node[w][h];
		// create nodes/initilize map
		for(int i = 0; i < w; i++){
			for(int j = 0; j < h; j++) {
				nodes[i][j] = new Node(n, w, h, p);
				nodes[i][j].x = i;
				nodes[i][j].y = j;
				nodes[i][j].setVector();
			}//for j
		}//for i

	} 

	void initTraining(int iterations)
	{
		timeConstant = iterations/Math.log(radius);   
	}

	void transferData(){
		for(int i = 0; i < numberOfXnodes; i++) {
			for(int j = 0; j < numberOfYnodes; j++) {

				//get best matching unit
				int ndxComposite = bestMatch(p.data.songs.get((i*numberOfYnodes)+j).dimensions);
				int x = ndxComposite >> 16;
			int y = ndxComposite & 0x0000FFFF;
			nodes[x][y].trueVectorLocation = (i*numberOfYnodes)+j;
			//nodes[x][y].w = p.data.songs.get(nodes[x][y].trueVectorLocation).dimensions;
			if(nodes[x][y].taken){
				System.out.println("I'm already filled");
			}
			else{
				nodes[x][y].taken = true;
			}
			}
		}
	}


	//This is where the magic happens!
	void train(int i)
	{   
		//Decrease the learning radius and change rate
		p.radiusDecay = radius*Math.exp(-i/timeConstant);
		p.learnDecay = learnRate*Math.exp(-i/timeConstant);
		int t = (int)(Math.random()*599);
		//get best matching unit
		int ndxComposite = bestMatch(p.data.songs.get(t).dimensions);
		int x = ndxComposite >> 16;
		int y = ndxComposite & 0x0000FFFF;
			//if (bDebug) println("bestMatch: " + x + ", " + y + " ndx: " + ndxComposite);
			//scale best match and neighbors...
			for(int a = 0; a < numberOfXnodes; a++) {
				for(int b = 0; b < numberOfYnodes; b++) {

					//float d = distance(nodes[x][y], nodes[a][b]);
					float d = PApplet.dist(nodes[x][y].x, nodes[x][y].y, nodes[a][b].x, nodes[a][b].y);
					//scale the amount of change
					double influence = Math.exp((-PApplet.sq(d)) / (2*p.radiusDecay*i));
					//println("Best Node: ("+x+", "+y+") Current Node ("+a+", "+b+") distance: "+d+" radiusDecay: "+radiusDecay);

					if (d < p.radiusDecay)          
						for(int k = 0; k < inputDimension; k++)
							//this is it! changes the node
							nodes[a][b].w[k] += influence*p.learnDecay*((p.data.songs.get(t).dimensions[k]*p.d[k]) - nodes[a][b].w[k]);

				} //for j
			} // for i

	} // train()

	float distance(Node node1, Node node2)
	{
		return PApplet.sqrt( PApplet.sq(node1.x - node2.x) + PApplet.sq(node1.y - node2.y) );
	}

	int bestMatch(float w[])
	{
		float minDist = PApplet.sqrt(inputDimension);
		int minIndex = 0;
		float tmp;

		for (int i = 0; i < numberOfXnodes; i++) {
			for (int j = 0; j < numberOfYnodes; j++) {
				if(!nodes[i][j].taken){
					tmp = weight_distance(nodes[i][j].w, w);
					//System.out.println(tmp + " " + i + " " + j + " " + minIndex);
					if (tmp < minDist) {
						minDist = tmp;
						minIndex = (i << 16) + j;
					}  //if
				}//test for taken
			} //for j
		} //for i


		// note this index is x << 16 + y. 
		return minIndex;
	}

	float weight_distance(float x[], float y[])
	{
		if (x.length != y.length) {
			System.out.println ("Error in SOM::distance(): array lens doesn't match");
			p.exit();
		}
		float tmp = (float) 0.0;
		for(int i = 0; i < x.length; i++)
			tmp += ((x[i] - (y[i]*p.d[i])) * (x[i] - (y[i]*p.d[i])));
		tmp = PApplet.sqrt(tmp);
		return tmp;
	}

	void render()
	{
		for(int i = 0; i < numberOfXnodes; i++) {
			for(int j = 0; j < numberOfYnodes; j++) {
				p.fill(nodes[i][j].getR(), nodes[i][j].getG(), nodes[i][j].getB());
				p.stroke(0);
				p.strokeWeight(0);
				p.rectMode(PConstants.CORNER);
				p.rect(p.xNodeOffset+(i*p.pixPerNodeW), j*p.pixPerNodeH, p.pixPerNodeW, p.pixPerNodeH); 
				//if (bDebug) println("Writing rect ("+r+", "+g+", "+b+") at ("+i*pixPerNodeW+", "+ j*pixPerNodeH+")");
			} // for j
		} // for i
	} // render()
}
