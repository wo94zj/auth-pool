package com.auth.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ThirdPartyUser implements Serializable {

	private static final long serialVersionUID = -8656270029804300204L;
	
	@JSONField(name = "thirdId")
	private Long id;
	private Long userId;
	private String appname;
	private String openId;
	private String nickname;
	private Integer sex;//性别；0：保密，1：男；2：女
	private String img;//头像
	private String thirdType;//认证机构（ThirdTypeEnums）
	
	@JSONField(serialize = false)
	private Integer status;//0：绑定；1：解绑
	
	private Long updateTime;
	private Long createTime;
}
