package com.kate.netty.nio;

/*
 * @auther pankaite
 * @date 2017/10/17
 * @version 1.0
 * @description 同步阻塞式IO
 */
public class TimeClient {
	/*
	 *  @param args
	 */
	public static void main(String[] args) {
		int port = 8090;
		if(args != null && args.length > 0){
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				//default value
			}
		}
		new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
	}

}
