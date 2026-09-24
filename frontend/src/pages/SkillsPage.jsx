import { useState, useMemo } from 'react'
import { Link } from 'react-router-dom'
import { ChevronRight } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { skillsApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import SearchInput from '../components/common/SearchInput.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState, EmptyState } from '../components/common/States.jsx'

export default function SkillsPage() {
  const [q, setQ] = useState('')
  const [cat, setCat] = useState('All')
  const { data, loading, error, refetch } = useFetch(() => skillsApi.getAll(), [])

  const skills = data?.data ?? []
  const categories = useMemo(() => ['All', ...new Set(skills.map(s => s.category).filter(Boolean))], [skills])

  const filtered = useMemo(() => {
    let list = skills
    if (cat !== 'All') list = list.filter(s => s.category === cat)
    if (q.trim()) list = list.filter(s => s.name?.toLowerCase().includes(q.toLowerCase()))
    return list
  }, [skills, cat, q])

  return (
    <div>
      <PageHeader title="Skills" subtitle={`${skills.length} skills in the graph`} />

      <div className="flex flex-wrap gap-3 mb-4">
        <SearchInput value={q} onChange={setQ} placeholder="Filter skills…" className="w-56" />
        <div className="flex flex-wrap gap-1.5">
          {categories.map(c => (
            <button
              key={c}
              onClick={() => setCat(c)}
              className={`px-3 py-1.5 text-xs font-medium rounded-full border transition-colors ${
                cat === c
                  ? 'bg-brand-600 text-white border-brand-600'
                  : 'bg-white text-gray-600 border-gray-200 hover:border-brand-300'
              }`}
            >
              {c}
            </button>
          ))}
        </div>
      </div>

      {loading && <LoadingState message="Loading skills…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}
      {!loading && !error && filtered.length === 0 && <EmptyState message="No skills match your filter." />}

      {!loading && !error && filtered.length > 0 && (
        <div className="card divide-y divide-gray-100">
          {filtered.map(sk => (
            <Link key={sk.id} to={`/skills/${sk.id}`} className="flex items-center justify-between px-5 py-3.5 hover:bg-gray-50 transition-colors group">
              <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-gray-800 group-hover:text-brand-600">{sk.name}</span>
                <Badge label={sk.category} />
              </div>
              <ChevronRight size={15} className="text-gray-300 group-hover:text-brand-500" />
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
