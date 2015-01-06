package net.cxd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cq.base.annotation.Alias;
import cq.base.entity.BaseBean;
/**
 * 好友和组多对多关联表
 * @author oopp1990
 *
 */
@Entity
@Alias(alias="UserFriend")
@Table(name="im_user_friend", catalog = "im")
public class UserFriend extends BaseBean{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true,nullable=false)
	private int id;
	@Column(name = "gid", nullable = false, length = 20)
	private int gid;//关联分组id
	@Column(name = "uid", nullable = false, length = 20)
	private int uid;//关联帐号
	
	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public UserFriend() {
		// TODO Auto-generated constructor stub
	}
	public UserFriend(int gid, int uid) {
		super();
		this.gid = gid;
		this.uid = uid;
	}
}
