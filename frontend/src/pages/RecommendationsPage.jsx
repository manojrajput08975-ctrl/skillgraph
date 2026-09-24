import { useState, useEffect } from 'react'
import { useSearchParams, Link } from 'react-router-dom'
import { Star, AlertCircle, ChevronDown, ChevronUp } from 'lucide-react'
import { candidatesApi, recommendationsApi } from '../api/index.js'
import { useFetch } from '../hooks/useFetch.js'
import PageHeader from '../components/common/PageHeader.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState, EmptyState } from '../components/common/States.jsx'
import Spinner from '../components/common/Spinner.jsx'

function MatchBar({ pct }) {
  const color = pct >= 80 ? 'bg-green-500' : pct >= 50 ? 'bg-amber-400' : 'bg-red-400'
  return (
    <div className="flex items-center gap-2">
      <div className="flex-1 h-2 bg-gray-100 rounded-full overflow-hidden">
        <div className={`h-full ${color} rounded-full transition-all`} style={{ width: `${pct}%` }} />
      </div>
      <span className="text-xs font-semibold text-gray-600 w-9 text-right">{pct}%</span>
    </div>
  )
}

function GapRow({ userId, roleId, roleTitle }) {
  const [open, setOpen] = useState(false)
  const [gaps, setGaps] = useState(null)
  const [loading, setLoading] = useState(false)

  async function load() {
    if (gaps) { setOpen(o => !o); return }
    setLoading(true)
    try {
      const res = await recommendationsApi.getGap(userId, roleId)
      setGaps(res.data?.data ?? [])
      setOpen(true)
    } finally { setLoading(false) }
  }

  const missing = gaps?.flatMap(g => g.missingSkills ?? []) ?? []

  return (
    <div>
      <button onClick={load} className="flex items-center gap-1.5 text-xs text-brand-600 hover:text-brand-800 mt-1 transition-colors">
        {loading ? <Spinner size="sm" /> : open ? <ChevronUp size={13} /> : <ChevronDown size={13} />}
        {open ? 'Hide' : 'Show'} skill gap
      </button>
      {open && gaps && (
        <div className="mt-2 p-3 bg-amber-50 rounded-lg">
          {missing.length === 0
            ? <p className="text-xs text-green-700 font-medium">✓ No skill gaps — fully qualified!</p>
            : (
              <div>
                <p className="text-xs font-semibold text-amber-700 mb-1.5 flex items-center gap-1">
                  <AlertCircle size={12} /> Missing skills for {roleTitle}:
                </p>
                <div className="flex flex-wrap gap-1.5">
                  {missing.map((s, i) => <Badge key={i} label={s} color="bg-amber-100 text-amber-700" />)}
                </div>
              </div>
            )
          }
        </div>
      )}
    </div>
  )
}

export default function RecommendationsPage() {
  const [searchParams] = useSearchParams()
  const [userId, setUserId] = useState(searchParams.get('userId') ?? '')

  const { data: candidatesData } = useFetch(() => candidatesApi.getAll(), [])
  const candidates = candidatesData?.data ?? []

  const {
    data: recsData, loading, error, refetch
  } = useFetch(
    () => userId ? recommendationsApi.getRoles(userId) : Promise.resolve({ data: null }),
    [userId]
  )
  const recs = recsData?.data ?? []

  return (
    <div>
      <PageHeader title="Career Fit" subtitle="Graph-based job role recommendations per candidate" />

      <div className="card p-5 mb-6">
        <label className="block text-sm font-medium text-gray-700 mb-2">Select Candidate</label>
        <select
          value={userId}
          onChange={e => setUserId(e.target.value)}
          className="input max-w-sm"
        >
          <option value="">— choose a candidate —</option>
          {candidates.map(c => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
      </div>

      {!userId && <EmptyState message="Select a candidate to see role recommendations." icon={Star} />}
      {userId && loading && <LoadingState message="Computing recommendations…" />}
      {userId && error   && <ErrorState message={error} onRetry={refetch} />}
      {userId && !loading && !error && recs.length === 0 && (
        <EmptyState message="No recommendations found. Candidate may have no skills recorded." />
      )}

      {userId && !loading && !error && recs.length > 0 && (
        <div className="space-y-3">
          {recs.map((rec, i) => (
            <div key={rec.roleId} className="card p-5">
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="text-xs font-bold text-gray-400">#{i + 1}</span>
                    <Link to={`/roles/${rec.roleId}`} className="font-semibold text-gray-900 hover:text-brand-600 transition-colors">
                      {rec.roleTitle}
                    </Link>
                    <Badge label={rec.level} />
                  </div>
                  <MatchBar pct={rec.matchPct} />
                  <p className="text-xs text-gray-400 mt-1">
                    {rec.matchedSkills} of {rec.totalRequired} required skills matched
                  </p>
                  <GapRow userId={userId} roleId={rec.roleId} roleTitle={rec.roleTitle} />
                </div>
                {rec.companies?.length > 0 && (
                  <div className="flex-shrink-0 text-right">
                    <p className="text-xs text-gray-400 mb-1">Hiring at</p>
                    {rec.companies.slice(0, 2).map(c => (
                      <p key={c.id} className="text-xs font-medium text-gray-700">{c.name}</p>
                    ))}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
