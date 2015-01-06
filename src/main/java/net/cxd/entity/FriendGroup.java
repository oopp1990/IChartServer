package net.cxd.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cq.base.annotation.Alias;
import cq.base.entity.BaseBean;

@Entity
@Alias(alias="FriendGroup")
@Table(name = "im_friend_group", catalog = "im")
public class FriendGroup extends BaseBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "uid", nullable = false, length = 15)
	private int uid;
	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Transient
	private List<UserFriend> friends;
	
	
	public List<UserFriend> getFriends() {
		return friends;
	}
	public void setFriends(List<UserFriend> friends) {
		this.friends = friends;
	}
	public FriendGroup(int uid, String name) {
		this.uid = uid;
		this.name = name;
	}
	public FriendGroup() {
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
