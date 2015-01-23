package ua.com.alexandr.bencode;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.com.alexandr.bencode.BEncoderConstants.*;

public class BDecoder {
	private final InputStream in;
	private int marker = 0;

	public BDecoder(InputStream in) {
		this.in = in;
	}

	public int getNextMarker() throws IOException {
		if (marker == 0) {
			marker = in.read();
		}
		return marker;
	}

	public Object decode() throws IOException {
		marker = getNextMarker();
		if (marker == -1) {
			return null;
		}
		
		if (marker >= CHAR_0 && marker <= CHAR_9) {
			return decodeString();
		} else if (marker == CHAR_I) {
			return decodeNumber();
		} else if (marker == CHAR_L) {
			return decodeList();
		} else if (marker == CHAR_D) {
			return decodeMap();
		} else {
			throw new InvalidBEncodingException("Неизвестный маркер: '" + marker + "'");
		}
	}

	public String decodeString() throws IOException {
		int c = getNextMarker();
		int num = c - CHAR_0;
		if (num < 0 || num > 9) {
			throw new InvalidBEncodingException("Ожидалось число: '" + (char) c + "'");
		}
		marker = 0;

		c = read();
		int i = c - CHAR_0;
		while (i >= 0 && i <= 9) {
			num = num * 10 + i;
			c = read();
			i = c - CHAR_0;
		}

		if (c != CHAR_COLON) {
			throw new InvalidBEncodingException("Ожидалось двоеточие: '" + (char) c + "'");
		}
		return new String(read(num), UTF_8);
	}

	public Number decodeNumber() throws IOException {
		int c = getNextMarker();
		if (c != CHAR_I) {
			throw new InvalidBEncodingException("Ожидался маркер '" + CHAR_I + "': '" + (char) c + "'");
		}
		marker = 0;

		c = read();
		if (c == CHAR_0) {
			c = read();
			if (c == CHAR_E) {
				return 0;
			} else {
				throw new InvalidBEncodingException("Маркер 'e' ожидался после нуля: '" + (char) c + "'");
			}
		}

		char[] chars = new char[256];
		int off = 0;

		if (c == CHAR_MINUS) {
			c = read();
			if (c == CHAR_0) {
				throw new InvalidBEncodingException("Отрицательный ноль не разрешен");
			}
			chars[off] = CHAR_MINUS;
			off++;
		}

		if (c < CHAR_1 || c > CHAR_9) {
			throw new InvalidBEncodingException("Неизвестное начало числа: '" + (char) c + "'");
		}
		chars[off] = (char) c;
		off++;

		c = read();
		int i = c - CHAR_0;
		while (i >= 0 && i <= 9) {
			chars[off] = (char) c;
			off++;
			c = read();
			i = c - CHAR_0;
		}

		if (c != CHAR_E)
			throw new InvalidBEncodingException("Число должно заканчиваться маркером '" + CHAR_E + "'");

		String s = new String(chars, 0, off);
		return new BigInteger(s).intValue();
	}

	public List<Object> decodeList() throws IOException {
		int c = getNextMarker();
		if (c != CHAR_L) {
			throw new InvalidBEncodingException("Ожидался маркер '" + CHAR_L + "': '" + (char) c + "'");
		}
		marker = 0;

		List<Object> result = new ArrayList<Object>();
		c = getNextMarker();
		while (c != CHAR_E) {
			Object o=decode();
			result.add(o);
			c = getNextMarker();
		}
		marker = 0;

		return result;
	}

	public Map<String, Object> decodeMap() throws IOException {
		int c = getNextMarker();
		if (c != CHAR_D) {
			throw new InvalidBEncodingException("Ожидался маркер '" + CHAR_D + "': '" + (char) c + "'");
		}
		marker = 0;

		Map<String, Object> result = new HashMap<String, Object>();
		c = getNextMarker();
		String key;
		Object value;
		while (c != CHAR_E) {
			// Ключи могут быть только строкой
			key = (String) decode();
			value = decode();
			result.put(key, value);
			c = getNextMarker();
		}
		marker = 0;

		return result;
	}

	private int read() throws IOException {
		int c = in.read();
		if (c == -1) {
			throw new EOFException();
		}
		return c;
	}

	private byte[] read(int length) throws IOException {
		byte[] result = new byte[length];

		int read = 0;
		while (read < length) {
			int i = in.read(result, read, length - read);
			if (i == -1) {
				throw new EOFException();
			}
			read += i;
		}

		return result;
	}
	
}