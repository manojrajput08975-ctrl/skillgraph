import { useParams, Link } from 'react-router-dom'
import { Building2, Zap } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { rolesApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import BackButton from '../components/common/BackButton.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState } from '../components/common/States.jsx'

export default function RoleDetailPage() {
  const { id } = useParams()
  const { data, loading, error, refetch } = useFetch(() => rolesApi.getById(id), [id])
  const r = data?.data

  return (
    <div>
      <BackButton to="/roles" label="All Roles" />
      {loading && <LoadingState message="Loading role…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}

      {r && (
        <>
          <div className="flex items-center gap-3 mb-6">
            <PageHeader title={r.title} />
            <Badge label={r.level} />
          </div>

          <div className="grid sm:grid-cols-2 gap-5">
            <div className="card p-5">
              <h2 className="font-semibold text-gray-800 mb-3 flex items-center gap-2"><Zap size={15} /> Required Skills</h2>
              {r.requiredSkills?.length > 0 ? (
                <div className="flex flex-wrap gap-2">
                  {r.requiredSkills.map(sk => (
                    <Link key={sk.id} to={`/skills/${sk.id}`}
                      className="flex items-center gap-1.5 px-3 py-1.5 bg-gray-50 rounded-lg hover:bg-brand-50 hover:text-brand-700 text-sm transition-colors">
                      {sk.name} <Badge label={sk.category} />
                    </Link>
                  ))}
                </div>
              ) : <p className="text-sm text-gray-400">No skills listed.</p>}
            </div>

            <div className="card p-5">
              <h2 className="font-semibold text-gray-800 mb-3 flex items-center gap-2"><Building2 size={15} /> Hiring Companies</h2>
              {r.companies?.length > 0 ? (
                <div className="space-y-2">
                  {r.companies.map(c => (
                    <div key={c.id} className="flex items-center justify-between py-2 px-3 rounded-lg bg-gray-50">
                      <span className="text-sm font-medium text-gray-800">{c.name}</span>
                      <div className="flex gap-2">
                        {c.industry && <Badge label={c.industry} />}
                        {c.size && <span className="text-xs text-gray-400">{c.size}</span>}
                      </div>
                    </div>
                  ))}
                </div>
              ) : <p className="text-sm text-gray-400">No companies listed.</p>}
            </div>
          </div>
        </>
      )}
    </div>
  )
}
