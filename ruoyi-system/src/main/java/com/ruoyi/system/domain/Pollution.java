package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 pollution
 * 
 * @author ruoyi
 * @date 2023-03-16
 */
public class Pollution extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "城市编码")
    private Long areaId;

    /** $column.columnComment */
    @Excel(name = "日期")
    private Date Date;

    /** $column.columnComment */
    @Excel(name = "PM2.5")
    private String pm25;

    /** $column.columnComment */
    @Excel(name = "PM10")
    private String PM10;

    /** $column.columnComment */
    @Excel(name = "SO2")
    private String SO2;

    /** $column.columnComment */
    @Excel(name = "NO2")
    private String NO2;

    /** $column.columnComment */
    @Excel(name = "CO")
    private String CO;

    /** $column.columnComment */
    @Excel(name = "O3")
    private String O3;

    /** $column.columnComment */
    @Excel(name = "入库日期")
    private Date enterDate;

    /** $column.columnComment */
    private Long userId;

    /** $column.columnComment */
    private String remarks;
    private String lamb;


    private String eps;

    private String min_samples;


    private String lmbda;


    public String getMin_samples() {
        return min_samples;
    }

    public void setMin_samples(String min_samples) {
        this.min_samples = min_samples;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getLmbda() {
        return lmbda;
    }

    public void setLmbda(String lmbda) {
        this.lmbda = lmbda;
    }

    public String getLamb() {
        return lamb;
    }

    public void setLamb(String lamb) {
        this.lamb = lamb;
    }



    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAreaId(Long areaId) 
    {
        this.areaId = areaId;
    }

    public Long getAreaId() 
    {
        return areaId;
    }
    public void setDate(Date Date) 
    {
        this.Date = Date;
    }

    public Date getDate() 
    {
        return Date;
    }
    public void setPm25(String pm25)
    {
        this.pm25 = pm25;
    }

    public String getPm25()
    {
        return pm25;
    }
    public void setPM10(String PM10)
    {
        this.PM10 = PM10;
    }

    public String getPM10()
    {
        return PM10;
    }
    public void setSO2(String SO2)
    {
        this.SO2 = SO2;
    }

    public String getSO2()
    {
        return SO2;
    }
    public void setNO2(String NO2)
    {
        this.NO2 = NO2;
    }

    public String getNO2()
    {
        return NO2;
    }
    public void setCO(String CO)
    {
        this.CO = CO;
    }

    public String getCO()
    {
        return CO;
    }
    public void setO3(String O3)
    {
        this.O3 = O3;
    }

    public String getO3()
    {
        return O3;
    }
    public void setEnterDate(Date enterDate) 
    {
        this.enterDate = enterDate;
    }

    public Date getEnterDate() 
    {
        return enterDate;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("areaId", getAreaId())
            .append("Date", getDate())
            .append("pm25", getPm25())
            .append("PM10", getPM10())
            .append("SO2", getSO2())
            .append("NO2", getNO2())
            .append("CO", getCO())
            .append("O3", getO3())
            .append("enterDate", getEnterDate())
            .append("userId", getUserId())
            .append("remarks", getRemarks())
            .append("lamb",getLamb())
            .toString();
    }


}
