import { useParams, Link } from 'react-router-dom'
import { MapPin, Clock, Mail, Star, FolderKanban } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { candidatesApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import BackButton from '../components/common/BackButton.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState } from '../components/common/States.jsx'

export default function CandidateDetailPage() {
  const { id } = useParams()
  const { data, loading, error, refetch } = useFetch(() => candidatesApi.getById(id), [id])
  const c = data?.data

  return (
    <div>
      <BackButton to="/candidates" label="All Candidates" />

      {loading && <LoadingState message="Loading candidate…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}

      {c && (
        <>
          <div className="card p-6 mb-5">
            <div className="flex items-start gap-4">
              <div className="w-14 h-14 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-bold text-lg flex-shrink-0">
                {c.name?.split(' ').map(n => n[0]).join('').slice(0, 2)}
              </div>
              <div className="flex-1 min-w-0">
                <PageHeader title={c.name} />
                <div className="flex flex-wrap gap-4 text-sm text-gray-500 -mt-4">
                  {c.email    && <span className="flex items-center gap-1.5"><Mail size={13} />{c.email}</span>}
                  {c.location && <span className="flex items-center gap-1.5"><MapPin size={13} />{c.location}</span>}
                  {c.yearsExp != null && <span className="flex items-center gap-1.5"><Clock size={13} />{c.yearsExp} years experience</span>}
                </div>
              </div>
              <Link
                to={`/recommendations?userId=${id}`}
                className="btn-primary flex-shrink-0"
              >
                <Star size={14} /> Career Fit
              </Link>
            </div>
          </div>

          {/* Skills */}
          <div className="card p-5 mb-5">
            <h2 className="font-semibold text-gray-800 mb-3">Skills</h2>
            {c.skills?.length > 0 ? (
              <div className="space-y-2">
                {c.skills.map(sk => (
                  <Link key={sk.id} to={`/skills/${sk.id}`} className="flex items-center justify-between py-2 px-3 rounded-lg hover:bg-gray-50 transition-colors group">
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-medium text-gray-800 group-hover:text-brand-600">{sk.name}</span>
                      <Badge label={sk.category} />
                    </div>
                    <div className="flex items-center gap-2 text-xs text-gray-400">
                      {sk.level && <Badge label={sk.level} />}
                      {sk.years != null && <span>{sk.years}y</span>}
                    </div>
                  </Link>
                ))}
              </div>
            ) : (
              <p className="text-sm text-gray-400">No skills recorded.</p>
            )}
          </div>

          {/* Projects */}
          <div className="card p-5">
            <h2 className="font-semibold text-gray-800 mb-3 flex items-center gap-2">
              <FolderKanban size={16} /> Projects
            </h2>
            {c.projects?.length > 0 ? (
              <div className="space-y-2">
                {c.projects.map(p => (
                  <Link key={p.id} to={`/projects/${p.id}`} className="flex items-center justify-between py-2 px-3 rounded-lg hover:bg-gray-50 transition-colors group">
                    <div>
                      <span className="text-sm font-medium text-gray-800 group-hover:text-brand-600">{p.name}</span>
                      {p.role && <span className="ml-2 text-xs text-gray-400">as {p.role}</span>}
                    </div>
                    <div className="flex items-center gap-2">
                      {p.domain && <Badge label={p.domain} />}
                      {p.status && <Badge label={p.status} />}
                    </div>
                  </Link>
                ))}
              </div>
            ) : (
              <p className="text-sm text-gray-400">No projects recorded.</p>
            )}
          </div>
        </>
      )}
    </div>
  )
}
