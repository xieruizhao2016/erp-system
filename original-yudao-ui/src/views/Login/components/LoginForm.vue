<template>
  <el-form
    v-show="getShow"
    ref="formLogin"
    :model="loginData.loginForm"
    :rules="LoginRules"
    class="login-form"
    label-position="top"
    label-width="120px"
    size="large"
  >
    <el-row class="mx-[-10px]">
      <el-col :span="24" class="px-10px">
        <el-form-item>
          <LoginFormTitle class="w-full" />
        </el-form-item>
      </el-col>
      <el-col :span="24" class="px-10px">
        <el-form-item v-if="loginData.tenantEnable === 'true' && showTenantInput" prop="tenantName">
          <el-input
            v-model="loginData.loginForm.tenantName"
            :placeholder="t('login.tenantNamePlaceholder')"
            :prefix-icon="iconHouse"
            link
            type="primary"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" class="px-10px">
        <el-form-item prop="username">
          <el-input
            v-model="loginData.loginForm.username"
            :placeholder="t('login.usernamePlaceholder')"
            :prefix-icon="iconAvatar"
            autocomplete="off"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" class="px-10px">
        <el-form-item prop="password">
          <el-input
            v-model="loginData.loginForm.password"
            :placeholder="t('login.passwordPlaceholder')"
            :prefix-icon="iconLock"
            show-password
            type="password"
            autocomplete="new-password"
            @keyup.enter="getCode()"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" class="px-10px mt-[-20px] mb-[-20px]">
        <el-form-item>
          <el-row justify="space-between" style="width: 100%">
            <el-col :span="6">
              <el-checkbox v-model="loginData.loginForm.rememberMe">
                {{ t('login.remember') }}
              </el-checkbox>
            </el-col>
            <el-col :offset="6" :span="12">
              <el-link
                class="float-right"
                type="primary"
                @click="setLoginState(LoginStateEnum.RESET_PASSWORD)"
              >
                {{ t('login.forgetPassword') }}
              </el-link>
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>
      <el-col :span="24" class="px-10px">
        <el-form-item>
          <XButton
            :loading="loginLoading"
            :title="t('login.login')"
            class="w-full"
            type="primary"
            @click="getCode()"
          />
        </el-form-item>
      </el-col>
      <Verify
        v-if="loginData.captchaEnable === 'true'"
        ref="verify"
        :captchaType="captchaType"
        :imgSize="{ width: '400px', height: '200px' }"
        mode="pop"
        @success="handleLogin"
      />
    </el-row>
  </el-form>
</template>
<script lang="ts" setup>
import { ElLoading } from 'element-plus'
import LoginFormTitle from './LoginFormTitle.vue'
import type { RouteLocationNormalizedLoaded } from 'vue-router'

import { useIcon } from '@/hooks/web/useIcon'

import * as authUtil from '@/utils/auth'
import { usePermissionStore } from '@/store/modules/permission'
import * as LoginApi from '@/api/login'
import { LoginStateEnum, useFormValid, useLoginState } from './useLogin'

defineOptions({ name: 'LoginForm' })

const { t } = useI18n()
const message = useMessage()
const iconHouse = useIcon({ icon: 'ep:house' })
const iconAvatar = useIcon({ icon: 'ep:avatar' })
const iconLock = useIcon({ icon: 'ep:lock' })
const formLogin = ref()
const { validForm } = useFormValid(formLogin)
const { setLoginState, getLoginState } = useLoginState()
const { currentRoute, push } = useRouter()
const permissionStore = usePermissionStore()
const redirect = ref<string>('')
const loginLoading = ref(false)
const verify = ref()
const captchaType = ref('blockPuzzle') // blockPuzzle 滑块 clickWord 点击文字 pictureWord 文字验证码

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN)

const LoginRules = {
  tenantName: [required],
  username: [required],
  password: [required]
}
// 控制租户输入框显示/隐藏
const showTenantInput = ref(true) // 默认显示

const loginData = reactive({
  isShowPassword: false,
  captchaEnable: import.meta.env.VITE_APP_CAPTCHA_ENABLE,
  tenantEnable: import.meta.env.VITE_APP_TENANT_ENABLE,
  loginForm: {
    // 默认租户名设置为"芋道源码"
    tenantName: '芋道源码',
    // 账号和密码不默认填充，提高安全性
    username: '',
    password: '',
    captchaVerification: '',
    rememberMe: true // 默认记录我。如果不需要，可手动修改
  }
})

// 切换租户输入框显示/隐藏的方法（供父组件调用）
defineExpose({
  toggleTenantInput: () => {
    showTenantInput.value = !showTenantInput.value
  }
})

