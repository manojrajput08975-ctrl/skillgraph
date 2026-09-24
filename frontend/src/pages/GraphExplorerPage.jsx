import { useState, useRef, useEffect, useCallback } from 'react'
import { graphApi, candidatesApi } from '../api/index.js'
import { useFetch } from '../hooks/useFetch.js'
import PageHeader from '../components/common/PageHeader.jsx'
import { LoadingState, ErrorState, EmptyState } from '../components/common/States.jsx'
import Spinner from '../components/common/Spinner.jsx'
import { Network, Table2, GitBranch, X } from 'lucide-react'

const NODE_COLORS = {
  User:       '#6366f1',
  Skill:      '#a855f7',
  Project:    '#22c55e',
  Technology: '#0ea5e9',
  JobRole:    '#f59e0b',
  Company:    '#14b8a6',
  Resource:   '#f97316',
  Node:       '#94a3b8',
}

const LINK_COLORS = {
  USER_WORKED_ON:            '#6366f1',
  PROJECT_USES:              '#22c55e',
  TECH_REQUIRES:             '#0ea5e9',
  SKILL_REQUIRED_FOR:        '#a855f7',
  USER_HAS_SKILL:            '#818cf8',
  SKILL_RELATED_TO:          '#c084fc',
  ROLE_AT:                   '#f59e0b',
  RESOURCE_TEACHES:          '#f97316',
  USER_RECOMMENDED_RESOURCE: '#fb923c',
}

function Legend() {
  return (
    <div className="px-5 py-3 flex flex-wrap gap-3 border-b border-gray-100 bg-gray-50">
      {Object.entries(NODE_COLORS).filter(([k]) => k !== 'Node').map(([type, color]) => (
        <span key={type} className="flex items-center gap-1.5 text-xs text-gray-600">
          <span className="w-2.5 h-2.5 rounded-full inline-block" style={{ background: color }} />
          {type}
        </span>
      ))}
    </div>
  )
}

function NodeInfoPanel({ node, onClose }) {
  if (!node) return null
  const props = node.properties ?? {}
  const skip = new Set(['id'])
  const entries = Object.entries(props).filter(([k]) => !skip.has(k))

  return (
    <div className="absolute top-3 right-3 z-20 w-64 bg-white rounded-xl border border-gray-200 shadow-lg">
      <div className="flex items-center justify-between px-4 py-3 border-b border-gray-100">
        <div>
          <span className="text-xs font-semibold uppercase tracking-wide"
            style={{ color: NODE_COLORS[node.type] ?? NODE_COLORS.Node }}>
            {node.type}
          </span>
          <p className="font-semibold text-gray-800 text-sm mt-0.5">{node.name}</p>
        </div>
        <button onClick={onClose} className="text-gray-400 hover:text-gray-600 transition-colors">
          <X size={15} />
        </button>
      </div>
      <div className="px-4 py-3 space-y-1.5">
        {entries.map(([k, v]) => (
          <div key={k} className="flex justify-between text-xs">
            <span className="text-gray-500 capitalize">{k.replace(/([A-Z])/g, ' $1')}</span>
            <span className="text-gray-800 font-medium text-right max-w-32 truncate">{String(v)}</span>
          </div>
        ))}
        {entries.length === 0 && <p className="text-xs text-gray-400">No additional properties.</p>}
      </div>
    </div>
  )
}

function ForceGraphCanvas({ graphData, onNodeClick, height = 520 }) {
  const containerRef = useRef(null)
  const [ForceGraph, setForceGraph] = useState(null)
  const [width, setWidth] = useState(800)

  useEffect(() => {
    import('react-force-graph-2d').then(m => setForceGraph(() => m.default))
  }, [])

  useEffect(() => {
    if (!containerRef.current) return
    const ro = new ResizeObserver(entries => {
      setWidth(entries[0].contentRect.width)
    })
    ro.observe(containerRef.current)
    setWidth(containerRef.current.offsetWidth)
    return () => ro.disconnect()
  }, [])

  const paintNode = useCallback((node, ctx, globalScale) => {
    const r = 6
    ctx.beginPath()
    ctx.arc(node.x, node.y, r, 0, 2 * Math.PI)
    ctx.fillStyle = NODE_COLORS[node.type] ?? NODE_COLORS.Node
    ctx.fill()

    const fontSize = Math.max(11 / globalScale, 2.5)
    ctx.font = `${fontSize}px Inter, sans-serif`
    ctx.fillStyle = '#374151'
    ctx.textAlign = 'center'
    ctx.fillText(node.name, node.x, node.y + r + fontSize + 1)
  }, [])

  const linkColor = useCallback(link => LINK_COLORS[link.label] ?? '#cbd5e1', [])

  if (!ForceGraph) return <LoadingState message="Loading graph renderer…" />

  return (
    <div ref={containerRef} style={{ height }}>
      <ForceGraph
        width={width}
        height={height}
        graphData={graphData}
        nodeLabel="name"
        nodeColor={n => NODE_COLORS[n.type] ?? NODE_COLORS.Node}
        nodeRelSize={6}
        linkLabel="label"
        linkColor={linkColor}
        linkDirectionalArrowLength={5}
        linkDirectionalArrowRelPos={1}
        linkCurvature={0.1}
        nodeCanvasObject={paintNode}
        nodeCanvasObjectMode={() => 'replace'}
        onNodeClick={onNodeClick}
        cooldownTicks={120}
        d3AlphaDecay={0.02}
        d3VelocityDecay={0.3}
      />
    </div>
  )
}

