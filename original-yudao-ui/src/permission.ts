import router from './router'
import type { RouteRecordRaw } from 'vue-router'
import { isRelogin } from '@/config/axios/service'
import { getAccessToken } from '@/utils/auth'
import { useTitle } from '@/hooks/web/useTitle'
import { useNProgress } from '@/hooks/web/useNProgress'
import { usePageLoading } from '@/hooks/web/usePageLoading'
import { useDictStoreWithOut } from '@/store/modules/dict'
import { useUserStoreWithOut } from '@/store/modules/user'
import { usePermissionStoreWithOut } from '@/store/modules/permission'

const { start, done } = useNProgress()

const { loadStart, loadDone } = usePageLoading()

const parseURL = (
  url: string | null | undefined
): { basePath: string; paramsObject: { [key: string]: string } } => {
  // 如果输入为 null 或 undefined，返回空字符串和空对象
  if (url == null) {
    return { basePath: '', paramsObject: {} }
  }

  // 找到问号 (?) 的位置，它之前是基础路径，之后是查询参数
  const questionMarkIndex = url.indexOf('?')
  let basePath = url
  const paramsObject: { [key: string]: string } = {}

  // 如果找到了问号，说明有查询参数
  if (questionMarkIndex !== -1) {
    // 获取 basePath
    basePath = url.substring(0, questionMarkIndex)

    // 从 URL 中获取查询字符串部分
    const queryString = url.substring(questionMarkIndex + 1)

    // 使用 URLSearchParams 遍历参数
    const searchParams = new URLSearchParams(queryString)
    searchParams.forEach((value, key) => {
      // 封装进 paramsObject 对象
      paramsObject[key] = value
    })
  }

  // 返回 basePath 和 paramsObject
  return { basePath, paramsObject }
}

// 路由不重定向白名单
const whiteList = [
  '/login',
  '/social-login',
  '/auth-redirect',
  '/bind',
  '/register',
  '/oauthLogin/gitee'
]

// 路由加载前
router.beforeEach(async (to, from, next) => {
  start()
  loadStart()
  if (getAccessToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      // 获取所有字典
      const dictStore = useDictStoreWithOut()
      const userStore = useUserStoreWithOut()
      const permissionStore = usePermissionStoreWithOut()
      if (!dictStore.getIsSetDict) {
        await dictStore.setDictMap()
      }
      if (!userStore.getIsSetUser) {
        // #region agent log
        fetch('http://127.0.0.1:7242/ingest/9f9bb41f-0fa7-49a2-9cbc-851e0d8f0404',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({location:'permission.ts:74',message:'User not set, calling setUserInfoAction',data:{toPath:to.path},timestamp:Date.now(),sessionId:'debug-session',runId:'run1',hypothesisId:'C'})}).catch(()=>{});
        // #endregion
        isRelogin.show = true
        try {
          const result = await userStore.setUserInfoAction()
          // #region agent log
          fetch('http://127.0.0.1:7242/ingest/9f9bb41f-0fa7-49a2-9cbc-851e0d8f0404',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({location:'permission.ts:77',message:'setUserInfoAction result',data:{resultIsNull:result===null},timestamp:Date.now(),sessionId:'debug-session',runId:'run1',hypothesisId:'C'})}).catch(()=>{});
          // #endregion
          // 如果获取用户信息失败（返回null），说明token无效，跳转到登录页
          if (result === null) {
            isRelogin.show = false
            next(`/login?redirect=${to.fullPath}`)
            return
          }
          isRelogin.show = false
          // 后端过滤菜单
          await permissionStore.generateRoutes()
          permissionStore.getAddRouters.forEach((route) => {
            router.addRoute(route as unknown as RouteRecordRaw) // 动态添加可访问路由表
          })
          const redirectPath = from.query.redirect || to.path
          // 修复跳转时不带参数的问题
          const redirect = decodeURIComponent(redirectPath as string)
          const { paramsObject: query } = parseURL(redirect)
          const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect, query }
          next(nextData)
        } catch (error) {
          // #region agent log
          fetch('http://127.0.0.1:7242/ingest/9f9bb41f-0fa7-49a2-9cbc-851e0d8f0404',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({location:'permission.ts:96',message:'Error in setUserInfoAction',data:{errorMessage:(error as Error)?.message,errorName:(error as Error)?.name},timestamp:Date.now(),sessionId:'debug-session',runId:'run1',hypothesisId:'C'})}).catch(()=>{});
          // #endregion
          // 如果发生错误，清除token并跳转到登录页
          console.error('获取用户信息时发生错误:', error)
          isRelogin.show = false
          next(`/login?redirect=${to.fullPath}`)
        }
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
    }
  }
})

router.afterEach((to) => {
  useTitle(to?.meta?.title as string)
  done() // 结束Progress
  loadDone()
})
