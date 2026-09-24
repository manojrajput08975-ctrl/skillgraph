import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import Layout from './components/common/Layout.jsx'
import DashboardPage from './pages/DashboardPage.jsx'
import CandidatesPage from './pages/CandidatesPage.jsx'
import CandidateDetailPage from './pages/CandidateDetailPage.jsx'
import SkillsPage from './pages/SkillsPage.jsx'
import SkillDetailPage from './pages/SkillDetailPage.jsx'
import ProjectsPage from './pages/ProjectsPage.jsx'
import ProjectDetailPage from './pages/ProjectDetailPage.jsx'
import RolesPage from './pages/RolesPage.jsx'
import RoleDetailPage from './pages/RoleDetailPage.jsx'
import RecommendationsPage from './pages/RecommendationsPage.jsx'
import GraphExplorerPage from './pages/GraphExplorerPage.jsx'
import SearchPage from './pages/SearchPage.jsx'

export default function App() {
  return (
    <BrowserRouter>
      <Toaster position="top-right" />
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<DashboardPage />} />
          <Route path="candidates" element={<CandidatesPage />} />
          <Route path="candidates/:id" element={<CandidateDetailPage />} />
          <Route path="skills" element={<SkillsPage />} />
          <Route path="skills/:id" element={<SkillDetailPage />} />
          <Route path="projects" element={<ProjectsPage />} />
          <Route path="projects/:id" element={<ProjectDetailPage />} />
          <Route path="roles" element={<RolesPage />} />
          <Route path="roles/:id" element={<RoleDetailPage />} />
          <Route path="recommendations" element={<RecommendationsPage />} />
          <Route path="graph" element={<GraphExplorerPage />} />
          <Route path="search" element={<SearchPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
