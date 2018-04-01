package com.xmg.p2p.base.service;

import java.util.List;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryQuearyObject;

/**
 * 数据字典相关服务
 * @author Administrator
 *
 */
public interface ISystemDictionaryService {
	/**
	 * 数据字典分类的分页查询
	 * @return
	 */
	 public PageResult queryDics(SystemDictionaryQuearyObject qo);
	 /**
	  * 修改或者保存
	  * @param dictionary
	  */
	public void saveorUpdateDic(SystemDictionary dictionary);
	/**
	 * 数据字典明细的分页查询
	 * 
	 */
	PageResult queryItems(SystemDictionaryQuearyObject qo);
	/**
	 * 查询所有的数据字典明细
	 */
	List<SystemDictionary> listAllDics();
	public void saveorUpdateItem(SystemDictionaryItem dictionaryItem);
	/**
	 * 根据数据字典分类sn查询明细
	 * @param sn
	 * @return
	 */
	List<SystemDictionaryItem> listByParent(String sn);
}
