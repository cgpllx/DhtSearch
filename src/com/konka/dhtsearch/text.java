package com.konka.dhtsearch;

public class text {
	public static void main(String[] args) {
		String dd="{a={id=12345, target=dddd)}, q=find_node, t=tt, v=LT, y=q}";
		System.out.println(dd);
		System.out.println(dd.replace("{", "{\"").replace("=", "\"=\"").replace(",", "\",\""));
		String ddd="{\"a\"=\"{id=12345, target=dddd)}\", \"q\"=\"find_node\", \"t\"=\"tt\", \"v\"=\"LT\", \"y\"=\"q\"}";
		System.out.println(ddd);
	}
}
