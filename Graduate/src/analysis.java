import java.io.File;

public class analysis {
	public analysis() {
		
	}
	public Boolean isFileExists(String path){
		File file=new File(path);
		if(file.exists())
		{
			return true;
		}
		
		else {
			
			return false;
		}
			
	}

}
