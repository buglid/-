<template>
  <div class="app-container">
    <el-form :model="queryForm" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="50px" label-position="left">
      <el-form-item label="地区" prop="areaId">
        <el-cascader
            v-model="queryForm.address"
            :options="options"
            clearable
            @change="handleChange">
          </el-cascader>
      </el-form-item>
      <el-form-item label="时间" style="margin-left: 20px;">
        <el-date-picker
          v-model="queryForm.dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="网址" prop="num" style="display: block;">
        <el-input
            style="width: 250px;"
            placeholder="请输入网址"
            v-model="queryForm.webSite"
            :disabled="true"
            clearable>
        </el-input>
      </el-form-item>

      <el-form-item label="" style="display: block;">
        <el-input
          style="width: 860px"
          type="textarea"
          :rows="20"
          placeholder="请输入python代码"
          v-model="queryForm.pythonCode">
        </el-input>
      </el-form-item>
    
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">开始爬取</el-button>
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
            type="info"
            plain
            icon="el-icon-upload2"
            size="mini"
            @click="handleImport"
            v-hasPermi="['system:pollution:import']"
          >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:pollution:export']"
        >导出</el-button>
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
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="地区" prop="areaId">
          <el-cascader
            v-model="formAddress"
            :options="options"
            clearable
            @change="handleChange">
          </el-cascader>
        </el-form-item>
        <el-form-item label="日期" prop="Date">
          <el-date-picker clearable
            v-model="form.date"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="PM2.5" prop="pm25">
          <el-input v-model="form.pm25" placeholder="请输入PM2.5" />
        </el-form-item>
        <el-form-item label="PM10" prop="PM10">
          <el-input v-model="form.pm10" placeholder="请输入PM10" />
        </el-form-item>
        <el-form-item label="SO2" prop="SO2">
          <el-input v-model="form.so2" placeholder="请输入SO2" />
        </el-form-item>
        <el-form-item label="NO2" prop="NO2">
          <el-input v-model="form.no2" placeholder="请输入NO2" />
        </el-form-item>
        <el-form-item label="CO" prop="CO">
          <el-input v-model="form.co" placeholder="请输入CO" />
        </el-form-item>
        <el-form-item label="O3" prop="O3">
          <el-input v-model="form.o3" placeholder="请输入O3" />
        </el-form-item>
        <!-- <el-form-item label="${comment}" prop="enterDate">
          <el-date-picker clearable
            v-model="form.enterDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择${comment}">
          </el-date-picker>
        </el-form-item> -->
        <!-- <el-form-item label="${comment}" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入${comment}" />
        </el-form-item> -->
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPollution, getPollution, delPollution, addPollution, updatePollution,cluster,craw } from "@/api/system/pollution";
import {citys,filterCitys} from '../../../assets/city'
import {pyCode} from '../../../assets/py'
import { getToken } from "@/utils/auth";
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
        num:1
      },
     
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      options:{},
      queryForm:{ 
        dateRange: [],// 日期范围
        webSite:'http://www.tianqihoubao.com/api',
        pythonCode:'',
        address:'',
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（导入大气污染数据）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 1,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/pollution/importData"
      },
      formAddress:[],
    };
  },
  created() {
    this.options = citys
    this.queryForm.pythonCode = pyCode
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
      listPollution(this.queryParams).then(response => {
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
        num:1
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      // this.getList();
      if(!this.queryForm.address||this.queryForm.address.length<=0){
        this.$modal.msgWarning("请选择城市");  
        return
      }
      if(!this.queryForm.dateRange||this.queryForm.dateRange.length<=0){
        this.$modal.msgWarning("请选择开始和结束日期");
        return
      }
      const cityInfo = filterCitys.find(x=>x.city_code==this.queryForm.address[1]);
      const p = {
        webSite:this.queryForm.webSite,
        areaId:this.queryForm.address[1],
        startTime:this.queryForm.dateRange[0],
        endTime:this.queryForm.dateRange[1],
        cityName:cityInfo?cityInfo.city_name:''
      }
      const loading = this.$loading({
          lock: true,
          text: '爬取中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      craw(this.addDateRange(p, this.dateRange)).then(response=>{
        this.$modal.msgSuccess("爬取成功");
        loading.close();
      }).catch(() => {
        loading.close();
      });
      console.log("@queryParams",p)
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryForm = {}
      // this.resetForm("queryForm");
      // this.handleQuery();
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
      this.title = "添加";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPollution(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.areaId = this.formAddress[1];
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
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function() {
        return delPollution(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/pollution/export', {
        ...this.queryParams
      }, `pollution_${new Date().getTime()}.xlsx`)
    },
     /** 导入按钮操作 */
     handleImport() {
      this.upload.title = "导入大气污染数据";
      this.upload.open = true;
    },
     /** 下载模板操作 */
     importTemplate() {
      this.download('system/pollution/importTemplate', {
      }, `pollution_template_${new Date().getTime()}.xlsx`)
    },
     // 文件上传中处理
     handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
