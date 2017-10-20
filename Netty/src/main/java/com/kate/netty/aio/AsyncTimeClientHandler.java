package com.kate.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>,Runnable{
	
	private AsynchronousSocketChannel channel;
	private String host;
	private int port;
	private CountDownLatch latch;
	
	public AsyncTimeClientHandler(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			channel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void run() {
		latch = new CountDownLatch(1);
		channel.connect(new InetSocketAddress(host, port), this, this);
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void completed(Void result, AsyncTimeClientHandler attachment) {
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

			public void completed(Integer result, ByteBuffer buffer) {
				if (buffer.hasRemaining()) {
					channel.write(buffer, buffer, this);
				}
				else {
					ByteBuffer readBuffer = ByteBuffer.allocate(1024);
					channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>(){

						public void completed(Integer result, ByteBuffer buffer) {
							buffer.flip();
							byte[] bytes = new byte[buffer.remaining()];
							buffer.get(bytes);
							String body;
							try {
								body = new String(bytes, "UTF-8");
								System.out.println("Now is : " + body);
								latch.countDown();
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}		
						}

						public void failed(Throwable exc, ByteBuffer buffer) {
							try {
								channel.close();
								latch.countDown();
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
					//ignore on close
				}
			}
		});
	}

	public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
		try {
			channel.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
