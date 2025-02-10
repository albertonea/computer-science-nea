import ky from "ky";
import {authHttpOptions} from "@/api/httpOptions.ts";

export type Trade = {
    tradeId: string
    price: number
    quantity: number
    tradeTime: string
}

export async function getTradeHistory(ticker: string): Promise<Trade[]> {
    return ky
        .get(`trade-history/${ticker}`, {
            ...authHttpOptions,
        }).json()
}
