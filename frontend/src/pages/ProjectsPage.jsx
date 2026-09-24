import { useState, useMemo } from 'react'
import { Link } from 'react-router-dom'
import { ChevronRight } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { projectsApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import SearchInput from '../components/common/SearchInput.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState, EmptyState } from '../components/common/States.jsx'

export default function ProjectsPage() {
  const [q, setQ] = useState('')
  const [status, setStatus] = useState('All')
  const { data, loading, error, refetch } = useFetch(() => projectsApi.getAll(), [])

  const projects = data?.data ?? []
  const statuses = ['All', 'active', 'completed']

  const filtered = useMemo(() => {
    let list = projects
    if (status !== 'All') list = list.filter(p => p.status === status)
    if (q.trim()) list = list.filter(p => p.name?.toLowerCase().includes(q.toLowerCase()) || p.domain?.toLowerCase().includes(q.toLowerCase()))
    return list
  }, [projects, status, q])

  return (
    <div>
      <PageHeader title="Projects" subtitle={`${projects.length} projects in the graph`} />

      <div className="flex flex-wrap gap-3 mb-4">
        <SearchInput value={q} onChange={setQ} placeholder="Filter projects…" className="w-56" />
        <div className="flex gap-1.5">
          {statuses.map(s => (
            <button key={s} onClick={() => setStatus(s)}
              className={`px-3 py-1.5 text-xs font-medium rounded-full border transition-colors ${
                status === s ? 'bg-brand-600 text-white border-brand-600' : 'bg-white text-gray-600 border-gray-200 hover:border-brand-300'
              }`}
            >{s === 'All' ? 'All' : s.charAt(0).toUpperCase() + s.slice(1)}</button>
          ))}
        </div>
      </div>

      {loading && <LoadingState message="Loading projects…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}
      {!loading && !error && filtered.length === 0 && <EmptyState message="No projects match your filter." />}

      {!loading && !error && filtered.length > 0 && (
        <div className="grid sm:grid-cols-2 xl:grid-cols-3 gap-4">
          {filtered.map(p => (
            <Link key={p.id} to={`/projects/${p.id}`} className="card p-5 hover:shadow-md transition-shadow group">
              <div className="flex items-start justify-between mb-3">
                <div className="flex gap-2">
                  <Badge label={p.status} />
                  {p.domain && <Badge label={p.domain} />}
                </div>
                <ChevronRight size={15} className="text-gray-300 group-hover:text-brand-500" />
              </div>
              <h3 className="font-semibold text-gray-900 group-hover:text-brand-600 transition-colors">{p.name}</h3>
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
