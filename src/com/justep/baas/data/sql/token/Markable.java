package com.justep.baas.data.sql.token;

public interface Markable {
	public int position();
	public void mark(); //标记当前位置
	public void unmark(); //取消标记
	public void back(); //恢复到标记的位置
//	public void forward();
//	public void backward();
	
}
