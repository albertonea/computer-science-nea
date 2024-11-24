import {createRootRouteWithContext, Outlet} from '@tanstack/react-router'
import React, {Suspense} from "react";
import {isProd} from "../lib/utils.ts";
import {ThemeProvider} from "../context/theme-provider.tsx";
import Navbar from "../components/global/navbar.tsx";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {AuthContext} from "@/auth.tsx";

const queryClient = new QueryClient()

interface RouterContext {
    auth: AuthContext
}

export const Route = createRootRouteWithContext<RouterContext>()({
    component: () => {
        const TanStackRouterDevtools =
            isProd()
                ? () => null // Render nothing in production
                : React.lazy(() =>
                        // Lazy load in development
                        import('@tanstack/router-devtools').then((res) => ({
                            default: res.TanStackRouterDevtools,
                            // For Embedded Mode
                            // default: res.TanStackRouterDevtoolsPanel
                        })),
                )

        const TanStackReactQueryDevtools =
            isProd()
            ? () => null // Render nothing in production
            : React.lazy(() =>
                // Lazy load in development
                import('@tanstack/react-query-devtools').then((res) => ({
                    default: res.ReactQueryDevtools,
                    // For Embedded Mode
                    // default: res.TanStackRouterDevtoolsPanel
                })),
            )

        return (
            <QueryClientProvider client={queryClient}>
                <ThemeProvider>
                    <div className="flex flex-col min-h-screen">
                    <Navbar/>
                    <main className="grow shrink basis-1 w-full">
                        <Outlet />
                    </main>
                    </div>
                    <Suspense>
                        <TanStackRouterDevtools />
                        <TanStackReactQueryDevtools/>
                    </Suspense>
                </ThemeProvider>
            </QueryClientProvider>
        )
    }

})