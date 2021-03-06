package com.konka.dhtsearch;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.yaircc.torrent.bencoding.BDecodingException;
import org.yaircc.torrent.bencoding.BEncodedInputStream;
import org.yaircc.torrent.bencoding.BEncodedOutputStream;
//import org.yaircc.torrent.bencoding.BDecodingException;
//import org.yaircc.torrent.bencoding.BEncodedInputStream;
//import org.yaircc.torrent.bencoding.BEncodedOutputStream;
import org.yaircc.torrent.bencoding.BMap;
import org.yaircc.torrent.data.Hash;
import org.yaircc.torrent.data.Hash.Type;

import ua.com.alexandr.bencode.BEncoder;

public class DhtSearch {

	static SocketAddress[] addresss = { //
	new InetSocketAddress("router.bittorrent.com", 6881), //
			new InetSocketAddress("dht.transmissionbt.com", 6881),//
			new InetSocketAddress("router.utorrent.com", 6881) };

	public static void main(String args[]) throws Exception {
		// new InetSocketAddress("router.bittorrent.com", 6881),
		// new InetSocketAddress("dht.transmissionbt.com", 6881),
		// new InetSocketAddress("router.utorrent.com", 6881)
		InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
		final DatagramSocket socket = new DatagramSocket(9500, inetAddress);

		for (SocketAddress address : addresss) {
			// socket.bind(address);
			for (int i = 0; i < 50; i++) {
				find_node(socket, Util.sha(rundom_id(20)), address);
			}

		}
		Thread thread = new Thread() {
			@Override
			public void run() {
				super.run();
				startservice(socket);
			}
		};
		thread.start();
		// DatagramSocket socket = new DatagramSocket(55555);

		// socket.
		// socket.bind(packet.getSocketAddress());
		// for (int i = 0; i <= 5000; i++) {
		// socket.receive(packet1);
		// // System.out.println("Received from:" +
		// // packet1.getSocketAddress());
		// final byte[] b = packet1.getData();
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// handleMsg(b, socket, packet1);
		// } catch (BDecodingException e) {
		// e.printStackTrace();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }).start();
		//
		// }

		// socket.close();

	}

