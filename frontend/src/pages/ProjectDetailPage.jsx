import { useParams, Link } from 'react-router-dom'
import { Cpu, Users } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { projectsApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import BackButton from '../components/common/BackButton.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState } from '../components/common/States.jsx'

export default function ProjectDetailPage() {
  const { id } = useParams()
  const { data, loading, error, refetch } = useFetch(() => projectsApi.getById(id), [id])
  const p = data?.data

  return (
    <div>
      <BackButton to="/projects" label="All Projects" />
      {loading && <LoadingState message="Loading project…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}

      {p && (
        <>
          <div className="card p-6 mb-5">
            <div className="flex items-start gap-3 mb-2">
              <Badge label={p.status} />
              {p.domain && <Badge label={p.domain} />}
            </div>
            <PageHeader title={p.name} />
          </div>

          <div className="grid sm:grid-cols-2 gap-5">
            {/* Technologies */}
            <div className="card p-5">
              <h2 className="font-semibold text-gray-800 mb-3 flex items-center gap-2"><Cpu size={15} /> Technologies Used</h2>
              {p.technologies?.length > 0 ? (
                <div className="flex flex-wrap gap-2">
                  {p.technologies.map(t => (
                    <span key={t.id} className="px-3 py-1.5 bg-sky-50 text-sky-700 text-sm rounded-lg font-medium">
                      {t.name}
                      {t.type && <span className="ml-1 text-xs text-sky-400">· {t.type}</span>}
                    </span>
                  ))}
                </div>
              ) : <p className="text-sm text-gray-400">No technologies recorded.</p>}
            </div>

            {/* Contributors */}
            <div className="card p-5">
              <h2 className="font-semibold text-gray-800 mb-3 flex items-center gap-2"><Users size={15} /> Contributors</h2>
              {p.contributors?.length > 0 ? (
                <div className="space-y-2">
                  {p.contributors.map(u => (
                    <Link key={u.id} to={`/candidates/${u.id}`} className="flex items-center gap-3 py-2 px-3 rounded-lg hover:bg-gray-50 group transition-colors">
                      <div className="w-7 h-7 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center text-xs font-semibold flex-shrink-0">
                        {u.name?.split(' ').map(n => n[0]).join('').slice(0, 2)}
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-800 group-hover:text-brand-600">{u.name}</p>
                        {u.contributorRole && <p className="text-xs text-gray-400">{u.contributorRole}</p>}
                      </div>
                    </Link>
                  ))}
                </div>
              ) : <p className="text-sm text-gray-400">No contributors recorded.</p>}
            </div>
          </div>
        </>
      )}
    </div>
  )
}
