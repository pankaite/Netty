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
//		Socket socket = null;
//		BufferedReader in = null;
//		PrintWriter out = null;
//		try {
//			socket = new Socket("127.0.0.1", port);
//			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			out = new PrintWriter(socket.getOutputStream(), true);
//			out.println("QUERY TIME ORDER");
//			System.out.println("Send order to server succeed!");
//			String resp = in.readLine();
//			System.out.println("Now is : " + resp);
//		} catch (Exception e) {
//			// do nothing
//		} finally {
//			if (out != null) {
//				out.close();
//				out = null;
//			}
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				in = null;
//			}
//			if (socket != null) {
//				try {
//					socket.close();
//				} catch (IOException e2) {
//					e2.printStackTrace();
//				}
//				socket = null;
//			}
//		}
	}

}
