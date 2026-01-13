<template>
  <div class="chat-container">
    <!-- 消息列表 -->
    <div class="message-list" ref="messageListRef">
      <!-- 欢迎消息 -->
      <div v-if="messages.length === 0" class="welcome-message">
        <el-icon :size="48" color="#409EFF"><ChatDotRound /></el-icon>
        <h2>你好，我是AI编程小助手!</h2>
        <p>我可以帮助你：</p>
        <div class="feature-list">
          <el-tag type="primary" size="large">规划编程学习路线</el-tag>
          <el-tag type="success" size="large">提供项目学习建议</el-tag>
          <el-tag type="warning" size="large">求职全流程指南</el-tag>
          <el-tag type="danger" size="large">分析面试技巧</el-tag>
        </div>
        <p class="hint">试着问我一个问题吧~</p>
      </div>

      <!-- 聊天消息 -->
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message-item', msg.role]"
      >
        <div class="avatar">
          <el-avatar :size="36" :style="{ backgroundColor: msg.role === 'user' ? '#409EFF' : '#67C23A' }">
            <el-icon v-if="msg.role === 'user'"><User /></el-icon>
            <el-icon v-else><Monitor /></el-icon>
          </el-avatar>
        </div>
        <div class="message-content">
          <div class="message-bubble" v-html="renderMarkdown(msg.content)"></div>
        </div>
      </div>

      <!-- 加载中 -->
      <div v-if="loading" class="message-item assistant">
        <div class="avatar">
          <el-avatar :size="36" style="background-color: #67C23A">
            <el-icon><Monitor /></el-icon>
          </el-avatar>
        </div>
        <div class="message-content">
          <div class="message-bubble loading">
            <span class="dot"></span>
            <span class="dot"></span>
            <span class="dot"></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="2"
        placeholder="输入你的问题，例如：Java后端学习路线是什么？"
        @keydown.enter.ctrl="sendMessage"
        :disabled="loading"
        resize="none"
      />
      <el-button
        type="primary"
        :icon="Promotion"
        :loading="loading"
        @click="sendMessage"
        :disabled="!inputMessage.trim() || loading"
        size="large"
      >
        发送
      </el-button>
    </div>
    <div class="input-hint">
      <span>按 Ctrl + Enter 发送</span>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { Promotion } from '@element-plus/icons-vue'
import { sendMessageStream } from '../api/chat'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

// 配置 marked
marked.setOptions({
  highlight: function(code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value
    }
    return hljs.highlightAuto(code).value
  },
  breaks: true
})

const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messageListRef = ref(null)

const renderMarkdown = (content) => {
  return marked(content)
}

const scrollToBottom = async () => {
  await nextTick()
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

const sendMessage = async () => {
  const message = inputMessage.value.trim()
  if (!message || loading.value) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: message
  })
  inputMessage.value = ''
  loading.value = true
  scrollToBottom()

  // 添加一个空的 AI 消息，后续会逐步填充
  const aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'assistant',
    content: ''
  })

  try {
    await sendMessageStream(message, (token) => {
      // 逐字添加到 AI 消息中
      messages.value[aiMessageIndex].content += token
      scrollToBottom()
    })
  } catch (error) {
    console.error('发送消息失败:', error)
    // 如果流式失败，显示错误信息
    messages.value[aiMessageIndex].content = '抱歉，发生了错误，请稍后重试。'
  } finally {
    loading.value = false
    scrollToBottom()
  }
}
</script>

<style scoped>
.chat-container {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.message-list {
  flex: 1 1 0;
  min-height: 0;
  overflow-y: auto;
  padding: 20px;
  background: #f5f7fa;
}

.welcome-message {
  text-align: center;
  padding: 40px 20px;
  color: #606266;
}

.welcome-message h2 {
  margin: 16px 0 8px;
  color: #303133;
}

.welcome-message p {
  margin: 8px 0;
}

.feature-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin: 20px 0;
}

.hint {
  color: #909399;
  font-size: 14px;
  margin-top: 20px;
}

.message-item {
  display: flex;
  margin-bottom: 16px;
  gap: 12px;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-content {
  max-width: 70%;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.6;
  word-break: break-word;
}

.message-item.user .message-bubble {
  background: #409EFF;
  color: white;
  border-bottom-right-radius: 4px;
}

.message-item.assistant .message-bubble {
  background: white;
  color: #303133;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* Markdown 样式 */
.message-bubble :deep(pre) {
  background: #f6f8fa;
  padding: 12px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-bubble :deep(code) {
  font-family: 'Fira Code', Consolas, Monaco, monospace;
  font-size: 13px;
}

.message-bubble :deep(p) {
  margin: 8px 0;
}

.message-bubble :deep(ul),
.message-bubble :deep(ol) {
  padding-left: 20px;
  margin: 8px 0;
}

.message-bubble :deep(li) {
  margin: 4px 0;
}

/* 加载动画 */
.message-bubble.loading {
  display: flex;
  gap: 4px;
  padding: 16px 20px;
}

.dot {
  width: 8px;
  height: 8px;
  background: #409EFF;
  border-radius: 50%;
  animation: bounce 1.4s ease-in-out infinite;
}

.dot:nth-child(1) { animation-delay: 0s; }
.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-8px); }
}

.input-area {
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  background: white;
  border-top: 1px solid #ebeef5;
}

.input-area :deep(.el-textarea__inner) {
  border-radius: 8px;
}

.input-area .el-button {
  height: auto;
  padding: 12px 24px;
}

.input-hint {
  text-align: center;
  padding: 0 0 12px;
  font-size: 12px;
  color: #909399;
}
</style>
