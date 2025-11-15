<template>
  <div>
    <el-card shadow="never">
      <el-skeleton :loading="loading" animated>
        <el-row :gutter="16" justify="space-between">
          <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
            <div class="flex items-center">
              <el-avatar :src="avatar" :size="70" class="mr-16px">
                <img src="@/assets/imgs/avatar.gif" alt="" />
              </el-avatar>
              <div>
                <div class="text-20px">
                  {{ t('workplace.welcome') }} {{ username }} {{ t('workplace.happyDay') }}
                </div>
                <div class="mt-10px text-14px text-gray-500">
                  {{ t('workplace.toady') }}，20℃ - 32℃！
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-skeleton>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
defineOptions({ name: 'Index' })

const { t } = useI18n()
const userStore = useUserStore()

const loading = ref(true)
const avatar = computed(() => userStore.userVo?.avatar)

const username = computed(() => userStore.userVo?.username)

onMounted(() => {
  loading.value = false
})
</script>
