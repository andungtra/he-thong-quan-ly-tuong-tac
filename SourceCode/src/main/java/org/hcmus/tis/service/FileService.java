package org.hcmus.tis.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileService {
	public File getFile(String path){
		return new File(path);
	}
	public FileOutputStream getFileOutPutStream(String path) throws FileNotFoundException{
		return new FileOutputStream(new File(path));
	}

}
