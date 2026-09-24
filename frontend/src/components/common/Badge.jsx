const COLORS = {
  // categories
  Backend:      'bg-blue-100 text-blue-700',
  Frontend:     'bg-purple-100 text-purple-700',
  'AI/ML':      'bg-pink-100 text-pink-700',
  Database:     'bg-amber-100 text-amber-700',
  DevOps:       'bg-green-100 text-green-700',
  Cloud:        'bg-sky-100 text-sky-700',
  Data:         'bg-teal-100 text-teal-700',
  API:          'bg-indigo-100 text-indigo-700',
  Architecture: 'bg-orange-100 text-orange-700',
  // levels
  expert:       'bg-brand-100 text-brand-700',
  advanced:     'bg-blue-100 text-blue-700',
  beginner:     'bg-gray-100 text-gray-600',
  senior:       'bg-brand-100 text-brand-700',
  mid:          'bg-blue-100 text-blue-700',
  // status
  active:       'bg-green-100 text-green-700',
  completed:    'bg-gray-100 text-gray-600',
}

export default function Badge({ label, color }) {
  const cls = color || COLORS[label] || 'bg-gray-100 text-gray-600'
  return <span className={`badge ${cls}`}>{label}</span>
}
