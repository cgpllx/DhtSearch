package com.konka.dhtsearch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.yaircc.torrent.bencoding.BDecodingException;
import org.yaircc.torrent.bencoding.BEncodedInputStream;
import org.yaircc.torrent.bencoding.BEncodedOutputStream;
import org.yaircc.torrent.bencoding.BMap;
import org.yaircc.torrent.bencoding.BTypeException;
import org.yaircc.torrent.data.Hash;
import org.yaircc.torrent.data.Hash.Type;

public class DhtSearch {

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {

		JSONObject ajson = new JSONObject();
		ajson.put("id", "FF5C85FE1FDB933503999F9EB2EF59E4B0F51ECA");// 20位发件人的
		// ajson.put("target", "mnopqrstuvwxyz123456");// 要查询的id
		JSONObject object = new JSONObject();
		object.put("t", "dd");
		object.put("y", "q");
		object.put("q", "ping");
//		object.put("q", "find_node");
		object.put("a", ajson.toString());
		byte buf[] = BEncodedOutputStream.bencode(object.toString());
		// BEncoder bEncoder = new BEncoder();
		// byte buf[] = bEncoder.bencode(object.toString());
		System.out.println(object.toString());
		InetAddress address = InetAddress.getByName("0.0.0.0");
		final DatagramSocket socket = new DatagramSocket(9500, address);
		// It
		// byte buf[] = msg.getBytes();
		// DatagramPacket packet = new DatagramPacket(buf, buf.length,
		// InetAddress.getByName("198.98.102.169"), 55555);
		// packet.
		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("67.215.246.10"), 6881);
		// DatagramPacket packet = new DatagramPacket(buf, buf.length,
		// InetAddress.getByName("91.121.60.42"), 6881);
		// DatagramPacket packet = new DatagramPacket(buf, buf.length,
		// InetAddress.getByName("router.bittorrent.com"), 6881);
		socket.send(packet);
		// nodes = [["127.0.0.1", 9500], ["your.router.node", 4804]]
		System.out.println("发送完成");

		// DatagramSocket socket = new DatagramSocket(55555);
		byte[] buf1 = new byte[1024];
		final DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length);

		for (int i = 0; i <= 500; i++) {
			socket.receive(packet1);
//			System.out.println("Received from:" + packet1.getSocketAddress());
			final byte[] b = packet1.getData();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					handleMsg(b, socket, packet1);
				} catch (BDecodingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
			
		}

		socket.close();

	}
	public static void handleMsg(byte[] b,DatagramSocket socket,DatagramPacket packet1) throws BDecodingException, Exception{
		// System.out.println(b);

		// System.out.println("q=" + q);
		// System.out.println("新jar解码=" + BEncodedInputStream.bdecode(b));

		BMap bMap;
		try {
			bMap = (BMap) BEncodedInputStream.bdecode(b);
			System.out.println(bMap);
		} catch (Exception e1) {
//			e1.printStackTrace();
			System.out.println("取错="+BEncodedInputStream.bdecode(b));
//			Hash hastinfo_hash = new Hash((byte[]) BEncodedInputStream.bdecode(b), Type.ID);
			System.out.println("cgp="+new String((byte[])BEncodedInputStream.bdecode(b)));
			return;
		}
		String q = bMap.getString("q");

		System.out.println("q=" + bMap.getString("q"));
		// System.out.println("t=" + bMap.getString("t"));
//		System.out.println("y=" + bMap.getString("y"));
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
			id= hash.asHexString();
			// System.out.println("id=" + hash.asHexString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if(aa.containsKey("target")){
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
			System.out.println("响应ping=" + ps);
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
			sendMsg(socket, id, packet1.getSocketAddress());
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

	// target要查询的id
	public static   void sendMsg(DatagramSocket socket, String target, SocketAddress direction) throws Exception {
		JSONObject ajson = new JSONObject();
		ajson.put("id", "12345678901234567890");// 20位发件人的
		ajson.put("target", target);// 要查询的id
		JSONObject object = new JSONObject();
		object.put("t", "aa");
		object.put("y", "q");
		object.put("q", "find_node");
		object.put("a", ajson.toString());
		// BEncoder bEncoder = new BEncoder();
		byte buf[] = BEncodedOutputStream.bencode(object.toString());
//		System.out.println("收到find后重新查询他" + object.toString());
		DatagramPacket packet = new DatagramPacket(buf, buf.length, direction);
		socket.send(packet);
	}
}
