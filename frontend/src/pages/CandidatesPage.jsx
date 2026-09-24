import { useState, useMemo } from 'react'
import { Link } from 'react-router-dom'
import { MapPin, Clock, ChevronRight } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { candidatesApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import SearchInput from '../components/common/SearchInput.jsx'
import { LoadingState, ErrorState, EmptyState } from '../components/common/States.jsx'

export default function CandidatesPage() {
  const [q, setQ] = useState('')
  const { data, loading, error, refetch } = useFetch(() => candidatesApi.getAll(), [])

  const candidates = useMemo(() => {
    const list = data?.data ?? []
    if (!q.trim()) return list
    const lower = q.toLowerCase()
    return list.filter(c =>
      c.name?.toLowerCase().includes(lower) ||
      c.location?.toLowerCase().includes(lower) ||
      c.email?.toLowerCase().includes(lower)
    )
  }, [data, q])

  return (
    <div>
      <PageHeader title="Candidates" subtitle={`${data?.data?.length ?? 0} candidates in the graph`} />

      <div className="mb-4">
        <SearchInput value={q} onChange={setQ} placeholder="Filter by name, location…" className="max-w-sm" />
      </div>

      {loading && <LoadingState message="Loading candidates…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}

      {!loading && !error && candidates.length === 0 && (
        <EmptyState message={q ? `No candidates matching "${q}"` : 'No candidates found.'} />
      )}

      {!loading && !error && candidates.length > 0 && (
        <div className="grid sm:grid-cols-2 xl:grid-cols-3 gap-4">
          {candidates.map(c => (
            <Link key={c.id} to={`/candidates/${c.id}`} className="card p-5 hover:shadow-md transition-shadow group">
              <div className="flex items-start justify-between">
                <div className="w-10 h-10 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-semibold text-sm flex-shrink-0">
                  {c.name?.split(' ').map(n => n[0]).join('').slice(0, 2)}
                </div>
                <ChevronRight size={16} className="text-gray-300 group-hover:text-brand-500 transition-colors mt-1" />
              </div>
              <h3 className="mt-3 font-semibold text-gray-900">{c.name}</h3>
              <p className="text-xs text-gray-400 mt-0.5">{c.email}</p>
              <div className="flex items-center gap-3 mt-3 text-xs text-gray-500">
                {c.location && (
                  <span className="flex items-center gap-1"><MapPin size={11} />{c.location}</span>
                )}
                {c.yearsExp != null && (
                  <span className="flex items-center gap-1"><Clock size={11} />{c.yearsExp}y exp</span>
                )}
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
