import Vue from 'vue'
import Router from 'vue-router'
import FunctionConfig from '@/components/FunctionConfig'
import EventHandler from '@/components/EventHandler'
import Login from '@/components/Login'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/functionConfig',
      name: 'FunctionConfig',
      component: FunctionConfig
    },
    {
      path: '/eventHandler',
      name: 'EventHandler',
      component: EventHandler
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        notNeedNavigationBar: true
      }
    }
  ]
})
