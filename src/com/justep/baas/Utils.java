package com.justep.baas;

public class Utils {
	public static String getLineSep() {
		return System.getProperty("line.separator");
	}
	
	public static void check(boolean isOK, Object msg1) {
		if (!isOK) {
			BaasException exp = new BaasException("" + msg1);
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4, Object msg5) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4, msg5));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4, Object msg5, Object msg6) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4, msg5, msg6));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4, Object msg5, Object msg6, Object msg7) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4, msg5, msg6, msg7));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4, Object msg5, Object msg6, Object msg7, Object msg8) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4, msg5, msg6, msg7, msg8));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4, Object msg5, Object msg6, Object msg7, Object msg8,
			Object msg9) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4, msg5, msg6, msg7, msg8, msg9));
			throw exp;
		}
	}

	public static void check(boolean isOK, Object msg1, Object msg2, Object msg3, Object msg4, Object msg5, Object msg6, Object msg7, Object msg8,
			Object msg9, Object msg10) {
		if (!isOK) {
			BaasException exp = new BaasException(buildString(msg1, msg2, msg3, msg4, msg5, msg6, msg7, msg8, msg9, msg10));
			throw exp;
		}
	}

	private static String buildString(Object... objs) {
		StringBuilder sb = new StringBuilder();
		for (Object o : objs)
			sb.append(o);
		return sb.toString();
	}

	public static boolean isNotEmptyString(String obj) {
		return obj != null && obj.trim().length() != 0;
	}

	public static boolean isEmptyString(String obj) {
		return null == obj || "".equals(obj);
	}

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isNotNull(Object obj) {
		return obj != null;
	}
}
