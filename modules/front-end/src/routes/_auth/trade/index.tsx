import { createFileRoute, Link } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/trade/')({
  component: Page,
})

function Page() {
  return (
    <div>
      <Link to="/trade/$id" params={{ id: 'bitcoin' }}>
        Bitcoin
      </Link>
    </div>
  )
}
