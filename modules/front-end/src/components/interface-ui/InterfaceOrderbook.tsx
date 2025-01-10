import {useState} from "react";
import {useStompClient, useSubscription} from "react-stomp-hooks";

type Orderbook = {
    sellSide: {
        price: number;
        quantity: number;
    }[],
    buySide: {
        price: number;
        quantity: number;
    }[],
}

export default function InterfaceOrderbook() {
    const [lastMessage, setLastMessage] = useState<Orderbook>()

    useSubscription('/orderbook',
        (message) => {
        setLastMessage(JSON.parse(message.body))
    })

    return (
        <div>
            orderbook
        </div>
    )
}
