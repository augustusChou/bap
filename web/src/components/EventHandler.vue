<template>
  <div id="eventHandler">
    <div>
      <el-input clearable style="width: 200px;" v-model="listQuery.schemasName" placeholder="数据库名称"></el-input>
      <el-input clearable style="width: 200px;" v-model="listQuery.tName" placeholder="表名"></el-input>
      <el-select clearable style="width: 200px;" v-model="listQuery.eventType" placeholder="请选择查询类型">
        <el-option
          v-for="item in queryTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button
        type="primary"
        @click="loadData()"
        icon="el-icon-search">查询
      </el-button>
    </div>
    <div style="margin: 40px 0;"></div>
    <el-button
      type="primary"
      size="mini"
      @click="openAddEventDialog()">新增事件处理
    </el-button>
    <el-table
      v-loading="loading"
      :data="configData.data">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-button
            type="primary"
            size="mini"
            @click="addOperationToRule(props.row.id)">新增操作
          </el-button>
          <el-table
            :data="props.row.operationList"
            style="width: 100%">
            <el-table-column
              width="200px"
              label="操作备注">
              <template slot-scope="scope">
                  <p>{{scope.row.remarks}}</p>
              </template>
            </el-table-column>
            <el-table-column width="150px"
              prop="delayTime"
              label="延迟执行时间(毫秒)">
            </el-table-column>
            <el-table-column
              width="200px"
              prop="createTime"
              label="创建时间">
            </el-table-column>
            <el-table-column
              width="200px"
              prop="updateTime"
              label="修改时间">
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" @click="updateOperationRemark(scope.row.id,scope.row.ruleId)">修改备注</el-button>
                <el-button type="primary" size="mini" @click="updateDelayTime(scope.row.id,scope.row.ruleId)">修改延迟执行时间</el-button>
                <el-button
                  type="warning"
                  size="mini"
                  @click="showScriptEdit(scope.$index, scope.row)">查看脚本
                </el-button>
                <el-button type="danger" size="mini" @click="deleteOperation(scope.row.id,scope.row.ruleId)">删除操作</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column
        label="数据库名称">
        <template slot-scope="scope">
          <span>{{ scope.row.schemasName }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="数据表名称">
        <template slot-scope="scope">
          <span>{{ scope.row.tname }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="查询类型">
        <template slot-scope="scope">
          <span>{{ scope.row.eventType===1?'增加': scope.row.eventType===2?'修改': scope.row.eventType===3?'删除': '异常'}}</span>
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
            @click="openRuleEditDialog(scope.$index, scope.row)">编辑
          </el-button>
          <el-button v-if="scope.row.ruleStatus==='1'"
            size="mini" type="warning"
            @click="disableRule(scope.$index, scope.row)">禁用
          </el-button>
          <el-button v-if="scope.row.ruleStatus==='0'"
                     size="mini"
                     type="primary"
                     @click="enableRule(scope.$index, scope.row)">启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      :total="listQuery.total"
    >
    </el-pagination>
    <el-dialog
      :fullscreen="1!==2"
      :visible.sync="scriptEditDialogParam.visible"
      width="70%"
      :before-close="closeScriptEditDialog">
      <span>
          <p style="font-size: 24px">脚本编写指南：http://wiki.joyxuan.com/pages/viewpage.action?pageId=9830487</p>
          <codemirror ref="myCm" @ready="onCodemirrorReady" :value="code" @input="change"
                      :options="options"></codemirror>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeScriptEditDialog()">取 消</el-button>
        <el-button type="warning" @click="verifyScript()">校验脚本</el-button>
        <el-button type="primary" @click="saveScriptUpdateChange()">保 存</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :visible.sync="ruleEditDialogParam.visible"
      :title="ruleEditDialogParam.title"
      width="40%">
      <span>
        <el-input  v-model="ruleEditDialogParam.tmpData.schemasName" placeholder="数据库名称"></el-input>
              <div style="margin: 20px 0;"></div>
        <el-input  v-model="ruleEditDialogParam.tmpData.tname" placeholder="表名"></el-input>
              <div style="margin: 20px 0;"></div>
         <el-select  v-model="ruleEditDialogParam.tmpData.eventType" placeholder="请选择查询类型">
          <el-option
            v-for="item in queryTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <div style="margin: 20px 0;"></div>
          <el-select  v-model="ruleEditDialogParam.tmpData.ruleStatus" placeholder="状态">
          <el-option
            v-for="item in eventRuleStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="ruleEditDialogParam.visible = false">取 消</el-button>
        <el-button type="primary" @click="saveRuleEditUpdateChange()">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {getEventRule, updateEventRule, delEventRule} from '@/api/event_handler'
import {verifyScript, updateEventOperation, addEventOperation, delEventOperation} from '@/api/event_operation'
import 'codemirror/lib/codemirror.css'
import {addEventRule} from '../api/event_handler'

require('codemirror/mode/javascript/javascript')
require('codemirror/mode/vue/vue')
require('codemirror/addon/hint/show-hint.js')
require('codemirror/addon/hint/show-hint.css')
require('codemirror/addon/hint/javascript-hint.js')

export default {
  name: 'EventHandler',
  data () {
    return {
      mode: 'text/javascript',
      scriptCode: '',
      loading: false,
      configData: {},
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        total: 0,
        schemasName: '',
        eventType: '',
        tName: ''
      },
      scriptEditDialogParam: {
        tmpOperation: {},
        visible: false
      },
      ruleEditDialogParam: {
        title: '',
        isNew: false,
        tmpData: {
          eventType: '',
          tname: '',
          schemasName: '',
          ruleStatus: '0'
        },
        visible: false
      },
      queryTypeOptions: [{
        value: 1,
        label: '增加'
      }, {
        value: 2,
        label: '修改'
      }, {
        value: 3,
        label: '删除'
      }],
      eventRuleStatusOptions: [{
        value: '1',
        label: '启用'
      }, {
        value: '0',
        label: '禁用'
      }]
    }
  },
  computed: {
    code: function () {
      return this.scriptCode
    },
    options: function () {
      return {
        mode: this.mode,
        tabSize: 2,
        styleSelectedText: true,
        lineNumbers: true,
        lineWrapping: true
      }
    }
  },
  mounted () {
    console.log('执行页面加载函数')
    this.loadData()
  },
  methods: {
    onCodemirrorReady: function (cm) {
      cm.setSize('100%', '100%')
    },
    change: function (code) {
      this.scriptCode = code
    },
    loadData: function () {
      this.loading = true
      getEventRule(this.listQuery).then(res => {
        if (res.code === 0) {
          this.configData = res.data
          this.listQuery.total = res.data.total
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
    showScriptEdit: function (lineNum, row) {
      this.scriptCode = row.operationScript
      this.scriptEditDialogParam.tmpOperation = JSON.parse(JSON.stringify(row))
      this.scriptEditDialogParam.visible = true
    },
    saveScriptUpdateChange: function () {
      this.scriptEditDialogParam.tmpOperation.operationScript = this.scriptCode
      let requestParam = this.scriptEditDialogParam.tmpOperation
      updateEventOperation(requestParam).then(res => {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '修改成功',
            type: 'success'
          })
          this.closeScriptEditDialog()
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
    verifyScript: function () {
      this.scriptEditDialogParam.tmpOperation.operationScript = this.scriptCode
      let requestParam = this.scriptEditDialogParam.tmpOperation
      verifyScript(requestParam).then(res => {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '校验成功',
            type: 'success'
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: res.msg,
            duration: 3000
          })
        }
      })
    },
    saveRuleEditUpdateChange: function () {
      if (this.ruleEditDialogParam.isNew) {
        this.addEventRule()
      } else {
        this.enableRule(-1, this.ruleEditDialogParam.tmpData)
      }

      this.ruleEditDialogParam.visible = false
    },
    enableRule: function (lineNum, row) {
      let requestParam = JSON.parse(JSON.stringify(row))
      requestParam.operationList = []
      requestParam.ruleStatus = '1'
      updateEventRule(requestParam).then(res => {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '启用成功',
            type: 'success'
          })
          this.closeScriptEditDialog()
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
    disableRule: function (lineNum, row) {
      delEventRule(row).then(res => {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '禁用成功',
            type: 'success'
          })
          this.closeScriptEditDialog()
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
    closeScriptEditDialog: function () {
      this.scriptEditDialogParam.visible = false
    },
    handleSizeChange (size) {
      this.listQuery.pageSize = size
      this.loadData()
    },
    handleCurrentChange (page) {
      this.listQuery.pageNo = page
      this.loadData()
    },
    openRuleEditDialog: function (lineNum, row) {
      this.ruleEditDialogParam.tmpData = JSON.parse(JSON.stringify(row))
      this.ruleEditDialogParam.isNew = false
      this.ruleEditDialogParam.title = '修改事件规则'
      this.ruleEditDialogParam.visible = true
    },
    deleteOperation: function (operationId, ruleId) {
      this.$confirm('此操作将永久删除该操作, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delEventOperation({id: operationId, ruleId: ruleId}).then(res => {
          if (res.code === 0) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success'
            })
            this.closeScriptEditDialog()
          } else {
            this.$notify.error({
              title: '失败',
              message: res.msg,
              duration: 3000
            })
          }
          this.loadData()
        })
      }).catch(() => {
      })
    },
    updateOperationRemark: function (operationId, ruleId) {
      this.$prompt('请输入操作备注(不要超过256个字符)', '修改操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        updateEventOperation({
          id: operationId,
          ruleId: ruleId,
          remarks: value
        }).then(res => {
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '修改操作成功'
            })
          } else {
            this.$message({
              type: 'error',
              message: res.msg
            })
          }
          this.loadData()
        })
      }).catch(() => {
      })
    },
    updateDelayTime: function (operationId, ruleId) {
      this.$prompt('请输入延迟执行时间(整型、单位为毫秒)，在check_event函数返回true后停顿指定的时间后再执行run函数', '修改操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        updateEventOperation({
          id: operationId,
          ruleId: ruleId,
          delayTime: value
        }).then(res => {
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '添加操作成功'
            })
          } else {
            this.$message({
              type: 'error',
              message: res.msg
            })
          }
          this.loadData()
        })
      }).catch(() => {
      })
    },
    addOperationToRule: function (ruleId) {
      this.$prompt('请输入操作备注', '新增操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        addEventOperation({
          ruleId: ruleId,
          remarks: value
        }).then(res => {
          if (res.code === 0) {
            this.$message({
              type: 'success',
              message: '添加操作成功'
            })
          } else {
            this.$message({
              type: 'error',
              message: res.msg
            })
          }
          this.loadData()
        })
      }).catch(() => {
      })
    },
    openAddEventDialog: function () {
      this.ruleEditDialogParam.title = '新增事件规则'
      this.ruleEditDialogParam.tmpData.schemasName = ''
      this.ruleEditDialogParam.tmpData.tname = ''
      this.ruleEditDialogParam.tmpData.eventType = ''
      this.ruleEditDialogParam.tmpData.ruleStatus = '0'
      this.ruleEditDialogParam.isNew = true
      this.ruleEditDialogParam.visible = true
    },
    addEventRule: function () {
      addEventRule(this.ruleEditDialogParam.tmpData).then(res => {
        if (res.code === 0) {
          this.$notify({
            title: '成功',
            message: '添加成功',
            type: 'success'
          })
          this.loadData()
        } else {
          this.$notify.error({
            title: '失败',
            message: res.msg,
            duration: 3000
          })
        }
      })
    }
  }
}
</script>

<style scoped>
</style>
