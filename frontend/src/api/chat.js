import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 60000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const sendMessage = async (message) => {
  const response = await api.post('/chat', { message })
  return response.data
}

export const sendMessageStream = async (message, onToken) => {
  const response = await fetch('/api/chat/stream', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ message })
  })

  if (!response.ok) {
    throw new Error('Stream request failed')
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder()

  try {
    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      const text = decoder.decode(value, { stream: true })
      // SSE 格式: data: xxx\n\n
      const lines = text.split('\n')
      for (const line of lines) {
        if (line.startsWith('data:')) {
          const token = line.substring(5).trim()
          if (token) {
            onToken(token)
          }
        } else if (line.trim()) {
          // 如果不是 SSE 格式，直接传递内容
          onToken(line)
        }
      }
    }
  } finally {
    reader.releaseLock()
  }
}

export const healthCheck = async () => {
  const response = await api.get('/chat/health')
  return response.data
}
