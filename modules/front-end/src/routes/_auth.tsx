import {
    Outlet,
    createFileRoute,
    redirect,
} from '@tanstack/react-router'
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";
import {toast} from "sonner";
import {Order} from "@/api/orders.ts";

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
        const json:Order = JSON.parse(message.body)
        if (json.remainingQuantity === 0) {
            toast(`Order Filled`, {
                description: `${json.side} ${json.initialQuantity} ${json.ticker} @ ${(json.executedValue/json.initialQuantity)/100}$ avg`
            })
        } else {
            const filledQty = json.initialQuantity - json.remainingQuantity
            toast("Order Partially Filled", {
                description: `${json.side} ${filledQty} ${json.ticker} @ ${(json.executedValue/filledQty)/100}$ avg`,
            })
        }
    })

    useSubscription(`/stream/errors/${auth.auth?.user.userId}`, (message) => {
        toast(`Error: ${message.body}`)
    })
    return (
        <Outlet />
    )
}
