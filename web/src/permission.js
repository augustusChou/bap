import router from '@/router/index'
import Cookies from 'js-cookie'

router.beforeEach((to, from, next) => {
  if (Cookies.get('accessToken')) { // 判断是否有token
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    if (to.path === '/login') {
      next()
    } else {
      console.log('未登录')
      next({
        path: '/login'
      })
    }
  }
})
