package com.ruoyi.system.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;

import com.ruoyi.system.algorithm.sm4.SM4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PollutionMapper;
import com.ruoyi.system.domain.Pollution;
import com.ruoyi.system.service.IPollutionService;

import javax.validation.Validator;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-16
 */
@Service
public class PollutionServiceImpl implements IPollutionService {
    private static final Logger log = LoggerFactory.getLogger(PollutionServiceImpl.class);
    @Autowired
    private PollutionMapper pollutionMapper;

    @Autowired
    protected Validator validator;

    @Autowired
    private SM4 sm4;


    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Pollution selectPollutionById(Long id) {
        Pollution pollution1 = pollutionMapper.selectPollutionById(id);
        try{
            pollution1.setPm25(sm4.decrypt(pollution1.getPm25()));
            pollution1.setPM10(sm4.decrypt(pollution1.getPM10()));
            pollution1.setSO2(sm4.decrypt(pollution1.getSO2()));
            pollution1.setNO2(sm4.decrypt(pollution1.getNO2()));
            pollution1.setCO(sm4.decrypt(pollution1.getCO()));
            pollution1.setO3(sm4.decrypt(pollution1.getO3()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return pollution1;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param pollution 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Pollution> selectPollutionList(Pollution pollution) {
        List<Pollution> pollutionList=pollutionMapper.selectPollutionList(pollution);
        for(Pollution pollution1 : pollutionList){
            try{
                pollution1.setPm25(sm4.decrypt(pollution1.getPm25()));
                pollution1.setPM10(sm4.decrypt(pollution1.getPM10()));
                pollution1.setSO2(sm4.decrypt(pollution1.getSO2()));
                pollution1.setNO2(sm4.decrypt(pollution1.getNO2()));
                pollution1.setCO(sm4.decrypt(pollution1.getCO()));
                pollution1.setO3(sm4.decrypt(pollution1.getO3()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return pollutionList;
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param pollution 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertPollution(Pollution pollution) {
        try{
//            pollution.setDate(sm4Salt.addSaltEncryption(date));
            pollution.setPm25(sm4.encrypt(pollution.getPm25()));
            pollution.setPM10(sm4.encrypt(pollution.getPM10()));
            pollution.setSO2(sm4.encrypt(pollution.getSO2()));
            pollution.setNO2(sm4.encrypt(pollution.getNO2()));
            pollution.setCO(sm4.encrypt(pollution.getCO()));
            pollution.setO3(sm4.encrypt(pollution.getO3()));


        }catch (Exception e){
            e.printStackTrace();
        }

        return pollutionMapper.insertPollution(pollution);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param pollution 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updatePollution(Pollution pollution) {


        try{
            pollution.setPm25(sm4.decrypt( pollution.getPm25()));
            pollution.setPM10(sm4.decrypt( pollution.getPM10()));
            pollution.setSO2(sm4.decrypt( pollution.getSO2()));
            pollution.setNO2(sm4.decrypt( pollution.getNO2()));
            pollution.setCO(sm4.decrypt( pollution.getCO()));
            pollution.setO3(sm4.decrypt( pollution.getO3()));

//            String pm25=sm4.decrypt(pollution.getPm25());
//            pollution.setPm25(pm25);
//            String  pm2_5 = sm4.encrypt(pollution.getPm25());
//            pollution.setPm25(pm2_5);

        }catch(Exception e){
            e.printStackTrace();
        }


        return pollutionMapper.updatePollution(pollution);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePollutionByIds(Long[] ids) {
        return pollutionMapper.deletePollutionByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deletePollutionById(Long id) {
        return pollutionMapper.deletePollutionById(id);
    }

    @Override
    public String importPillution(List<Pollution> pollutionList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(pollutionList) || pollutionList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Pollution user : pollutionList) {
            try {
                // 验证是否存在这个用户
                if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    user.setUpdateBy(operName);
                    this.insertPollution(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、数据： " + user.getId() + " 更新成功");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、数据 " + user.getId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
