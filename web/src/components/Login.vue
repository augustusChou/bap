<template>
  <div id="loginPage">
    <div id="loginForm" style="width: 20%">
      <el-input v-model="loginForm.loginName" placeholder="登录名称"></el-input>
      <div style="margin: 5px 0;"></div>
      <el-input v-model="loginForm.loginPassword" type="password" placeholder="登录密码"></el-input>
      <div style="margin: 5px 0;"></div>
      <el-button type="primary" style="width: 100%" @click="login()">登录</el-button>
    </div>
  </div>
</template>

<script>
import {tryLogin} from '@/api/user'
import Cookies from 'js-cookie'

export default {
  name: 'Login',
  data () {
    return {
      loginForm: {
        loginName: '',
        loginPassword: ''
      }
    }
  },
  methods: {
    login: function () {
      tryLogin({loginName: this.loginForm.loginName, loginPassword: this.loginForm.loginPassword}).then(res => {
        if (res.code === 0) {
          // 登录成功之后重定向到首页
          Cookies.set('accessToken', res.data)
          this.$router.push({path: '/'})
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
  #loginPage{position:fixed;width:100%;height:100%;top:0;left:0;background-color:rgba(0,0,0,.7);}
  #loginForm{position: absolute;top:0;left:0;bottom:0;right:0;width:50%;height:50%;margin:auto;}
</style>
