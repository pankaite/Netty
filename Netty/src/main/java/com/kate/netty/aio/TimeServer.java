package com.kate.netty.aio;

import java.io.IOException;

/*
 * @auther pankaite
 * @date 2017/10/17
 * @version 1.0
 * @description NIO
 */
public class TimeServer {
	/*
	 *  @param args
	 *  @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int port = 8090;
		if(args != null && args.length > 0){
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				//default value
			}
		}
		AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
		new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
	}
}
