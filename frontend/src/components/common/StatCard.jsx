export default function StatCard({ label, value, icon: Icon, color = 'brand' }) {
  const bg   = `bg-${color}-50`
  const text = `text-${color}-600`
  return (
    <div className="card p-5 flex items-center gap-4">
      <div className={`${bg} ${text} p-3 rounded-xl`}>
        <Icon size={22} />
      </div>
      <div>
        <p className="text-2xl font-bold text-gray-900">{value ?? '—'}</p>
        <p className="text-sm text-gray-500 mt-0.5">{label}</p>
      </div>
    </div>
  )
}
