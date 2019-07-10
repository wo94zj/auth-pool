package com.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.auth.bean.ThirdPartyUser;

@Mapper
public interface ThirdPartyUserMapper {

	/**
	 * 主键冲突返回0
	 */
	@Insert("INSERT IGNORE INTO mc_user_third_bind(appname,third_type,user_id,open_id,nickname,sex,img,status,update_time,create_time) "
			+ "VALUES(#{appname},#{thirdType},#{userId},#{openId},#{nickname},#{sex},#{img},0,#{updateTime},#{createTime})")
	@Options(useGeneratedKeys = true)
	int insertThirdPartyUser(ThirdPartyUser user);
	
	//appname_thirdType_userId_unique_index\appname_thirdType_openId_unique_index
	@Insert("<script>"
			+ "INSERT INTO mc_user_third_bind(appname,third_type,user_id,open_id,nickname,sex,img,status,update_time,create_time) "
			+ "VALUES(#{appname},#{thirdType},#{userId},#{openId},#{nickname},#{sex},#{img},0,#{updateTime},#{createTime}) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "<if test='userId!=null'>user_id=#{userId},</if>"
			+ "<if test='openId!=null'>open_id=#{openId},</if>"
			+ "nickname=#{nickname},sex=#{sex},img=#{img},status=0,update_time=#{updateTime}"
			+ "</script>")
	@Options(useGeneratedKeys = true)
	int insertOrUpdate(ThirdPartyUser user);
	
	@Select("SELECT * FROM mc_user_third_bind WHERE appname=#{appname} AND user_id=#{userId} AND status=0")
	List<ThirdPartyUser> selectBindsByUserId(@Param("appname")String appname, @Param("userId")Long userId);
	
	@Select("SELECT * FROM mc_user_third_bind WHERE appname=#{appname} AND third_type=#{thirdType} AND open_id=#{openId} AND status=0")
	ThirdPartyUser selectBindsByOpenId(@Param("appname")String appname, @Param("thirdType")String thirdType, @Param("openId")String openId);
	
	/**
	 * 解绑
	 */
	@Update("UPDATE xw_user_third_bind SET status=1 WHERE appname=#{appname} AND user_id=#{userId} AND third_type=#{thirdType}")
	int updateStatus(@Param("appname")String appname, @Param("userId")Long userId, @Param("thirdType")String thirdType);
}
