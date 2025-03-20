import ky from "ky";
import {authHttpOptions} from "@/api/httpOptions.ts";

export type Candlestick = {
    open: number;
    high: number;
    low: number;
    close: number;
    volume: number;
    ticker: string;
    intervalStart: string;
}

export type Interval = "FIVE_MINUTES" | "FIFTEEN_MINUTES" | "ONE_HOUR" | "FOUR_HOURS"


export async function getCandlesticks(ticker: string, interval: Interval): Promise<Candlestick[]> {
    return ky
        .get(`trade-history/${ticker}?interval=${interval}`, {
            ...authHttpOptions,
        }).json()
}
