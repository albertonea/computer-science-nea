import ky from "ky";
import {authHttpOptions} from "@/api/httpOptions.ts";

type Order = {
    orderId: string;
    userId: string;
    side: 'BUY' | 'SELL'
    initialQuantity: number;
    remainingQuantity: number;
    ticker: string;
    price: number;
    createdAt: string;
}

export function getOpenOrders(ticker: string):Promise<Order[]> {
    return ky.get(`orders/open-orders/${ticker}`, {
        ...authHttpOptions
    }).json()
}