	public static void startservice(DatagramSocket socket) {
		for (int i = 0; i < 1000; i++) {
			byte[] buf1 = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf1, buf1.length);
			try {
				// System.out.println("等接受信息");
				socket.receive(packet);
				byte[] b = packet.getData();
				// System.out.println("收到信息了");
				handleMsg(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		stop(socket);
	}

	public static void stop(DatagramSocket socket) {

		socket.close();
	}

	public static void handleMsg(byte[] b) throws Exception {
		// BDecoder bd=new BDecoder(b);
		BMap bMap = (BMap) BEncodedInputStream.bdecode(b);
		if (bMap.getString("y").equals("r")) {
			System.out.println("y============" + bMap.getString("y"));
			System.out.println("r============" + bMap.getMap("r"));
			// System.out.println("q=" + bMap.getString("q"));
			System.out.println("t=" + bMap.getString("t"));
		}
	}

	public static void handleMsg(byte[] b, DatagramSocket socket, DatagramPacket packet1) throws BDecodingException, Exception {
		// System.out.println(b);

		// System.out.println("q=" + q);
		// System.out.println("新jar解码=" + BEncodedInputStream.bdecode(b));

		BMap bMap;
		try {
			bMap = (BMap) BEncodedInputStream.bdecode(b);
			// System.out.println(bMap);
		} catch (Exception e1) {
			// e1.printStackTrace();
			// System.out.println("取错=" + BEncodedInputStream.bdecode(b));
			// Hash hastinfo_hash = new Hash((byte[])
			// BEncodedInputStream.bdecode(b), Type.ID);
			// System.out.println("cgp=" + new String((byte[]) BEncodedInputStream.bdecode(b)));
			return;
		}
		String q = bMap.getString("q");

		// System.out.println("q=" + bMap.getString("q"));
		// System.out.println("t=" + bMap.getString("t"));
		System.out.println("y============" + bMap.getString("y"));
		// if(!bMap.getString("y").equals("r")){
		// continue;
		// }
		BMap aa = bMap.getMap("a");
		// System.out.println("id=" + aa.getString("id"));
		// System.out.println(" target=" + aa.getString("target"));
		// System.out.println("info_hash=" + aa.getString("info_hash"));
		String id = "";
		try {
			byte[] bb = (byte[]) aa.get("id");
			Hash hash = new Hash(bb, Type.ID);
			id = hash.asHexString();
			// System.out.println("id=" + hash.asHexString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (aa.containsKey("target")) {
				byte[] target = (byte[]) aa.get("target");
				Hash hastargeth = new Hash(target, Type.SHA1);
			}
			// System.out.println("hastargeth=" + hastargeth.asHexString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (aa.containsKey("info_hash")) {
				byte[] info_hash = (byte[]) aa.get("info_hash");
				Hash hastinfo_hash = new Hash(info_hash, Type.SHA1);
				// System.out.println("hastinfo_hash=" +
				// hastinfo_hash.asHexString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object t = null;
		try {
			t = bMap.get("t");
			// System.out.println("t=" + t);
			// Hash hashttt = new Hash(t, Type.ID);
			// System.out.println("t=" + hashttt.asHexString());

			// System.out.println("新t0=" + new String((byte[]) t));
			// System.out.println("新t1=" + new String((byte[]) t, "UTF-8"));
			// System.out.println("新t2=" + new String((byte[]) t,
			// "ISO-8859-1"));
			// System.out.println("ttt=" + new String(t, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("ping".equals(q)) {
			JSONObject aa1 = new JSONObject();
			// aa1.put
			aa1.put("id", "FF5C85FE1FDB933503999F9EB2EF59E4B0F51ECA");
			JSONObject bb = new JSONObject();
			// bb.put("t", map.get("t"));// 把t穿回去 （交易）
			bb.put("t", new String((byte[]) t, "UTF-8"));
			bb.put("y", "r");
			bb.put("r", aa1.toString());
			String ps = bb.toString();
			// System.out.println("响应ping=" + ps);
			byte[] buf11 = BEncodedOutputStream.bencode(ps);
			DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length, packet1.getSocketAddress());
			socket.send(packet11);
		} else if ("find_node".equals(q)) {
			// String nodes = [["127.0.0.1", 9500], ["your.router.node",
			// 4804]]
			// Map<String, Integer> map_node = new HashMap<>();
			// map_node.put("127.0.0.1", 9500);
			// String nodes = [["127.0.0.1", 9500], ["your.router.node",
			// 4804]];
			// sendMsg(socket, id, packet1.getSocketAddress());
			// DatagramPacket packet11 = new DatagramPacket(buf11,
			// buf11.length, packet1.getSocketAddress());

		} else {
			// System.out.println((q)
			;
		}

		// if ("ping".equals(q)) {
		// JSONObject aa = new JSONObject();
		// aa.put("id", "abcdefghij0123456782");
		// JSONObject bb = new JSONObject();
		// // bb.put("t", map.get("t"));// 把t穿回去 （交易）
		// bb.put("y", "r");
		// bb.put("r", aa.toString());
		// // byte[] buf11 = bb.toString().getBytes("UTF-8");
		// // BEncoder bEncoder1 = new BEncoder();
		// // BEncoder.
		// String ps = bb.toString();
		// System.out.println("响应ping=" + ps);
		// byte buf11[] = bEncoder1.bencode(ps);
		// DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length,
		// packet1.getSocketAddress());
		// socket.send(packet11);
		// } else if ("find_node".equals(q)) {
		// JSONObject aa = new JSONObject();
		// aa.put("y", "r");
		//
		// JSONObject bb = new JSONObject();
		// bb.put("id", "abcdefghij0123456782");
		// // bb.put("nodes", "abcdefghij0123456782");//这里有问题
		// nodes是4字节ip+2字节端口+20nodeid
		// aa.put("r", bb.toString());
		// aa.put("t", (String) map.get("t"));
		// BEncoder bEncoder1 = new BEncoder();
		// System.out.println("响应find_node=" + aa.toString());
		// byte[] buf11 = bEncoder1.bencode(aa.toString());
		// DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length,
		// packet1.getSocketAddress());
		// socket.send(packet11);
		// // sendMsg(socket, (String) map.get("t"),
		// packet1.getSocketAddress());
		// } else {
		// System.out.println("收到info");
		// }
	}

	public static String rundom_id(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = (char) (Math.random() * 255);
			builder.append(c);
		}
		return builder.toString();
	}

	// target要查询的id
	public static void find_node(DatagramSocket socket, String target, SocketAddress direction) throws Exception {
//		JSONObject ajson = new JSONObject();
//		ajson.put("id", Util.sha(rundom_id(20)));// 20位发件人的
//		ajson.put("target", target);// 要查询的id
//		JSONObject object = new JSONObject();
//		object.put("t", rundom_id(4));
//		object.put("y", "q");
//		object.put("q", "find_node");
//		object.put("a", ajson.toString());
		
		BEncoder bEncoder = new BEncoder();
		HashMap<String, Object> hashBMap = new HashMap<String, Object>();
		hashBMap.put("t", rundom_id(4));
		hashBMap.put("y", "q");
		hashBMap.put("q", "find_node");
		HashMap<String, Object> aa = new HashMap<String, Object>();
		aa.put("id", Util.sha(rundom_id(20)));
		aa.put("target", target);
		hashBMap.put("a", System.currentTimeMillis() + "");
		// byte buf[] = BEncodedOutputStream.bencode(hashBMap);
		byte buf[] = bEncoder.bencode(hashBMap);
		System.out.println("findnode=" + new String(buf));
		DatagramPacket packet = new DatagramPacket(buf, buf.length, direction);
		socket.send(packet);
		System.out.println("find_node");
	}
}
