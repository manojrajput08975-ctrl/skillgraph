import { useState, useEffect, useRef } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { Search, Users, Zap, FolderKanban, Cpu, Briefcase, Building2 } from 'lucide-react'
import { searchApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, EmptyState } from '../components/common/States.jsx'

const TYPE_ICON = {
  User:       { icon: Users,       color: 'text-brand-600 bg-brand-50' },
  Skill:      { icon: Zap,         color: 'text-purple-600 bg-purple-50' },
  Project:    { icon: FolderKanban,color: 'text-green-600 bg-green-50' },
  Technology: { icon: Cpu,         color: 'text-sky-600 bg-sky-50' },
  JobRole:    { icon: Briefcase,   color: 'text-amber-600 bg-amber-50' },
  Company:    { icon: Building2,   color: 'text-teal-600 bg-teal-50' },
}

const TYPE_ROUTE = {
  User:       (id) => `/candidates/${id}`,
  Skill:      (id) => `/skills/${id}`,
  Project:    (id) => `/projects/${id}`,
  Technology: (id) => `/technologies/${id}`,
  JobRole:    (id) => `/roles/${id}`,
  Company:    (id) => `/roles`,
}

export default function SearchPage() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [q, setQ] = useState(searchParams.get('q') ?? '')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [typeFilter, setTypeFilter] = useState('All')
  const debounce = useRef(null)

  useEffect(() => {
    if (debounce.current) clearTimeout(debounce.current)
    if (q.trim().length < 2) { setResults([]); return }
    debounce.current = setTimeout(async () => {
      setLoading(true); setError(null)
      try {
        const res = await searchApi.search(q.trim())
        setResults(res.data?.data ?? [])
        setSearchParams({ q: q.trim() }, { replace: true })
      } catch (e) {
        setError(e.message)
      } finally {
        setLoading(false)
      }
    }, 350)
    return () => clearTimeout(debounce.current)
  }, [q])

  const types = ['All', ...new Set(results.map(r => r.type))]
  const filtered = typeFilter === 'All' ? results : results.filter(r => r.type === typeFilter)

  return (
    <div>
      <PageHeader title="Search" subtitle="Find candidates, skills, projects, roles and more" />

      <div className="relative mb-5 max-w-xl">
        <Search size={16} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none" />
        <input
          autoFocus
          type="text"
          value={q}
          onChange={e => setQ(e.target.value)}
          placeholder="Search anything…"
          className="input pl-10 text-base py-3"
        />
      </div>

      {q.trim().length >= 2 && results.length > 0 && (
        <div className="flex flex-wrap gap-1.5 mb-4">
          {types.map(t => (
            <button key={t} onClick={() => setTypeFilter(t)}
              className={`px-3 py-1 text-xs font-medium rounded-full border transition-colors ${
                typeFilter === t ? 'bg-brand-600 text-white border-brand-600' : 'bg-white text-gray-600 border-gray-200 hover:border-brand-300'
              }`}
            >{t} {t !== 'All' && `(${results.filter(r => r.type === t).length})`}</button>
          ))}
        </div>
      )}

      {loading && <LoadingState message="Searching…" />}

      {!loading && q.trim().length >= 2 && filtered.length === 0 && !error && (
        <EmptyState message={`No results for "${q}"`} />
      )}

      {!loading && filtered.length > 0 && (
        <div className="card divide-y divide-gray-100">
          {filtered.map(r => {
            const meta = TYPE_ICON[r.type] ?? { icon: Search, color: 'text-gray-500 bg-gray-50' }
            const Icon = meta.icon
            const href = TYPE_ROUTE[r.type]?.(r.id) ?? '/'
            return (
              <Link key={`${r.type}-${r.id}`} to={href} className="flex items-center gap-4 px-5 py-3.5 hover:bg-gray-50 transition-colors group">
                <div className={`${meta.color} p-2 rounded-lg flex-shrink-0`}>
                  <Icon size={15} />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-gray-900 group-hover:text-brand-600 truncate">{r.name}</p>
                  {r.subtitle && <p className="text-xs text-gray-400 truncate">{r.subtitle}</p>}
                </div>
                <Badge label={r.type} color="bg-gray-100 text-gray-500" />
              </Link>
            )
          })}
        </div>
      )}

      {!q.trim() && (
        <div className="text-center py-16 text-gray-400">
          <Search size={40} className="mx-auto mb-3 opacity-30" />
          <p className="text-sm">Type at least 2 characters to search</p>
        </div>
      )}
    </div>
  )
}
