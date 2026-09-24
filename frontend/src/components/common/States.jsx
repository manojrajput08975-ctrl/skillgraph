import Spinner from './Spinner.jsx'
import { AlertCircle, SearchX, RefreshCw } from 'lucide-react'

export function LoadingState({ message = 'Loading…' }) {
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-3 text-gray-400">
      <Spinner size="lg" />
      <p className="text-sm">{message}</p>
    </div>
  )
}

export function ErrorState({ message, onRetry }) {
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-3">
      <AlertCircle size={36} className="text-red-400" />
      <p className="text-sm text-gray-600 text-center max-w-sm">{message || 'Something went wrong.'}</p>
      {onRetry && (
        <button onClick={onRetry} className="btn-secondary mt-1">
          <RefreshCw size={14} /> Retry
        </button>
      )}
    </div>
  )
}

export function EmptyState({ message = 'No results found.', icon: Icon = SearchX }) {
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-3 text-gray-400">
      <Icon size={36} />
      <p className="text-sm">{message}</p>
    </div>
  )
}
