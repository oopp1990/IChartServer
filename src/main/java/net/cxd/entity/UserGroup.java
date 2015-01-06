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
@Alias(alias="UserGroup")
@Table(name = "im_user_group", catalog = "im")
public class UserGroup extends BaseBean{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique = true, nullable = false)
	private int id;
	private int uid;
	private int gid;//组ID

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

}
