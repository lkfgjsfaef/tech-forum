import request from './api'

export function login(data) {
  return request.post('/user/login', data)
}

export function register(data) {
  return request.post('/user/register', data)
}

export function getUserProfile() {
  return request.get('/user/profile')
}

export function updateUserProfile(data) {
  return request.put('/user/profile', data)
}

export function uploadAvatar(formData) {
  return request.post('/user/avatar', formData)
}

export function listTags() {
  return request.get('/tag/list')
}

export function createTag(data) {
  return request.post('/tag/create', data)
}

export function updateTag(tagId, data) {
  return request.put('/tag/' + tagId, data)
}

export function deleteTag(tagId) {
  return request.delete('/tag/' + tagId)
}

export function addTagToSession(sessionId, tagId) {
  return request.post('/tag/session/' + sessionId + '/' + tagId)
}

export function removeTagFromSession(sessionId, tagId) {
  return request.delete('/tag/session/' + sessionId + '/' + tagId)
}

export function createSession(data) {
  return request.post('/session/create', data)
}

export function listSessions() {
  return request.get('/session/list')
}

export function deleteSession(sessionId) {
  return request.delete('/session/' + sessionId)
}

export function searchSessions(keyword) {
  return request.get('/session/search', { params: { keyword } })
}

export function searchMessages(keyword) {
  return request.get('/message/search', { params: { keyword } })
}

export function renameSession(sessionId, title) {
  return request.put('/session/' + sessionId + '/title', null, { params: { title } })
}

export function updateSystemPrompt(sessionId, systemPrompt) {
  return request.put('/session/' + sessionId + '/prompt', { systemPrompt })
}

export function listMessages(sessionId) {
  return request.get('/message/list', { params: { sessionId } })
}

export function updateMessage(messageId, sessionId, content) {
  return request.put('/message/' + messageId, { content }, { params: { sessionId } })
}

export function deleteAfterMessage(sessionId, afterMessageId) {
  return request.delete('/message/after', { params: { sessionId, afterMessageId } })
}

export function updateFeedback(messageId, sessionId, feedback) {
  return request.put('/message/' + messageId + '/feedback', { feedback }, { params: { sessionId } })
}

export function listModels() {
  return request.get('/model/list')
}

export function listTemplates() {
  return request.get('/template/list')
}

export function uploadFile(file, sessionId) {
  const formData = new FormData()
  formData.append('file', file)
  if (sessionId) formData.append('sessionId', sessionId)
  return request.post('/upload/file', formData)
}

export function regenerate(data) {
  return request.post('/chat/regenerate', data)
}

export function streamChat(sessionId, content, modelName) {
  const params = new URLSearchParams({ sessionId, content })
  if (modelName) params.append('modelName', modelName)
  return request.get('/chat/stream', { params })
}
