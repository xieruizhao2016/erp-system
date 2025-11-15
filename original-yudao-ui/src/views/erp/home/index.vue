<template>
  <div class="erp-home-image">
    <img src="/favicon.ico" alt="ERP系统" class="erp-logo" />
  </div>
</template>

<script lang="ts" setup>
/** ERP首页 */
defineOptions({ name: 'ErpHome' })
</script>

<style lang="scss" scoped>
.erp-home-image {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  
  .erp-logo {
    max-width: 200px;
    max-height: 200px;
  }
}
</style>
