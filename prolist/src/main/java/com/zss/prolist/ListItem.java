package com.zss.prolist;

import java.io.Serializable;

public class ListItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String itemTitle;
	private Class cls;

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public Class getCls() {
		return cls;
	}

	public void setCls(Class cls) {
		this.cls = cls;
	}

}
