package ua.com.alexandr.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ua.com.alexandr.bencode.BEncoderConstants.*;

public class BEncoder {

	public byte[] bencode(Object o) throws IllegalArgumentException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(o, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	public byte[] bencode(String s) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(s, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	public void bencode(String s, OutputStream out) throws IOException {
		byte[] bs = s.getBytes(UTF_8);
		bencode(bs, out);
	}

	public byte[] bencode(Number n) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(n, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	public void bencode(Number n, OutputStream out) throws IOException {
		out.write(CHAR_I);
		String s = n.toString();
		out.write(s.getBytes(UTF_8));
		out.write(CHAR_E);
	}

	public byte[] bencode(List<Object> list) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(list, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	public void bencode(List<Object> list, OutputStream out) throws IOException {
		out.write(CHAR_L);
		for (Object object : list) {
			bencode(object, out);
		}
		out.write(CHAR_E);
	}

	public byte[] bencode(byte[] bs) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(bs, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	public void bencode(byte[] bs, OutputStream out) throws IOException {
		String l = Integer.toString(bs.length);
		out.write(l.getBytes(UTF_8));
		out.write(CHAR_COLON);
		out.write(bs);
	}

	public byte[] bencode(Map<String, Object> m) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bencode(m, baos);
			return baos.toByteArray();
		} catch (IOException ioe) {
			throw new InternalError(ioe.toString());
		}
	}

	public void bencode(Map<String, Object> m, OutputStream out) throws IOException {
		out.write(CHAR_D);

		// Ключи должны быть упорядочены в лексикографическом порядке.
		Set<String> s = m.keySet();
		List<String> l = new ArrayList<String>(s);
		Collections.sort(l);

		Object value;
		for (String key : l) {
			value = m.get(key);
			bencode(key, out);
			bencode(value, out);
		}
		out.write(CHAR_E);
	}
	
	@SuppressWarnings("unchecked")
	public void bencode(Object o, OutputStream out) throws IOException, IllegalArgumentException {
	 	if (o instanceof String) {
			bencode((String) o, out);
	 	} else if (o instanceof byte[]) {
			bencode((byte[]) o, out);
	 	} else if (o instanceof Number) {
			bencode((Number) o, out);
	 	} else if (o instanceof List) {
			bencode((List<Object>) o, out);
	 	} else if (o instanceof Map) {
			bencode((Map<String, Object>) o, out);
	 	} else { 
			throw new IllegalArgumentException("Ошибка кодирования: " + o.getClass());
	 	}
	}

}