// ── Tab: Career Path ──────────────────────────────────────────────────────────

function CareerPathGraph() {
  const { data, loading, error, refetch } = useFetch(() => graphApi.careerPath(), [])
  const [selectedNode, setSelectedNode] = useState(null)

  const raw = data?.data
  const graphData = raw ? {
    nodes: raw.nodes.map(n => ({ id: n.id, name: n.label, type: n.type, properties: n.properties })),
    links: raw.edges.map(e => ({ source: e.source, target: e.target, label: e.type })),
  } : null

  return (
    <div className="card">
      <div className="px-5 py-4 border-b border-gray-100">
        <h2 className="font-semibold text-gray-800">Career Path Graph</h2>
        <p className="text-xs text-gray-400 mt-0.5">
          User → Project → Technology → Skill → JobRole — click any node to inspect
        </p>
      </div>
      <Legend />
      <div className="relative">
        {loading && <LoadingState message="Loading career path graph…" />}
        {error   && <ErrorState message={error} onRetry={refetch} />}
        {!loading && !error && !graphData && <EmptyState message="No career path data found." />}
        {!loading && !error && graphData && (
          <>
            <NodeInfoPanel node={selectedNode} onClose={() => setSelectedNode(null)} />
            <ForceGraphCanvas
              graphData={graphData}
              onNodeClick={node => setSelectedNode(node)}
              height={560}
            />
          </>
        )}
      </div>
      {!loading && !error && graphData && (
        <div className="px-5 py-3 border-t border-gray-100 bg-gray-50 flex flex-wrap gap-4 text-xs text-gray-500">
          <span>{graphData.nodes.length} nodes</span>
          <span>{graphData.links.length} relationships</span>
          <span className="text-gray-400">Click a node to see its properties</span>
        </div>
      )}
    </div>
  )
}

// ── Tab: Neighbourhood Explorer ───────────────────────────────────────────────

