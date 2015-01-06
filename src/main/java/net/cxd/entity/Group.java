package net.cxd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cq.base.annotation.Alias;
import cq.base.entity.BaseBean;

@Entity()
@Alias(alias="Group")
@Table(name = "im_group", catalog = "im")
public class Group extends BaseBean{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique = true, nullable = false)
	private int id;
	@Column(name = "auth_num", nullable = false, length = 15)
	private int authId;// 创建者id
	private String name;// 名字
	@Column(name = "user_num", nullable = false, length = 3)
	private int userNum;// 用户人数
	@Column(name = "photo_file", length = 3)
	private String photoFile;// 群相片
	@Column(name = "max_num", nullable = false, length = 3)
	private int maxNum;// 最大人数

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAuthId() {
		return authId;
	}

	public void setAuthId(int authId) {
		this.authId = authId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public String getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}
	public Group() {
	}
	public Group( int authId, String name) {
		super();
		this.authId = authId;
		this.name = name;
	}
	
}
