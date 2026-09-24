import { useParams, Link } from 'react-router-dom'
import { useFetch } from '../hooks/useFetch.js'
import { skillsApi } from '../api/index.js'
import PageHeader from '../components/common/PageHeader.jsx'
import BackButton from '../components/common/BackButton.jsx'
import Badge from '../components/common/Badge.jsx'
import { LoadingState, ErrorState } from '../components/common/States.jsx'

export default function SkillDetailPage() {
  const { id } = useParams()
  const { data, loading, error, refetch } = useFetch(() => skillsApi.getById(id), [id])
  const sk = data?.data

  return (
    <div>
      <BackButton to="/skills" label="All Skills" />
      {loading && <LoadingState message="Loading skill…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}

      {sk && (
        <>
          <div className="flex items-center gap-3 mb-6">
            <PageHeader title={sk.name} />
            <Badge label={sk.category} />
          </div>

          <div className="grid sm:grid-cols-2 gap-5">
            {/* Related skills */}
            <div className="card p-5">
              <h2 className="font-semibold text-gray-800 mb-3">Related Skills</h2>
              {sk.relatedSkills?.length > 0 ? (
                <div className="flex flex-wrap gap-2">
                  {sk.relatedSkills.map(r => (
                    <Link key={r.id} to={`/skills/${r.id}`} className="flex items-center gap-1.5 px-3 py-1.5 bg-gray-50 rounded-lg hover:bg-brand-50 hover:text-brand-700 text-sm transition-colors">
                      {r.name} <Badge label={r.category} />
                    </Link>
                  ))}
                </div>
              ) : <p className="text-sm text-gray-400">No related skills.</p>}
            </div>

            {/* Required for roles */}
            <div className="card p-5">
              <h2 className="font-semibold text-gray-800 mb-3">Required For Roles</h2>
              {sk.roles?.length > 0 ? (
                <div className="space-y-2">
                  {sk.roles.map(r => (
                    <Link key={r.id} to={`/roles/${r.id}`} className="flex items-center justify-between py-2 px-3 rounded-lg hover:bg-gray-50 group transition-colors">
                      <span className="text-sm font-medium text-gray-800 group-hover:text-brand-600">{r.title}</span>
                      <Badge label={r.level} />
                    </Link>
                  ))}
                </div>
              ) : <p className="text-sm text-gray-400">Not required for any role.</p>}
            </div>
          </div>
        </>
      )}
    </div>
  )
}
