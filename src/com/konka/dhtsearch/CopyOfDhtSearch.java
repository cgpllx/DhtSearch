package com.konka.dhtsearch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.yaircc.torrent.bencoding.BEncodedInputStream;

import net.sf.json.JSONObject;
import ua.com.alexandr.bencode.BDecoder;
import ua.com.alexandr.bencode.BEncoder;

public class CopyOfDhtSearch {

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {

		JSONObject ajson = new JSONObject();
		ajson.put("id", "cgp123458dldkdjdiru2");// 20位发件人的
//		ajson.put("target", "mnopqrstuvwxyz123456");// 要查询的id
		JSONObject object = new JSONObject();
		object.put("t", "dd");
		object.put("y", "q");
		object.put("q", "find_node");
		object.put("a", ajson.toString());
		BEncoder bEncoder = new BEncoder();
		byte buf[] = bEncoder.bencode(object.toString());
		System.out.println(object.toString());
		InetAddress address = InetAddress.getByName("0.0.0.0");
		DatagramSocket socket = new DatagramSocket(9500, address);
		// It
		// byte buf[] = msg.getBytes();
		// DatagramPacket packet = new DatagramPacket(buf, buf.length,
		// InetAddress.getByName("198.98.102.169"), 55555);
		// packet.
		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("67.215.246.10"), 6881);
//		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("91.121.60.42"), 6881);
//		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("router.bittorrent.com"), 6881);
		socket.send(packet);

		System.out.println("发送完成");

		// DatagramSocket socket = new DatagramSocket(55555);
		byte[] buf1 = new byte[1024];
		DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length);

		for (int i = 0; i <= 5000; i++) {
			socket.receive(packet1);
			System.out.println("Received from:" + packet1.getSocketAddress());
			byte[] b = packet1.getData();
			// System.out.println(b);
			InputStream in = new ByteArrayInputStream(b);
			BDecoder bDecoder = new BDecoder(in);

			Map<String, Object> map = null;
			try {
				map = (Map<String, Object>) bDecoder.decode();
			} catch (Exception e) {
				continue;
			}
			String q = (String) map.get("q");
			System.out.println("q=" + q);
			System.out.println("新jar解码="+BEncodedInputStream.bdecode(b));

			// System.out.println("t=" + map.get("t"));
			// System.out.println("y=" + map.get("y"));
			Map<String, Object> a = (Map<String, Object>) map.get("a");

			System.out.println("map=" + map);
			// System.out.println("mapid=" + mapa.get("id"));
			// {"t":"aa", "y":"r", "r": {"id":"mnopqrstuvwxyz123456"}}

			if ("ping".equals(q)) {
				JSONObject aa = new JSONObject();
				aa.put("id", "abcdefghij0123456782");
				JSONObject bb = new JSONObject();
				bb.put("t", map.get("t"));// 把t穿回去 （交易）
				bb.put("y", "r");
				bb.put("r", aa.toString());
				// byte[] buf11 = bb.toString().getBytes("UTF-8");
				BEncoder bEncoder1 = new BEncoder();
				// BEncoder.
				String ps = bb.toString();
				System.out.println("响应ping=" + ps);
				byte buf11[] = bEncoder1.bencode(ps);
				DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length, packet1.getSocketAddress());
				socket.send(packet11);
			} else if ("find_node".equals(q)) {
				// tid = msg["t"]
				// target = msg["a"]["target"]
				// self.master.log(target, address)
				// self.play_dead(tid, address)
				// JSONObject aa = new JSONObject();
				// aa.put("id", "abcdefghij0123456782");
				// aa.put("nodes", "abcdefghij0123456782");
				// String t = (String) a.get("target");
				// JSONObject bb = new JSONObject();
				// // y="e",
				// String tt = (String) map.get("t");
				// bb.put("t", tt);
				// // e=[202, "Server Error"]
				// bb.put("y", "r");
				// bb.put("r", aa.toString());
				// BEncoder bEncoder1 = new BEncoder();
				// String sd = bb.toString();
				// System.out.println("响应find_node=" + sd);
				// byte[] buf11 = bEncoder1.bencode(sd);
				// byte[] buf11 = bb.toString().getBytes("UTF-8");

				// DatagramPacket packet11 = new DatagramPacket(buf11,
				// buf11.length, packet1.getSocketAddress());
				// socket.send(packet11);

				// ------------------上面是正确响应他
				// 然后还要采集他的数据 上面的数据中有一个target ，target就是info_hash
				// 对方findnode时候不应该响应他，而是拿到他的id去找他的node
				JSONObject aa = new JSONObject();
				aa.put("y", "r");

				JSONObject bb = new JSONObject();
				bb.put("id", "abcdefghij0123456782");
//				bb.put("nodes", "abcdefghij0123456782");//这里有问题 nodes是4字节ip+2字节端口+20nodeid
				aa.put("r", bb.toString());
				aa.put("t", (String) map.get("t"));
				BEncoder bEncoder1 = new BEncoder();
				System.out.println("响应find_node=" + aa.toString());
				byte[] buf11 = bEncoder1.bencode(aa.toString());
				DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length, packet1.getSocketAddress());
				socket.send(packet11);
				sendMsg(socket, (String) map.get("t"),  packet1.getSocketAddress());
			} else {
				System.out.println("收到info");
			}

		}

		socket.close();

	}

	// target要查询的id
	public static final void sendMsg(DatagramSocket socket,String target, SocketAddress direction) throws Exception {
		JSONObject ajson = new JSONObject();
		ajson.put("id", "cgp123458dldkdjdiru2");// 20位发件人的
//		ajson.put("target", target);// 要查询的id
		JSONObject object = new JSONObject();
		object.put("t", target);
		object.put("y", "q");
		object.put("q", "find_node");
		object.put("a", ajson.toString());
		BEncoder bEncoder = new BEncoder();
		byte buf[] = bEncoder.bencode(object.toString());
		System.out.println("收到find后重新查询他"+object.toString());
		DatagramPacket packet = new DatagramPacket(buf, buf.length, direction);
		socket.send(packet);
	}
}
