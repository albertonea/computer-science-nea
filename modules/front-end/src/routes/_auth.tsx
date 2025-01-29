import {
    Outlet,
    createFileRoute,
    redirect,
} from '@tanstack/react-router'
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";
import {toast} from "sonner";

export const Route = createFileRoute('/_auth')({
    beforeLoad: ({ context, location }) => {
        if (!context.auth?.isAuthenticated) {
            throw redirect({
                to: '/login',
                search: {
                    redirect: location.href,
                },
            })
        }
    },
    component: AuthLayout,
})

function AuthLayout() {
    const auth = useAuth()
    useSubscription(`/stream/filledOrders/${auth.auth?.user.userId}`, (message) => {
        toast(`order filled ${message.body}`)
    })

    useSubscription(`/stream/errors/${auth.auth?.user.userId}`, (message) => {
        toast(`Error: ${message.body}`)
    })
    return (
        <Outlet />
    )
}
