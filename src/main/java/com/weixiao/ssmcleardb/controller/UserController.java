
package com.weixiao.ssmcleardb.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weixiao.frame.base.BaseAdminController;
import com.weixiao.frame.base.BaseCodeMessage;
import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;
import com.weixiao.ssmcleardb.model.User;
import com.weixiao.ssmcleardb.service.UserManager;

/**
 * @author lipw
 * 表名 user
 * 备注：User
 */
@Controller
@Scope("prototype")
@RequestMapping("/webapi/user")
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class UserController extends BaseAdminController {
	
	@Autowired
	private UserManager userManager;

	@RequestMapping("/index.do")
	public String index() {
		return "/page/user";
	}
	
	/** 
	 * 分页查询列表
	 **/
	@RequestMapping("/findPage.do")
	@ResponseBody
	public void findPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PageRequest<Map> pageRequest = setPageValue(request);
		Map mapFilters = pageRequest.getFilters();// 设置分页，获取查询条件
		Page page = userManager.findByPageRequest(pageRequest);
		this.outPageJson(response, page, true);
	}
	
	/** 
	 * 查询列表
	 **/
	@RequestMapping("/findList.do")
	@ResponseBody
	public void findList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<User> userList = userManager.findBy(SORT_COLUMNS, " id DESC");
		outResultJson(response, userList);
	}
	
	/** 
	 * 保存或更新对象
	 **/
	@RequestMapping("/save.do")
	@ResponseBody
	public 	void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User  entity;
		Long id = this.getLong("id");
		if(id == null){
			entity = new User();
			this.setFieldValues(entity, request, false);
		}else{
			entity = userManager.get(id);
			if(entity == null){
				this.outFailureJson(response);
				return;
			}
			this.setFieldValues(entity, request, true);

		}
		userManager.saveOrUpdate(entity);
		this.outSuccessJson(response);
	}
	
	/**
	 * 删除对象
	 * 这里接受一个名称为“ids”的字符串，id之间用英文半角的逗号“,”分隔。
	 **/
	@RequestMapping("/delete.do")
	@ResponseBody
	public 	void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String ids = get("ids");
		userManager.removeByIds(ids);
	}
	
	/**
	 * 根据ID查询
	 * */
	@RequestMapping("/get.do")
	public String getById(HttpServletRequest request, Model model) throws Exception {
		Long id = this.getLong("id");
		User  entity = null;
		if (id != null){
			entity = userManager.get(id);
		}
		model.addAttribute(entity);
		return "/page/user";
	}
}

