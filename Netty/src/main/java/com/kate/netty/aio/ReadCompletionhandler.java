package com.kate.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

public class ReadCompletionhandler implements CompletionHandler<Integer, ByteBuffer> {
	
	private AsynchronousSocketChannel channel;
	
	public ReadCompletionhandler(AsynchronousSocketChannel channel) {
		if (this.channel == null) {
			this.channel = channel;
		}
	}

	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		String req;
		try {
			req = new String(body, "UTF-8");
			System.out.println("The time server receive order : " + req);
			String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
			doWrite(currentTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void doWrite(String currentTime) {
		if (currentTime != null && currentTime.trim().length() > 0) {
			byte[] bytes = currentTime.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

				public void completed(Integer result, ByteBuffer buffer) {
					if (buffer.hasRemaining()) {
						channel.write(buffer, buffer, this);
					}
				}

				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						channel.close();
					} catch (IOException e) {
						//ignore on close
					}
				}
			});
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
