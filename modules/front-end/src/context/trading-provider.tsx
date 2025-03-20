import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {filter, props, map, reduce} from "ramda";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import {getBalances} from "@/api/balances.ts";
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";
import {Candlestick, getCandlesticks, Interval} from "@/api/candlestick.ts";
import {intervalToMs} from "@/lib/utils.ts";

type TradingProviderProps = {
    children: ReactNode
    ticker: string
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

type Trade = {
    tradeId: string
    side: string
    price: number
    quantity: number
    ticker: string
    tradeTime: string
}

type TradingProviderState = {
    balances: TransformedBalances | undefined
    ticker: string
    marketPrice: number | undefined
    openOrders: OpenOrder[]
    candlesticks: Candlestick[]
    orderBook: OrderBook | undefined
    interval: Interval
    setInterval: (interval: Interval) => void
    trades: Trade[]
}

const TradingProviderContext = createContext<TradingProviderState | undefined>(undefined)

export function TradingProvider({children, ticker, orders}: TradingProviderProps){
    const [openOrders, setOpenOrders] = useState<OpenOrder[]>(orders)
    const [candlesticks, setCandlesticks] = useState<Candlestick[]>([])
    const [orderBook, setOrderBook] = useState<OrderBook>()
    const [trades, setTrades] = useState<Trade[]>([])
    const [interval, setInterval] = useState<Interval>("FIFTEEN_MINUTES")
    const [marketPrice, setMarketPrice] = useState<number | undefined>(candlesticks.length ? candlesticks[candlesticks.length - 1].close : undefined)
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

    const {data:candlesticksResponse} = useQuery({
        queryKey: ['tradeHistory', ticker],
        queryFn: () => getCandlesticks(ticker, interval),
        staleTime: 0
    })

    useEffect(() => {
        if (candlesticksResponse) {
            console.log("candlesticks", candlesticks)
            setCandlesticks(candlesticksResponse)
        }
    }, [candlesticksResponse])

    useEffect(() => {
        queryClient.invalidateQueries({
            queryKey: ['tradeHistory', ticker]
        })
    }, [interval]);

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

        const tradeTime = new Date(trade.tradeTime).getTime();
        const intervalMs = intervalToMs[interval];
        const intervalStartMs = Math.floor(tradeTime / intervalMs) * intervalMs;
        const intervalStart = new Date(intervalStartMs).toISOString().replace('Z', '+00:00')
        setTrades((prevTrades) => [...prevTrades, trade]);
        setCandlesticks((prevCandlesticks) => {
            console.log(intervalStart)
            console.log(prevCandlesticks)
            const existingCandleIndex = prevCandlesticks.findIndex(
                (candle) => candle.intervalStart === intervalStart
            );
            console.log(existingCandleIndex)

            const tradePrice = trade.price;
            const tradeQuantity = trade.quantity;

            if (existingCandleIndex >= 0) {
                // Update existing candlestick
                const updatedCandlesticks = [...prevCandlesticks];
                const candle = updatedCandlesticks[existingCandleIndex];

                candle.high = Math.max(candle.high, tradePrice);
                candle.low = Math.min(candle.low, tradePrice);
                candle.close = tradePrice; // Last trade price becomes the close
                candle.volume += tradeQuantity;

                return updatedCandlesticks;
            } else {
                // Create new candlestick
                const newCandle: Candlestick = {
                    ticker: trade.ticker,
                    intervalStart: intervalStart,
                    open: tradePrice, // First trade price is the open
                    high: tradePrice,
                    low: tradePrice,
                    close: tradePrice,
                    volume: tradeQuantity,
                };
                return [...prevCandlesticks, newCandle];
            }
        });
        setMarketPrice(trade.price/100)
    })

    useEffect(() => {
        setOpenOrders(filter((o) => o.remainingQuantity > 0 ,openOrders))
    }, [openOrders]);

    return (
        <TradingProviderContext.Provider {...props} value={{balances, ticker, marketPrice,trades, openOrders, candlesticks, orderBook, interval, setInterval}}>
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
