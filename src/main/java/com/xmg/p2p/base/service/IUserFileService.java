package com.xmg.p2p.base.service;

import java.util.List;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.query.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;

/**
 * 风控材料服务
 * @author Administrator
 *
 */
public interface IUserFileService {

	/**
	 * 上传一个风控资料
	 * @param filename
	 */
	public void apply(String filename);
	/**
	 * 列出一个用户没有选择风控材料类型的风控资料对象
	 * hashType true  有类型  false 没有类型
	 * @param id
	 * @return
	 */
	public List<UserFile> listFileByHasType(Long logininfoId,boolean hashType);
	
	/**
	 * 批量处理用户风控类型的选择
	 * @param id
	 * @param fileType
	 */
	public void batchUpdateFileType(Long[] id, Long[] fileType);
	
	public PageResult query(UserFileQueryObject qo);
	/**
	 * 审核
	 * @param id
	 * @param score
	 * @param remark
	 * @param state
	 */
	public void audit(Long id, int score, String remark, int state);
	
	List<UserFile> queryForList(UserFileQueryObject qo);
}
