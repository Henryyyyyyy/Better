package com.example.lib_db.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Stay
 * @version create time：Mar 11, 2014 6:52:59 PM
 */
public class SerializeUtil {
	public static Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ObjectInputStream objInStream = null;
		try {
			objInStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			return objInStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (objInStream != null) {
				try {
					// we do this to give GC a hand with ObjectInputStream
					// reference maps
					objInStream.close();
				} catch (IOException e) {
					// ignored
				}
			}
		}
	}

	public static byte[] serialize(Object obj) {
		if (obj == null) {
			return null;
		}
		ObjectOutputStream objOutStream = null;
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			objOutStream = new ObjectOutputStream(outStream);
			objOutStream.writeObject(obj);
			objOutStream.close();
			objOutStream = null;
			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (objOutStream != null) {
				try {
					// we do this to give GC a hand with ObjectOutputStream
					// reference maps
					objOutStream.close();
				} catch (IOException e) {
					// ignored
				}
			}
		}
	}
}
