import {useSubscription} from "react-stomp-hooks";
import {useState} from "react";

export default function InterfaceTrades() {
    const [lastMessage, setLastMessage] = useState<string>()

    useSubscription("/stream/trades/AAPL", (message) => setLastMessage(message.body))

    return (
        <div className="py-2 px-4 flex flex-col gap-8">
            trades
            {lastMessage}
        </div>
    )
}