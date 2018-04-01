package com.xmg.p2p.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.mapper.SystemDictionaryItemMapper;
import com.xmg.p2p.base.mapper.SystemDictionaryMapper;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryQuearyObject;
import com.xmg.p2p.base.service.ISystemDictionaryService;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {

	@Autowired
	private SystemDictionaryMapper systemDictionaryMapper;
	@Autowired
	private SystemDictionaryItemMapper systemDictionaryItemMapper;
	
	
	@Override
	public PageResult queryDics(SystemDictionaryQuearyObject qo) {
		int count = systemDictionaryMapper.queryForCount(qo);
		if (count>0) {
			List<SystemDictionary> list = systemDictionaryMapper.query(qo);
			return new PageResult(list,count, qo.getCurrentPage() , qo.getPageSize());
			
			
		}
				
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void saveorUpdateDic(SystemDictionary dictionary) {
		if (dictionary.getId()!=null) {
			systemDictionaryMapper.updateByPrimaryKey(dictionary);
		}else {
			systemDictionaryMapper.insert(dictionary);
		}
		
	}

	@Override
	public PageResult queryItems(SystemDictionaryQuearyObject qo) {
		int count = systemDictionaryItemMapper.queryForCount(qo);
		if (count>0) {
			List<SystemDictionaryItem> list = systemDictionaryItemMapper.query(qo);
			return new PageResult(list,count, qo.getCurrentPage() , qo.getPageSize());
		}
				
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public List<SystemDictionary> listAllDics() {
		
		return systemDictionaryMapper.selectAll();
	}
	/**
	 * 修改或者保存数据字典明细
	 */
	@Override
	public void saveorUpdateItem(SystemDictionaryItem dictionaryItem) {
		if (dictionaryItem.getId()!=null) {
			systemDictionaryItemMapper.updateByPrimaryKey(dictionaryItem);
		}else {
			systemDictionaryItemMapper.insert(dictionaryItem);
		}
		
	}

	@Override
	public List<SystemDictionaryItem> listByParent(String sn) {
		
		return systemDictionaryItemMapper.listByParentSn(sn);
	}

}
