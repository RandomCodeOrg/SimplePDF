package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

import java.util.ArrayList;
import java.util.List;

public class DefaultProcessListener implements ProcessListener {

	private final List<ProcessMessage> messages = new ArrayList<ProcessMessage>();

	public DefaultProcessListener() {
			
	}

	@Override
	public void start() {

	}

	@Override
	public void addMessage(ProcessMessage pm) {
		messages.add(pm);
	}

	@Override
	public void complete() {

	}
	
	public int getMessageCount(){ return messages.size(); }
	public ProcessMessage getMessage(int index){ return messages.get(index); }

}
