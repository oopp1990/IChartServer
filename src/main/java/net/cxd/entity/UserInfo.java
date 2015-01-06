package net.cxd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cq.base.annotation.Alias;
import cq.base.entity.BaseBean;

@Entity()
@Alias(alias = "UserInfo")
@Table(name = "im_user_info", catalog = "im")
public class UserInfo extends BaseBean {
	@Id
	@Column(name = "uid", unique = true, nullable = false)
	private int uid;
	@Column(name = "name", nullable = false, length = 20)
	private String name;// 昵称
	@Column(name = "level", nullable = false, length = 3)
	private Integer level ;
	@Column(name = "photo_file")
	private String photoFile;
	@Column(name = "signature")
	private String signature;// 心情签名
	@Column(name = "vipLevel", nullable = false, length = 2)
	private Integer vipLevel ;
	@Column(name = "phone_num")
	private String phoneNum;
	@Column(name = "email")
	private String email;
	@Column(name = "address")
	private String address;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
