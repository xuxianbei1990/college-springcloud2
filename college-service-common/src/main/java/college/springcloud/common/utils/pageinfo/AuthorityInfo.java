package college.springcloud.common.utils.pageinfo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/9/22
 * Time: 13:55
 * Version:V1.0
 */
public interface AuthorityInfo {

    /**
     * 设置租户名称
     *
     * @param tenantId
     */
    default void setTenantId(Long tenantId) {

    }

    /**
     * 公司
     *
     * @param companyId
     */
    default void setCompanyId(Long companyId) {

    }

    /**
     * 创建用户id
     */
    Long getCreateBy();

    /**
     * 设置创建用户名称
     */
    String getCreateName();

    /**
     * 设置创建时间
     */
    default LocalDateTime getCreateDate() {
        return LocalDateTime.now();
    }


    /**
     * 更新用户ID
     */
    default Long getUpdateBy() {
        return getCreateBy();
    }

    /**
     * 更新用户人
     */
    default String getUpdateName() {
        return getCreateName();
    }

    /**
     * 更新时间
     */
    default LocalDateTime getUpdateDate() {
        return LocalDateTime.now();
    }

    /**
     * 设置创建用户id
     *
     * @param userId
     */
    void setCreateBy(Long userId);

    /**
     * 设置创建用户名称
     *
     * @param createName
     */
    void setCreateName(String createName);

    /**
     * 设置创建时间
     *
     * @param localDateTime
     */
    default void setCreateDate(LocalDateTime localDateTime) {

    }


    /**
     * 设置创建时间
     *
     * @param createDate
     */
    default void setCreateDate(Date createDate) {

    }

    /**
     * 更新用户ID
     *
     * @param userId
     */
    default void setUpdateBy(Long userId) {

    }

    /**
     * 更新用户人
     *
     * @param updateName
     */
    default void setUpdateName(String updateName) {

    }

    /**
     * 更新时间
     *
     * @param updateDate
     */
    default void setUpdateDate(LocalDateTime updateDate) {

    }

}
