import {createContext, ReactNode, useContext, useState} from "react";
import {filter, props, map, reduce} from "ramda";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import {getBalances} from "@/api/balances.ts";
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

type TransformedBalances = {
    [key: string]: {
        balance:number,
        lockedBalance:number
    }
}

type TradingProviderState = {
    balances: TransformedBalances | undefined
    ticker: string
    marketPrice: number | undefined
    openOrders: OpenOrder[]
    trades: Trade[]
    orderBook: OrderBook | undefined
}


const TradingProviderContext = createContext<TradingProviderState | undefined>(undefined)

export function TradingProvider({children, ticker, tradeHistory, orders}: TradingProviderProps){
    const [openOrders, setOpenOrders] = useState<OpenOrder[]>(orders)
    const [trades, setTrades] = useState<Trade[]>(tradeHistory)
    const [orderBook, setOrderBook] = useState<OrderBook>()
    const [marketPrice, setMarketPrice] = useState<number | undefined>(tradeHistory.length ? tradeHistory[tradeHistory.length - 1].price/100 : undefined)
    const auth = useAuth();

    const queryClient = useQueryClient();



    const {data:balances} = useQuery({
        queryKey: ['balances'],
        queryFn: getBalances,
        select: (b):TransformedBalances => {
            const filteredTickers = filter((x) => x.ticker === "USD" || x.ticker === ticker ,b)
            return reduce((a, b) => {
                if (b.ticker === "USD") {
                    return {...a, [b.ticker]: {balance:b.balance/100, lockedBalance:b.lockedBalance/100}}
                }
                return {...a, [b.ticker]: {balance:b.balance, lockedBalance:b.lockedBalance}}
            }, {}, filteredTickers)
        },
    })

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
        setMarketPrice(trade.price/100)
    })

    return (
        <TradingProviderContext.Provider {...props} value={{balances, ticker, marketPrice, openOrders, trades, orderBook}}>
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
