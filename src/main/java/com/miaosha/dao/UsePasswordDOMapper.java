package com.miaosha.dao;

import com.miaosha.dataobject.UsePasswordDO;
import org.springframework.stereotype.Repository;


@Repository
public interface UsePasswordDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 20 17:47:09 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 20 17:47:09 CST 2020
     */
    int insert(UsePasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 20 17:47:09 CST 2020
     */
    int insertSelective(UsePasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 20 17:47:09 CST 2020
     */
    UsePasswordDO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 20 17:47:09 CST 2020
     */
    int updateByPrimaryKeySelective(UsePasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 20 17:47:09 CST 2020
     */
    int updateByPrimaryKey(UsePasswordDO record);
}