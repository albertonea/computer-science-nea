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


    return (
        <div>
            orderbook
        </div>
    )
}