// 获取验证码
const getCode = async () => {
  // 情况一，未开启：则直接登录
  if (loginData.captchaEnable === 'false') {
    await handleLogin({})
  } else {
    // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
    // 弹出验证码
    verify.value.show()
  }
}
// 获取租户 ID
const getTenantId = async () => {
  if (loginData.tenantEnable === 'true') {
    try {
      const res = await LoginApi.getTenantIdByName(loginData.loginForm.tenantName)
      if (res == null || res === undefined) {
        // 如果租户不存在，使用默认租户"芋道源码"（ID=1）
        console.warn(`租户"${loginData.loginForm.tenantName}"不存在，使用默认租户"芋道源码"`)
        authUtil.setTenantId(1)
        // 更新租户名为"芋道源码"，确保一致性
        loginData.loginForm.tenantName = '芋道源码'
      } else {
        authUtil.setTenantId(res)
        console.log(`✅ 成功获取租户ID: ${res} (租户名: ${loginData.loginForm.tenantName})`)
      }
    } catch (error: any) {
      // 如果获取租户ID失败（网络错误等），使用默认租户"芋道源码"（ID=1）
      console.warn('获取租户ID失败，使用默认租户"芋道源码":', error)
      authUtil.setTenantId(1)
      // 更新租户名为"芋道源码"，确保一致性
      loginData.loginForm.tenantName = '芋道源码'
    }
    // 确保租户ID已设置（双重保险）
    if (!authUtil.getTenantId()) {
      console.warn('租户ID未设置，强制设置为默认租户"芋道源码"（ID=1）')
      authUtil.setTenantId(1)
      loginData.loginForm.tenantName = '芋道源码'
    }
  } else {
    // 如果租户功能未启用，也确保有默认值
    if (!authUtil.getTenantId()) {
      authUtil.setTenantId(1)
    }
  }
}
// 记住我
const getLoginFormCache = () => {
  const loginForm = authUtil.getLoginForm()
  if (loginForm) {
    // 只恢复"记住我"选项和租户名，不恢复账号密码（提高安全性）
    loginData.loginForm = {
      ...loginData.loginForm,
      // 账号和密码不自动填充，需要用户手动输入
      username: '',
      password: '',
      rememberMe: loginForm.rememberMe,
      // 如果缓存中有租户名则使用，否则保持默认值"芋道源码"
      tenantName: loginForm.tenantName ? loginForm.tenantName : loginData.loginForm.tenantName
    }
  } else {
    // 如果没有缓存，确保使用默认值
    if (!loginData.loginForm.tenantName) {
      loginData.loginForm.tenantName = '芋道源码'
    }
  }
}
// 根据域名，获得租户信息
const getTenantByWebsite = async () => {
  if (loginData.tenantEnable === 'true') {
    try {
      const website = location.host
      const res = await LoginApi.getTenantByWebsite(website)
      if (res) {
        loginData.loginForm.tenantName = res.name
        authUtil.setTenantId(res.id)
      } else {
        // 如果根据域名没有获取到租户，确保使用默认值
        if (!loginData.loginForm.tenantName) {
          loginData.loginForm.tenantName = '芋道源码'
        }
      }
    } catch (error) {
      // 静默处理错误，不显示错误提示，因为这是可选的 API 调用
      // 如果失败，使用默认租户名即可
      console.warn('获取租户信息失败，使用默认租户:', error)
      if (!loginData.loginForm.tenantName) {
        loginData.loginForm.tenantName = '芋道源码'
      }
    }
  } else {
    // 如果租户功能未启用，也设置默认值
    if (!loginData.loginForm.tenantName) {
      loginData.loginForm.tenantName = '芋道源码'
    }
  }
}
const loading = ref() // ElLoading.service 返回的实例
// 登录
const handleLogin = async (params: any) => {
  loginLoading.value = true
  try {
    // 先获取租户ID（如果失败会使用默认值，不会抛出错误）
    await getTenantId()
    // 再次确认租户ID已设置
    const tenantId = authUtil.getTenantId()
    if (!tenantId && loginData.tenantEnable === 'true') {
      console.error('登录前租户ID未设置，强制设置为默认租户"芋道源码"（ID=1）')
      authUtil.setTenantId(1)
      loginData.loginForm.tenantName = '芋道源码'
    }
    const data = await validForm()
    if (!data) {
      return
    }
    const loginDataLoginForm = { ...loginData.loginForm }
    loginDataLoginForm.captchaVerification = params.captchaVerification
    const res = await LoginApi.login(loginDataLoginForm)
    if (!res) {
      return
    }
    loading.value = ElLoading.service({
      lock: true,
      text: '正在加载系统中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    if (loginDataLoginForm.rememberMe) {
      authUtil.setLoginForm(loginDataLoginForm)
    } else {
      authUtil.removeLoginForm()
    }
    authUtil.setToken(res)
    if (!redirect.value) {
      redirect.value = '/'
    }
    // 判断是否为SSO登录
    if (redirect.value.indexOf('sso') !== -1) {
      window.location.href = window.location.href.replace('/login?redirect=', '')
    } else {
      await push({ path: redirect.value || permissionStore.addRouters[0].path })
    }
  } finally {
    loginLoading.value = false
    loading.value.close()
  }
}

watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true
  }
)
onMounted(() => {
  getLoginFormCache()
  getTenantByWebsite()
})
</script>

<style lang="scss" scoped>
.login-code {
  float: right;
  width: 100%;
  height: 38px;

  img {
    width: 100%;
    height: auto;
    max-width: 100px;
    vertical-align: middle;
    cursor: pointer;
  }
}
</style>
