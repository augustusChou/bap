<template>
  <div id="app">

    <el-container v-if="!$route.meta.notNeedNavigationBar">
      <el-header>
        <div style="text-align:right">
          <el-button type="primary"  @click="tryLogout()">注销</el-button>
        </div>
        </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu
            default-active="2"
            class="el-menu-vertical-demo">
            <el-menu-item index="1">
              <i class="el-icon-menu"></i>
              <span slot="title"> <router-link to="/functionConfig">事件功能配置</router-link></span>
            </el-menu-item>
            <el-menu-item index="2">
              <i class="el-icon-edit"></i>
              <span slot="title"><router-link to="/eventHandler">事件处理配置</router-link></span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
    <!-- 登录页 -->
    <router-view v-if="$route.meta.notNeedNavigationBar"></router-view>
  </div>
</template>

<script>
import {logout} from '@/api/user'
import Cookies from 'js-cookie'
export default {
  name: 'App',
  data () {
    return {}
  },
  methods: {
    tryLogout: function () {
      logout(Cookies.get('accessToken')).then(res => {
        Cookies.remove('accessToken')
        this.$router.push({path: '/login'})
      })
    }
  }
}
</script>

<style>
#app {
}
#app .router-link-active {
  text-decoration: none;
}
</style>
