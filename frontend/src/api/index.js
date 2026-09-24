import apiClient from './client.js'

export const dashboardApi = {
  getStats: () => apiClient.get('/api/dashboard'),
}

export const candidatesApi = {
  getAll:  ()   => apiClient.get('/api/candidates'),
  getById: (id) => apiClient.get(`/api/candidates/${id}`),
}

export const skillsApi = {
  getAll:  ()   => apiClient.get('/api/skills'),
  getById: (id) => apiClient.get(`/api/skills/${id}`),
}

export const projectsApi = {
  getAll:  ()   => apiClient.get('/api/projects'),
  getById: (id) => apiClient.get(`/api/projects/${id}`),
}

export const technologiesApi = {
  getAll:  ()   => apiClient.get('/api/technologies'),
  getById: (id) => apiClient.get(`/api/technologies/${id}`),
}

export const rolesApi = {
  getAll:  ()   => apiClient.get('/api/roles'),
  getById: (id) => apiClient.get(`/api/roles/${id}`),
}

export const searchApi = {
  search: (q) => apiClient.get('/api/search', { params: { q } }),
}

export const recommendationsApi = {
  getRoles:  (userId)           => apiClient.get(`/api/recommendations/${userId}`),
  getGap:    (userId, roleId)   => apiClient.get(`/api/recommendations/${userId}/gap/${roleId}`),
}

export const graphApi = {
  traversal:     ()       => apiClient.get('/api/graph/traversal'),
  careerPath:    ()       => apiClient.get('/api/graph/career-path'),
  neighbourhood: (nodeId) => apiClient.get(`/api/graph/neighbourhood/${nodeId}`),
}
