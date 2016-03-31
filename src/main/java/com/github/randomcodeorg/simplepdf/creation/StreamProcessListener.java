package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.FormattingTools;
import com.github.randomcodeorg.simplepdf.ProcessMessage;
import com.github.randomcodeorg.simplepdf.ProcessMessage.MessageType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * An implementation of {@link ProcessListener} that writes events to the given streams.
 * @author Marcel Singer
 *
 */
public class StreamProcessListener implements ProcessListener {

	private final OutputStreamWriter stdStreamWriter;
	private final OutputStreamWriter errStreamWriter;
	private boolean leaveOpened = true;
	
	/**
	 * Creates a new instance of {@link StreamProcessListener}.
	 * @param stdStreamWriter The writer to write information events to.
	 * @param errStreamWriter The writer to write warning and error events to.
	 */
	public StreamProcessListener(OutputStreamWriter stdStreamWriter, OutputStreamWriter errStreamWriter){
		this.stdStreamWriter = stdStreamWriter;
		this.errStreamWriter = errStreamWriter;
	}
	
	/**
	 * Creates a new instance of {@link StreamProcessListener}.
	 * @param stdStream The stream to write information events to.
	 * @param errStream The stream to write warning and error events to.
	 */
	public StreamProcessListener(OutputStream stdStream, OutputStream errStream){
		this(new OutputStreamWriter(stdStream), new OutputStreamWriter(errStream));
	}
	
	/**
	 * Sets if the streams should be closed when the listeners completes ({@link #complete()}.
	 * @param leaveOpened <code>true</code> if the streams should not be closed.
	 */
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
	
	/**
	 * Writes the given message using the given writer.
	 * @param writer The writer to be used.
	 * @param pm The message to write.
	 */
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
