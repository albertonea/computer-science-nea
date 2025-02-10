import {createContext, ReactNode, useContext, useState} from "react";
import {filter, props, map} from "ramda";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import {Balance, getBalances} from "@/api/balances.ts";
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";
import {Trade} from "@/api/trades.ts";

type TradingProviderProps = {
    children: ReactNode
    ticker: string
    tradeHistory: Trade[]
    orders: OpenOrder[]
}

export type OpenOrder = {
    createdAt: string;
    ticker: string;
    orderId: string;
    price: number;
    initialQuantity: number;
    remainingQuantity: number;
    side: 'BUY' | 'SELL';
}

type OrderBookLevel = {
    price: number;
    totalQuantity: number;
}

type OrderBook = {
    ticker: string;
    buySide: OrderBookLevel[];
    sellSide: OrderBookLevel[];
}

type TradingProviderState = {
    balances: Balance[] | undefined
    openOrders: OpenOrder[]
    trades: Trade[]
    orderBook: OrderBook | undefined
}


const TradingProviderContext = createContext<TradingProviderState | undefined>(undefined)

export function TradingProvider({children, ticker, tradeHistory, orders}: TradingProviderProps){
    const [openOrders, setOpenOrders] = useState<OpenOrder[]>(orders)
    const [trades, setTrades] = useState<Trade[]>(tradeHistory)
    const [orderBook, setOrderBook] = useState<OrderBook>()
    const auth = useAuth();

    const queryClient = useQueryClient();
    // balances refetch when orders are filled
    const {data:balances} = useQuery({
        queryKey: ['balances'],
        queryFn: getBalances
    })

    // orders, change array when orders get filled
    useSubscription(`/stream/openOrders/${auth.auth?.user.userId}`,
        (message) => {
            setOpenOrders([...openOrders, JSON.parse(message.body)])
            queryClient.invalidateQueries({
                queryKey: ['balances']
            })
        }
    )

    useSubscription(`/stream/filledOrders/${auth.auth?.user.userId}`,
        (message) => {
            const filledOrder = JSON.parse(message.body)
            queryClient.invalidateQueries({
                queryKey: ['balances']
            })
            if (filledOrder.remainingQuantity > 0) {
                setOpenOrders(
                    map((o) => {
                        if (o.orderId === filledOrder.orderId) {
                            return filledOrder
                        }
                        return o
                    }, openOrders)
                )
            } else {
                setOpenOrders(
                    filter((o) => {
                        return o.orderId !== filledOrder.orderId
                    }, openOrders),
                )
            }
        }
    )

    //orderbook update every 5 seconds with new data
    useSubscription('/stream/orderBook',
        (message) => {
            setOrderBook(JSON.parse(message.body))
        })

    //trades data, update list and update chart
    useSubscription(`/stream/trades/${ticker}`, (message) => {
        const trade = JSON.parse(message.body)
        setTrades([trade, ...trades])
    })

    return (
        <TradingProviderContext.Provider {...props} value={{balances, openOrders, trades, orderBook}}>
            {children}
        </TradingProviderContext.Provider>
    );
}

export const useTradingContext = () => {
    const context = useContext(TradingProviderContext);

    if (context === undefined)
        throw new Error('useTheme must be used within a ThemeProvider');

    return context;
};
