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
@Alias(alias="User")
@Table(name="im_user", catalog = "im")
public class User extends BaseBean{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uid",unique = true, nullable = false)
	private int uid;
	@Column(name = "name", nullable = false, length = 30)
	private String name;
	@Column(name = "password", nullable = false, length = 40)
	private String password;
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User() {
	}
	public User( String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
}
