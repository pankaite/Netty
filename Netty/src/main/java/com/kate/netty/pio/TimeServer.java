package com.kate.netty.pio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * @auther pankaite
 * @date 2017/10/17
 * @version 1.0
 * @description 伪异步IO
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
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The Time Server is starting in port : " + port);
			Socket socket = null;
			TimeServerHandlerExcutePool singleExecutor = new TimeServerHandlerExcutePool(50, 10000);
			while(true){
				socket = server.accept();
				singleExecutor.execute(new TimeServerHandler(socket));
			}
		} finally {
			if (server != null) {
				System.out.println("The time server close!");
				server.close();
				server = null;
			}
		}
	}
}
