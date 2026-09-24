import { useState, useMemo } from 'react'
import { Link } from 'react-router-dom'
import { ChevronRight } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { rolesApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import SearchInput from '../components/common/SearchInput.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState, EmptyState } from '../components/common/States.jsx'

export default function RolesPage() {
  const [q, setQ] = useState('')
  const { data, loading, error, refetch } = useFetch(() => rolesApi.getAll(), [])
  const roles = data?.data ?? []

  const filtered = useMemo(() => {
    if (!q.trim()) return roles
    const lower = q.toLowerCase()
    return roles.filter(r => r.title?.toLowerCase().includes(lower) || r.level?.toLowerCase().includes(lower))
  }, [roles, q])

  return (
    <div>
      <PageHeader title="Job Roles" subtitle={`${roles.length} roles in the graph`} />
      <div className="mb-4">
        <SearchInput value={q} onChange={setQ} placeholder="Filter roles…" className="max-w-sm" />
      </div>

      {loading && <LoadingState message="Loading roles…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}
      {!loading && !error && filtered.length === 0 && <EmptyState message="No roles match your filter." />}

      {!loading && !error && filtered.length > 0 && (
        <div className="card divide-y divide-gray-100">
          {filtered.map(r => (
            <Link key={r.id} to={`/roles/${r.id}`} className="flex items-center justify-between px-5 py-4 hover:bg-gray-50 transition-colors group">
              <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-gray-800 group-hover:text-brand-600">{r.title}</span>
                <Badge label={r.level} />
              </div>
              <ChevronRight size={15} className="text-gray-300 group-hover:text-brand-500" />
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
