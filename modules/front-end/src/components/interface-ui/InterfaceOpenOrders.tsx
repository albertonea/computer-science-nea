import {useState} from "react";
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";

type OpenOrders = {
    price: number;
    initialQuantity: number;
    remainingQuantity: number;
    side: 'BUY' | 'SELL';
}[]


export default function InterfaceOpenOrders() {
    const [lastMessage, setLastMessage] = useState<OpenOrders>()
    const auth = useAuth()

    useSubscription(`/stream/openOrders/${auth.user?.userId}`,
        (message) => {
            setLastMessage(JSON.parse(message.body))
        })

    return (
        <div>
            {JSON.stringify(lastMessage)}
        </div>
    )
}
