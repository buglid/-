package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.Pollution;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2023-03-16
 */
public interface IPollutionService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Pollution selectPollutionById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param pollution 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Pollution> selectPollutionList(Pollution pollution);

    /**
     * 新增【请填写功能名称】
     * 
     * @param pollution 【请填写功能名称】
     * @return 结果
     */
    public int insertPollution(Pollution pollution);

    /**
     * 修改【请填写功能名称】
     * 
     * @param pollution 【请填写功能名称】
     * @return 结果
     */
    public int updatePollution(Pollution pollution);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deletePollutionByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deletePollutionById(Long id);


    /**
     * 导入数据
     *
     * @param pollutionList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importPillution(List<Pollution> pollutionList, Boolean isUpdateSupport, String operName);
}
