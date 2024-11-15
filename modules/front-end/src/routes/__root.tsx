import { createRootRoute, Outlet } from '@tanstack/react-router'
import React, {Suspense} from "react";
import {isProd} from "../lib/utils.ts";
import {ThemeProvider} from "../context/theme-provider.tsx";
import Navbar from "../components/global/navbar.tsx";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

const queryClient = new QueryClient()

export const Route = createRootRoute({
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
                    <Navbar/>
                    <Outlet />
                    <Suspense>
                        <TanStackRouterDevtools />
                        <TanStackReactQueryDevtools/>
                    </Suspense>
                </ThemeProvider>
            </QueryClientProvider>
        )
    }

})