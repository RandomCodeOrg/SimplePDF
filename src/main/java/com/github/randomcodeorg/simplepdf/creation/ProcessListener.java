package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

public interface ProcessListener {

	public void start();
	
	public void addMessage(ProcessMessage pm);
	
	public void complete();
	
}
