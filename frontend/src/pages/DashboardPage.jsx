import { Link } from 'react-router-dom'
import { Users, Zap, FolderKanban, Cpu, Briefcase, Building2, BookOpen, GitBranch } from 'lucide-react'
import { useFetch } from '../hooks/useFetch.js'
import { dashboardApi } from '../api/index.js'
import StatCard from '../components/common/StatCard.jsx'
import PageHeader from '../components/common/PageHeader.jsx'
import { LoadingState, ErrorState } from '../components/common/States.jsx'

const QUICK_LINKS = [
  { to: '/candidates',  label: 'Browse Candidates',   icon: Users,        color: 'bg-brand-50 text-brand-600' },
  { to: '/skills',      label: 'Explore Skills',       icon: Zap,          color: 'bg-purple-50 text-purple-600' },
  { to: '/projects',    label: 'View Projects',        icon: FolderKanban, color: 'bg-green-50 text-green-600' },
  { to: '/roles',       label: 'Job Roles',            icon: Briefcase,    color: 'bg-amber-50 text-amber-600' },
  { to: '/recommendations', label: 'Career Fit',       icon: GitBranch,    color: 'bg-pink-50 text-pink-600' },
  { to: '/graph',       label: 'Graph Explorer',       icon: GitBranch,    color: 'bg-sky-50 text-sky-600' },
]

export default function DashboardPage() {
  const { data, loading, error, refetch } = useFetch(() => dashboardApi.getStats(), [])

  const stats = data?.data

  return (
    <div>
      <PageHeader
        title="Dashboard"
        subtitle="Overview of your SkillGraph knowledge base"
      />

      {loading && <LoadingState message="Loading dashboard…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}

      {stats && (
        <>
          <div className="grid grid-cols-2 sm:grid-cols-3 xl:grid-cols-4 gap-4 mb-8">
            <StatCard label="Candidates"    value={stats.totalCandidates}    icon={Users}       color="brand"  />
            <StatCard label="Skills"        value={stats.totalSkills}        icon={Zap}         color="purple" />
            <StatCard label="Projects"      value={stats.totalProjects}      icon={FolderKanban} color="green" />
            <StatCard label="Technologies"  value={stats.totalTechnologies}  icon={Cpu}         color="sky"   />
            <StatCard label="Job Roles"     value={stats.totalRoles}         icon={Briefcase}   color="amber" />
            <StatCard label="Companies"     value={stats.totalCompanies}     icon={Building2}   color="teal"  />
            <StatCard label="Resources"     value={stats.totalResources}     icon={BookOpen}    color="pink"  />
            <StatCard label="Relationships" value={stats.totalRelationships} icon={GitBranch}   color="indigo"/>
          </div>

          <h2 className="text-base font-semibold text-gray-700 mb-3">Quick Access</h2>
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
            {QUICK_LINKS.map(({ to, label, icon: Icon, color }) => (
              <Link
                key={to}
                to={to}
                className="card p-4 flex items-center gap-3 hover:shadow-md transition-shadow group"
              >
                <div className={`${color} p-2.5 rounded-lg`}>
                  <Icon size={18} />
                </div>
                <span className="text-sm font-medium text-gray-700 group-hover:text-gray-900">{label}</span>
              </Link>
            ))}
          </div>
        </>
      )}
    </div>
  )
}
