package com.konka.dhtsearch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

import net.sf.json.JSONObject;
import ua.com.alexandr.bencode.BDecoder;
import ua.com.alexandr.bencode.BEncoder;

public class DhtSearch {

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {

		// String msg="{"t\\":\\"aa\\", "y":"q", "q":"find_node", "a": {"id":"abcdefghij0123456789", "target":"mnopqrstuvwxyz123456"}}";
		JSONObject a = new JSONObject();
		a.put("id", "abcdefghij0123456782");
		a.put("target", "mnopqrstuvwxyz123456");
		// System.out.println(a.toString());
		// String msg="{\"t\":\"dd\",\"y\":\"q\",\"q\":\"find_node\",\"a\":\"{\"id\":\"abcdefghij0123456782\",\"target\":\"mnopqrstuvwxyz123456\"}\"}";
		JSONObject object = new JSONObject();
		object.put("t", "dd");
		object.put("y", "q");
		object.put("q", "find_node");
		object.put("a", a.toString());
		BEncoder bEncoder = new BEncoder();
		// BEncoder.
		byte buf[] = bEncoder.bencode(object.toString());
		System.out.println(object.toString());
		InetAddress address = InetAddress.getByName("0.0.0.0");
		DatagramSocket socket = new DatagramSocket(9500, address);
		// It
		// byte buf[] = msg.getBytes();
		// DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("198.98.102.169"), 55555);
		// packet.
		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("router.bittorrent.com"), 6881);
		socket.send(packet);

		System.out.println("发送完成");

		// DatagramSocket socket = new DatagramSocket(55555);
		byte[] buf1 = new byte[1024];
		DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length);

		// String gg="{a={id=2 }, q=\"ping\", t=\"d\", y=1}";
		// JSONObject jsonObject1=JSONObject.fromString(gg.replace("=", ":"));

		// if(jsonObject.has("a")){
		// String ra=jsonObject.getString("a");
		// System.out.println("a="+ra);
		// System.out.println("t="+jsonObject.getString("t"));
		// System.out.println("y="+jsonObject.getString("y"));
		// }

		for (int i = 0; i <= 50; i++) {
			socket.receive(packet1);
			System.out.println("Received from:" + packet1.getSocketAddress());
			byte[] b = packet1.getData();
			// System.out.println(b);
			InputStream in = new ByteArrayInputStream(b);
			BDecoder bDecoder = new BDecoder(in);

//			System.out.println("Data is--:" + new String(b, "UTF-8"));
			// String json=bDecoder.decode().toString();

			// bDecoder.bdecode(in)
			// String json=bDecoder.bdecodeBytes().getString();
			Map<String, Object> map=null ;
			try {
				  map = (Map<String, Object>) bDecoder.decode();
			} catch (Exception e) {
				 continue;
			}
//			System.out.println("Data is:" + map);
			String q = (String) map.get("q");
			System.out.println("q=" + q);
//			System.out.println("t=" + map.get("t"));
//			System.out.println("y=" + map.get("y"));
			Map<String, Object> mapa = (Map<String, Object>) map.get("a");

//			System.out.println("mapa=" + mapa);
//			System.out.println("mapid=" + mapa.get("id"));
			// {"t":"aa", "y":"r", "r": {"id":"mnopqrstuvwxyz123456"}}

			if ("ping".equals(q)) {
				JSONObject aa = new JSONObject();
				aa.put("id", "abcdefghij0123456782");
				JSONObject bb = new JSONObject();
				bb.put("t", "aa");
				bb.put("y", "r");
				bb.put("r", aa.toString());
//				byte[] buf11 = bb.toString().getBytes("UTF-8");
				BEncoder bEncoder1 = new BEncoder();
				// BEncoder.
				String ps=bb.toString();
				System.out.println("响应ping="+ps);
				byte buf11[] = bEncoder1.bencode(ps);
				DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length, packet1.getSocketAddress());
				socket.send(packet11);
			} else if ("find_node".equals(q)) {
				// tid = msg["t"]
				// target = msg["a"]["target"]
				// self.master.log(target, address)
				// self.play_dead(tid, address)
				JSONObject aa = new JSONObject();
				aa.put("id", "abcdefghij0123456782");
				String t=(String) mapa.get("target");
				JSONObject bb = new JSONObject();
				bb.put("t", t);
//				  y="e",
//				            e=[202, "Server Error"]
				bb.put("y", "e");
				bb.put("e", "Server Error");
				BEncoder bEncoder1 = new BEncoder();
				String sd=bb.toString();
				System.out.println("响应find_node="+sd);
				byte[] buf11 =bEncoder1.bencode(sd);
//				byte[] buf11 = bb.toString().getBytes("UTF-8");

				DatagramPacket packet11 = new DatagramPacket(buf11, buf11.length, packet1.getSocketAddress());
				socket.send(packet11);
			}else{
				System.out.println("收到info");
			}

		}

		socket.close();

	}
	// msg = dict(
	// t=tid,
	// y="q",
	// q="find_node",
	// a=dict(id=nid, target=random_id())
}
// BOOTSTRAP_NODES = [
// ("router.bittorrent.com", 6881),
// ("dht.transmissionbt.com", 6881),
// ("router.utorrent.com", 6881)
// ] 