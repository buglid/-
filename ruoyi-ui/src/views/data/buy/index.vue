<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="地区" prop="areaId">
        <el-cascader
            v-model="address"
            :options="options"
            clearable
            @change="handleChange">
          </el-cascader>
      </el-form-item>
      <el-form-item label="时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="标准化类型" prop="num">
            <el-select
              v-model="queryParams.type"
              placeholder="类型"
              clearable
              style="width: 240px"
            >
              <el-option label="Z-Score标准化" value="1"/>
              <el-option label="0-1标准化" value="2"/>
            </el-select>
          </el-form-item>
    
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">开始标准化</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:pollution:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:pollution:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:pollution:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:pollution:export']"
        >导出原始数据</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport2"
          v-hasPermi="['system:pollution:export']"
        >导出标准化数据</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表信息------------------------------------------------------>

    <el-table v-loading="loading" :data="pollutionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="地区" align="center" prop="areaId">
          <template slot-scope="scope">
              <span>{{ cityName(scope.row.areaId) }}</span>
          </template>
      </el-table-column>
      <el-table-column label="日期" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="PM2.5" align="center" prop="pm25" />
      <el-table-column label="PM10" align="center" prop="pm10" />
      <el-table-column label="SO2" align="center" prop="so2" />
      <el-table-column label="NO2" align="center" prop="no2" />
      <el-table-column label="CO" align="center" prop="co" />
      <el-table-column label="O3" align="center" prop="o3" />
      <el-table-column label="入库日期" align="center" prop="enterDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.enterDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户" align="center" prop="userId" />
      <el-table-column label="备注" align="center" prop="remarks" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:pollution:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:pollution:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="areaId">
          <el-input v-model="form.areaId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="Date">
          <el-date-picker clearable
            v-model="form.Date"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择${comment}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="${comment}" prop="pm25">
          <el-input v-model="form.pm25" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="PM10">
          <el-input v-model="form.PM10" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="SO2">
          <el-input v-model="form.SO2" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="NO2">
          <el-input v-model="form.NO2" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="CO">
          <el-input v-model="form.CO" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="O3">
          <el-input v-model="form.O3" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="enterDate">
          <el-date-picker clearable
            v-model="form.enterDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择${comment}">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="${comment}" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入${comment}" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPollution, getPollution, delPollution, addPollution, updatePollution,cluster,standardization } from "@/api/system/pollution";
import {citys,filterCitys} from '../../../assets/city'
export default {
  name: "Pollution",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      pollutionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        areaId: null,
        Date: null,
        pm25: null,
        PM10: null,
        SO2: null,
        NO2: null,
        CO: null,
        O3: null,
        enterDate: null,
        userId: null,
        remarks: null,
        type:"1"
      },
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      options:{},
      address:''
    };
  },
  created() {
    this.options = citys
    this.getList();
    // const cityArr = []
    // citys.forEach(a=>{
    //   cityArr.push({
    //     label:a.label,
    //     value:a.value
    //   })
    //   if(a.children.length>0){
    //     a.children.forEach(b=>{
    //         cityArr.push({
    //           label:b.label,
    //           value:b.value
    //         })
    //       if(b.children.length>0){
    //         b.children.forEach(c=>{
    //           cityArr.push({
    //             label:c.label,
    //             value:c.value
    //           })
    //         })
    //       }
    //     })
    //   }
    // })
    // console.log('@citys',JSON.stringify(cityArr))
  },
  computed:{
    cityName(){
      return (code)=>{
        const item = filterCitys.find(x=>x.city_code==code);
        const name = item?item.city_name:'';
        return name
      }
    }
  },
  methods: {
    handleChange(){

    },
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      const p = {
        ...this.queryParams,
        areaId:this.address[1],
        beginTime:this.dateRange[0],
        endTime:this.dateRange[1],
      }
      listPollution(p).then(response => {
        this.pollutionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        areaId: null,
        Date: null,
        pm25: null,
        PM10: null,
        SO2: null,
        NO2: null,
        CO: null,
        O3: null,
        enterDate: null,
        userId: null,
        remarks: null,
        type:1
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      // this.getList();
      if(!this.address||this.address.length<=0){
        this.$modal.msgWarning("请选择城市");
        return
      }
      if(!this.dateRange||this.dateRange.length<=0){
        this.$modal.msgWarning("请选择开始和结束日期");
        return
      }
      const p = {
        ...this.queryParams,
        areaId:this.address[1],
        beginTime:this.dateRange[0],
        endTime:this.dateRange[1],
      }
      p.pageNum = 1
      p.pageSize = 10000
      console.log("@queryParams",p)
      this.loading = true;
      standardization(p).then(response => {
        response.rows.forEach(item=>{
          item.no2 = item.params.no2;
          item.pm25 = item.params.pm25;
          item.o3 = item.params.o3;
          item.so2 = item.params.so2;
          item.pm10 = item.params.pm10;
          item.co = item.params.co;
        });
        this.pollutionList = response.rows;
        this.total = response.total;
        this.queryParams.pageSize = response.total

        this.loading = false;
      });
      // this.$router.push({name:'BoxDiagram',params:this.addDateRange(p, this.dateRange)})
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加【请填写功能名称】";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPollution(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改【请填写功能名称】";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePollution(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPollution(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除【请填写功能名称】编号为"' + ids + '"的数据项？').then(function() {
        return delPollution(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const p = {
        ...this.queryParams,
        areaId:this.address[1],
        beginTime:this.dateRange[0],
        endTime:this.dateRange[1],
      }
      this.download('system/pollution/export', {
        ...p
      }, `pollution_${new Date().getTime()}.xlsx`)
    },
    handleExport2() {
      const p = {
        ...this.queryParams,
        areaId:this.address[1],
        beginTime:this.dateRange[0],
        endTime:this.dateRange[1],
      }
      this.download('system/pollution/exportstandardization', {
        ...p
      }, `pollution_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