function NeighbourhoodExplorer() {
  const [inputId, setInputId] = useState('')
  const [graphData, setGraphData] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [selectedNode, setSelectedNode] = useState(null)

  const { data: candidatesData } = useFetch(() => candidatesApi.getAll(), [])
  const candidates = candidatesData?.data ?? []

  async function explore(id) {
    if (!id) return
    setLoading(true); setError(null); setSelectedNode(null)
    try {
      const res = await graphApi.neighbourhood(id)
      const raw = res.data?.data
      if (!raw || raw.nodes.length === 0) { setGraphData(null); return }
      setGraphData({
        nodes: raw.nodes.map(n => ({ id: n.id, name: n.label, type: n.type, properties: n.properties })),
        links: raw.edges.map(e => ({ source: e.source, target: e.target, label: e.type })),
      })
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card">
      <div className="px-5 py-4 border-b border-gray-100">
        <h2 className="font-semibold text-gray-800">Neighbourhood Explorer</h2>
        <p className="text-xs text-gray-400 mt-0.5">Visualise up to 2 hops around any node</p>
      </div>

      <div className="p-5 flex flex-wrap gap-3 items-end border-b border-gray-100">
        <div className="flex-1 min-w-48">
          <label className="block text-xs font-medium text-gray-600 mb-1">Quick-pick a candidate</label>
          <select className="input" value={inputId} onChange={e => setInputId(e.target.value)}>
            <option value="">— select —</option>
            {candidates.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
        </div>
        <div className="flex-1 min-w-48">
          <label className="block text-xs font-medium text-gray-600 mb-1">Or enter any node ID</label>
          <input
            className="input"
            placeholder="e.g. skill-1, proj-3, tech-2…"
            value={inputId}
            onChange={e => setInputId(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && explore(inputId)}
          />
        </div>
        <button
          onClick={() => explore(inputId)}
          disabled={!inputId || loading}
          className="btn-primary"
        >
          {loading ? <Spinner size="sm" /> : <Network size={14} />}
          Explore
        </button>
      </div>

      <Legend />

      <div className="relative">
        {loading && (
          <div className="flex items-center justify-center" style={{ height: 480 }}>
            <Spinner size="lg" />
          </div>
        )}
        {!loading && error && <ErrorState message={error} />}
        {!loading && !error && !graphData && (
          <EmptyState
            message="Select a candidate or enter a node ID (e.g. skill-1, proj-3, tech-2) to explore."
            icon={Network}
          />
        )}
        {!loading && !error && graphData && (
          <>
            <NodeInfoPanel node={selectedNode} onClose={() => setSelectedNode(null)} />
            <ForceGraphCanvas
              graphData={graphData}
              onNodeClick={node => setSelectedNode(node)}
              height={480}
            />
          </>
        )}
      </div>

      {!loading && !error && graphData && (
        <div className="px-5 py-3 border-t border-gray-100 bg-gray-50 flex flex-wrap gap-4 text-xs text-gray-500">
          <span>{graphData.nodes.length} nodes</span>
          <span>{graphData.links.length} relationships</span>
          <span className="text-gray-400">Click a node to see its properties</span>
        </div>
      )}
    </div>
  )
}

// ── Tab: Traversal Table ──────────────────────────────────────────────────────

function TraversalTable() {
  const { data, loading, error, refetch } = useFetch(() => graphApi.traversal(), [])
  const rows = data?.data ?? []

  return (
    <div className="card">
      <div className="px-5 py-4 border-b border-gray-100 flex items-center justify-between">
        <div>
          <h2 className="font-semibold text-gray-800">4-Hop Traversal Table</h2>
          <p className="text-xs text-gray-400 mt-0.5">User → Project → Technology → Skill → JobRole</p>
        </div>
        <span className="text-xs text-gray-400 bg-gray-100 px-2 py-1 rounded-full">
          {rows.length} paths
        </span>
      </div>

      {loading && <LoadingState message="Running traversal…" />}
      {error   && <ErrorState message={error} onRetry={refetch} />}
      {!loading && !error && rows.length === 0 && <EmptyState message="No traversal paths found." />}

      {!loading && !error && rows.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100 bg-gray-50">
                {[
                  { label: 'User',       color: 'text-indigo-600' },
                  { label: 'Project',    color: 'text-green-600'  },
                  { label: 'Technology', color: 'text-sky-600'    },
                  { label: 'Skill',      color: 'text-purple-600' },
                  { label: 'Role',       color: 'text-amber-600'  },
                ].map(({ label, color }) => (
                  <th key={label} className={`px-4 py-2.5 text-left text-xs font-semibold uppercase tracking-wide ${color}`}>
                    {label}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {rows.map((r, i) => (
                <tr key={i} className="hover:bg-gray-50 transition-colors">
                  <td className="px-4 py-2.5 font-medium text-indigo-700">{r.user}</td>
                  <td className="px-4 py-2.5 text-green-700">{r.project}</td>
                  <td className="px-4 py-2.5 text-sky-700">{r.technology}</td>
                  <td className="px-4 py-2.5 text-purple-700">{r.skill}</td>
                  <td className="px-4 py-2.5 text-amber-700">{r.role}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

// ── Page ──────────────────────────────────────────────────────────────────────

const TABS = [
  { id: 'career',        label: 'Career Path Graph',  icon: GitBranch },
  { id: 'neighbourhood', label: 'Neighbourhood',       icon: Network   },
  { id: 'traversal',     label: 'Traversal Table',     icon: Table2    },
]

export default function GraphExplorerPage() {
  const [tab, setTab] = useState('career')

  return (
    <div>
      <PageHeader
        title="Graph Explorer"
        subtitle="Traverse and visualise the SkillGraph knowledge graph"
      />

      <div className="flex gap-2 mb-5 flex-wrap">
        {TABS.map(({ id, label, icon: Icon }) => (
          <button key={id} onClick={() => setTab(id)}
            className={`flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-lg border transition-colors ${
              tab === id
                ? 'bg-brand-600 text-white border-brand-600'
                : 'bg-white text-gray-600 border-gray-200 hover:border-brand-300'
            }`}
          >
            <Icon size={15} /> {label}
          </button>
        ))}
      </div>

      {tab === 'career'        && <CareerPathGraph />}
      {tab === 'neighbourhood' && <NeighbourhoodExplorer />}
      {tab === 'traversal'     && <TraversalTable />}
    </div>
  )
}
