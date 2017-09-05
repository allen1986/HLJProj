package com.hlj.entity;

import java.util.Date;

public class TbFile {

	private int id;
	private String regionId;
	private String fileName;
	private String creatorName;
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "TbFile [id=" + id + ", regionId=" + regionId + ", fileName=" + fileName + ", creatorName=" + creatorName
				+ ", createTime=" + createTime + "]";
	}

}
