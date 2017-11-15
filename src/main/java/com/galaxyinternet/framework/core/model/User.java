package com.galaxyinternet.framework.core.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.galaxyinternet.framework.core.utils.DateUtil;
import com.galaxyinternet.framework.core.utils.GSONUtil;

public class User extends BaseUser {
	private static final long serialVersionUID = 1L;

	private int userTzjlSum;
	
	@NotBlank(message="真实姓名不能为空")
	private String realName;// 真实姓名
	@NotBlank(message="登陆名称不能为空")
	private String nickName;// 登录名称
	@NotBlank(message="邮箱不能为空")
	private String email;// 邮箱
	private String mobile;// 手机
	private String telephone;// 座机
	private String status;// 账户状态 正常 禁用
	private String type;// 账户类型
	@NotBlank(message="工号不能为空")
	private String employNo;// 工号
	@NotNull(message="部门id不能为空")
	private Long departmentId;// 部门Id
	
	private String departmentName;// 部门名称

	private String role;// 角色
	private String password;// 密码
	private String originPassword;// 初始密码
	private Boolean gender;// 性别
	private Date birth;// 生日
	private String birthStr;
	
	private String address;// 地址
	private Boolean isAdmin;// 是否管理员
	
	private boolean isCurrentUser;
	
	private String companyId;
	
	private String aclient;
	
	private long idstr;
	public boolean isCurrentUser() {
		return isCurrentUser;
	}

	public void setCurrentUser(boolean isCurrentUser) {
		this.isCurrentUser = isCurrentUser;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	@NotNull(message="角色不能为空")
	private Long roleId;// 角色Id
	private List<Long> roleIds;

	private List<Long> ids;
	private String nameMbLike;
	
	//部门Ids
	private List<Long> departmentIds;
	
	public List<Long> getDepartmentIds() {
		return departmentIds;
	}
	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public String getBirthStr() {
		return birthStr;
	}

	public void setBirthStr(String birthStr) {
		this.birthStr = birthStr;
	}

	public String getNameMbLike() {
		return nameMbLike;
	}

	public void setNameMbLike(String nameMbLike) {
		this.nameMbLike = nameMbLike;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOriginPassword() {
		return originPassword;
	}

	public void setOriginPassword(String originPassword) {
		this.originPassword = originPassword;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public Date getBirth() {
		if (StringUtils.isNotBlank(this.getBirthStr())) {
			try {
				this.setBirth(DateUtil.convertStringToDate(this.getBirthStr()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public String getEmployNo() {
		return employNo;
	}

	public void setEmployNo(String employNo) {
		this.employNo = employNo;
	}
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public int getUserTzjlSum() {
		return userTzjlSum;
	}

	public void setUserTzjlSum(int userTzjlSum) {
		this.userTzjlSum = userTzjlSum;
	}

	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		if(id!=null){
			this.idstr = id.longValue();
		}
		this.id = id;
	}
	
	public long getIdstr() {
		return idstr;
	}

	public void setIdstr(long idstr) {
		if(this.id!=null){
			this.idstr = this.id.longValue();
		}else this.idstr = idstr;
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<Long> getRoleIds()
	{
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds)
	{
		this.roleIds = roleIds;
	}
	
	

	public String getAclient() {
		return aclient;
	}

	public void setAclient(String aclient) {
		this.aclient = aclient;
	}

	@Override
	public String toString() {
		return GSONUtil.toJson(this);
	}
}
