<template>
  <div id="functionConfig">
    <el-button type="primary" @click=" dialogParam.title = '新增配置';dialogParam.visible = true;dialogParam.isNew = true">
      新增配置
    </el-button>
    <el-table
      v-loading="loading"
      :data="configData.pageResponse.data">
      <el-table-column
        label="配置类型">
        <template slot-scope="scope">
          <span>{{ scope.row.configTypeDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="配置名称">
        <template slot-scope="scope">
          <span>{{ scope.row.configName }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="配置信息">
        <template slot-scope="scope">
          <el-popover
            placement="top-start"
            title="JSON"
            trigger="hover"
            :content="scope.row.configInfo">
            <el-button slot="reference">查看配置信息</el-button>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="更新时间">
        <template slot-scope="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      :total="configData.pageResponse.total"
    >
    </el-pagination>

    <el-dialog
      :title="dialogParam.title"
      :visible.sync="dialogParam.visible"
      width="30%"
      :before-close="closeDialog">
      <span>
          <el-select v-model="changeData.configType" placeholder="请选择配置类型">
          <el-option
            v-for="item in configData.funcList"
            :key="item.type"
            :label="item.desc"
            :value="item.type">
          </el-option>
        </el-select>
        <div style="margin: 20px 0;"></div>
        <el-input v-model="changeData.configName" placeholder="请输入配置名称"></el-input>
        <div style="margin: 20px 0;"></div>
        <el-input
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 10}"
          placeholder="请输入配置信息(JSON字符串)"
          v-model="changeData.configInfo">
        </el-input>
      </span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeDialog()">取 消</el-button>
    <el-button type="primary" @click="saveChange()">保 存</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {getFuncConfig, addFuncConfig, updateFuncConfig, delFuncConfig} from '@/api/function_config'

export default {
  name: 'FunctionConfig',
  data () {
    return {
      loading: false,
      configData: {pageResponse: {data: []}},
      pageNo: 1,
      pageSize: 10,
      dialogParam: {
        title: '',
        visible: false,
        isNew: false,
        id: 0
      },
      changeData: {
        configType: '',
        configName: '',
        configInfo: ''
      }
    }
  },
  mounted () {
    console.log('执行页面加载函数')
    this.loadData()
  },
  methods: {
    loadData: function () {
      this.loading = true
      getFuncConfig(this.pageNo, this.pageSize).then(res => {
        if (res.code === 0) {
          this.configData = res.data
        } else {
          this.$notify.error({
            title: '失败',
            message: res.msg,
            duration: 3000
          })
        }
        this.loading = false
      })
    },
    handleEdit: function (lineNum, row) {
      this.changeData.configType = row.configType
      this.changeData.configName = row.configName
      this.changeData.configInfo = row.configInfo
      this.dialogParam.id = row.id
      this.dialogParam.title = '修改配置'
      this.dialogParam.visible = true
    },
    handleDelete: function (lineNum, row) {
      delFuncConfig(row).then(res => {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success'
          })
          this.closeDialog()
        } else {
          this.$notify.error({
            title: '失败',
            message: res.msg,
            duration: 3000
          })
        }
        this.loadData()
      })
    },
    saveChange: function () {
      let requestParam = JSON.parse(JSON.stringify(this.changeData))
      if (this.dialogParam.isNew) {
        addFuncConfig(requestParam).then(res => {
          if (res.code === 0) {
            this.$notify({
              title: '成功',
              message: '添加成功',
              type: 'success'
            })
            this.closeDialog()
          } else {
            this.$notify.error({
              title: '失败',
              message: res.msg,
              duration: 3000
            })
          }
          this.loadData()
        })
      } else {
        requestParam.id = this.dialogParam.id
        updateFuncConfig(requestParam).then(res => {
          if (res.code === 0) {
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success'
            })
            this.closeDialog()
          } else {
            this.$notify.error({
              title: '失败',
              message: res.msg,
              duration: 3000
            })
          }
          this.loadData()
        })
      }
    },
    closeDialog: function () {
      this.dialogParam.isNew = false
      this.dialogParam.visible = false
      this.changeData.configType = ''
      this.changeData.configName = ''
      this.changeData.configInfo = ''
    },
    handleSizeChange (size) {
      this.pageSize = size
      this.loadData()
    },
    handleCurrentChange (page) {
      this.pageNo = page
      this.loadData()
    }
  }
}
</script>

<style scoped>

</style>
