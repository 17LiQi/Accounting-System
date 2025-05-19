<script setup lang="ts">
// 组件逻辑
</script>

<template>
  <div class="home">
    <h1>欢迎使用会计系统</h1>
    <nav>
      <router-link to="/accounts">查看账户列表</router-link>
    </nav>
  </div>
</template>

<style scoped>
.home {
  padding: 2rem;
  text-align: center;
}

h1 {
  margin-bottom: 2rem;
}

nav {
  margin-top: 2rem;
}

a {
  color: #42b983;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>