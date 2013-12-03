package com.athudong.psr.view.inter;

public interface WheelAdapter {
	
	/**
	 * ��ȡ��ʾ����Ŀ
	 * @return wheel(�����ؼ�)��ʾ����Ŀ
	 */
	public int getItemsCount();
	
	/**
	 * ���� �±��ȡitem
	 * @param index item���±�
	 * @return ����Wheel item���ı���null
	 */
	public String getItem(int index);
	
	/**
	 * ��ȡ���ĳ��� , ����ȷ��wheel�Ŀ��
	 * �������-1 ��ʹ��Ĭ�ϵ�wheel���
	 * @return ���ĳ���-1
	 */
	public int getMaximumLength();
}
