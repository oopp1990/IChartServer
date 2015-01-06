package net.cxd.util;

public class MsgType {
	// HART_BAT(0), LOGIN(1), ERROR(2), TXT(10), IMG(11), AUDEO(12), FILE(13),
	// USERMSG(
	// 21), GROUPMSG(22), SYSTEMMSG(23);
	public static final int HART_BAT = 0;
	public static final int LOGIN = 1;
	public static final int ERROR = 2;
	public static final int TXT = 10;
	public static final int IMG = 11;
	public static final int AUDEO = 12;
	public static final int FILE = 13;
	public static final int USERMSG = 21;
	public static final int GROUPMSG = 22;
	public static final int SYSTEMMSG = 23;

	private int type;

	MsgType(Integer type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static int getMsgType(int type) {
		switch (type) {
		case 0:
			return HART_BAT;
		case 21:
			return USERMSG;
		case 22:
			return GROUPMSG;
		case 23:
			return SYSTEMMSG;
		case 10:
			return TXT;
		case 11:
			return IMG;
		case 12:
			return AUDEO;
		case 13:
			return FILE;
		default:
			break;
		}
		return type;
	}
}
