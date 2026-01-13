<template>
  <div class="app-container">
    <el-container>
      <el-header class="app-header">
        <div class="header-content">
          <el-icon :size="28" color="#409EFF"><Monitor /></el-icon>
          <h1>AI编程小助手</h1>
        </div>
        <p class="subtitle">编程学习 · 项目建议 · 求职指导 · 面试技巧</p>
      </el-header>
      <el-main class="app-main">
        <ChatBox />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import ChatBox from './components/ChatBox.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

.app-container {
  min-height: 100vh;
  padding: 20px;
}

.app-header {
  text-align: center;
  color: white;
  padding: 20px 0;
  height: auto !important;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.header-content h1 {
  font-size: 28px;
  font-weight: 600;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.subtitle {
  margin-top: 8px;
  font-size: 14px;
  opacity: 0.9;
}

.app-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 0;
  height: calc(100vh - 140px);
  overflow: hidden;
}
</style>
