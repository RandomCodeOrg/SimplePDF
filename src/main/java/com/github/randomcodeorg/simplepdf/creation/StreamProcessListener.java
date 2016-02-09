package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.FormattingTools;
import com.github.randomcodeorg.simplepdf.ProcessMessage;
import com.github.randomcodeorg.simplepdf.ProcessMessage.MessageType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamProcessListener implements ProcessListener {

	private final OutputStreamWriter stdStreamWriter;
	private final OutputStreamWriter errStreamWriter;
	private boolean leaveOpened = true;
	
	
	public StreamProcessListener(OutputStreamWriter stdStreamWriter, OutputStreamWriter errStreamWriter){
		this.stdStreamWriter = stdStreamWriter;
		this.errStreamWriter = errStreamWriter;
	}
	
	public StreamProcessListener(OutputStream stdStream, OutputStream errStream){
		this(new OutputStreamWriter(stdStream), new OutputStreamWriter(errStream));
	}
	
	public void setLeaveOpen(boolean leaveOpened){
		this.leaveOpened = leaveOpened;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void addMessage(ProcessMessage pm) {
		OutputStreamWriter writer = stdStreamWriter;
		MessageType mt = pm.getMessageType();
		if(mt == MessageType.ERROR || mt == MessageType.WARNING) writer = errStreamWriter;
		writeMessage(writer, pm);
	}
	
	private void writeMessage(OutputStreamWriter writer, ProcessMessage pm){
		StringBuilder sb = new StringBuilder();
		sb.append(pm.getMessageType());
		if(pm.hasPosition()) sb.append(" at position " + pm.getAffectedPosition() );
		sb.append(":\n");
		sb.append("\t" + pm.getMessage());
		sb.append("\n\tElement:\n");
		sb.append(FormattingTools.indentText(pm.getAffectedElement().toXML(), 2));
		try {
			writer.write(sb.toString() + "\n\n\n");
		} catch (IOException e) {

		}
	}

	@Override
	public void complete() {
		try{
			stdStreamWriter.flush();
			errStreamWriter.flush();
			if(!leaveOpened){
				stdStreamWriter.close();
				errStreamWriter.close();
			}
		}catch(IOException ex){
			
		}
	}
	
	
	
	
}
