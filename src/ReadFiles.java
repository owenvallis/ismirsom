import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;


public class ReadFiles {


	ArrayList<Song> songs = new ArrayList<Song>(); //collection of feature data/song
	FilenameFilter filterTxt = new TXTFilter(); //data filter types
	File directory = new File("data/"); //set data folder path
	File[] files = directory.listFiles(filterTxt); //create an array of filtered files from the data folder

	ReadFiles(){
		readTextFiles();	
		Scale();
	}

	void readTextFiles() {

		//Puts the strings into the first position of sequential ArrayList Elements
		for (File f : files) {
			

			try {
				Scanner scanner = new Scanner(f);
				scanner.useDelimiter(System.getProperty("line.separator")); 
				
				while (scanner.hasNext()) {
					Song song = new Song(scanner.next());
					songs.add(song);
				}
				scanner.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	
	void Scale()
	{
		// For Every Features - Scale from 0 to 1
		for (int i = 0; i < songs.get(0).dimensions.length; i++)
		{
			// Negative number finder
			int negFlag = 0;
			
			// Find Maximum
			float maxValue = songs.get(0).dimensions[i];
			
			for (int j = 0; j < songs.size(); j++)
			{
				if (Math.abs(songs.get(j).dimensions[i]) > maxValue)
				{
					maxValue = Math.abs(songs.get(j).dimensions[i]);
				}
			}
			
			// Scale by maxValue
			for (int j = 0; j < songs.size(); j++) 
			{
				if (songs.get(j).dimensions[i] < 0) 
				{
					negFlag = 1;
				}
				if(maxValue != 0){
				songs.get(j).dimensions[i] = songs.get(j).dimensions[i]/maxValue;
				}
			}
			
			// if vector data is found to contain negative values, bring min to zero and then scale back to 0.0 - 1.0
			
			if (negFlag == 1)
			{
				for (int j = 0; j < songs.size(); j++) 
				{
					songs.get(j).dimensions[i] = (float) ((songs.get(j).dimensions[i]+1.0)*.5);
				}
			}
			
		}
	}
	

}

class TXTFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.endsWith(".txt"));
	}

}

