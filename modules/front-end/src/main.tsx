import { StrictMode } from 'react'
import ReactDOM from 'react-dom/client'
import { RouterProvider, createRouter } from '@tanstack/react-router'
import './index.css';
import '../node_modules/react-grid-layout/css/styles.css'
// import '../node_modules/react-resizable/css/styles.css'

// Import the generated route tree
import { routeTree } from './routeTree.gen.ts'
import {AuthProvider, useAuth} from "@/auth.tsx";

// Create a new router instance
const router = createRouter({
    routeTree,
    defaultPreload: "intent",
    context: {
        auth: undefined
    }
})

// Register the router instance for type safety
declare module '@tanstack/react-router' {
    interface Register {
        router: typeof router
    }
}

function InnerApp() {
    const auth = useAuth()
    return <RouterProvider router={router} context={{ auth }} />
}

function App() {
    return (
        <AuthProvider>
            <InnerApp />
        </AuthProvider>
    )
}

// Render the app
const rootElement = document.getElementById('root')!
if (!rootElement.innerHTML) {
    const root = ReactDOM.createRoot(rootElement)
    root.render(
        <StrictMode>
            <App/>
        </StrictMode>,
    )
}