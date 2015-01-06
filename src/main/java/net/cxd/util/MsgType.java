package net.cxd.util;

public enum MsgType {
	HART_BAT(0),ERROR(1), TXT(10), IMG(11), AUDEO(12), FILE(13), USERMSG(21), GROUPMSG(
			22), SYSTEMMSG(23);
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

	public static MsgType getMsgType(int type) {
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
		return null;
	}
}
