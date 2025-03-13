// Parse interval strings
import {Trade} from "@/api/trades.ts";

function parseInterval(intervalStr: string) {
    const value = parseInt(intervalStr, 10);
    if (intervalStr.endsWith("m")) {
        return { unit: "minutes", value, ms: value * 60 * 1000 };
    }
    if (intervalStr.endsWith("h")) {
        return { unit: "hours", value, ms: value * 60 * 60 * 1000 };
    }
    throw new Error("Invalid interval format");
}

// Given a timestamp and an interval string,
// return the bucket start timestamp aligned to the clock
function getBucketStart(timestamp: number, intervalStr: string) {
    const { unit, value } = parseInterval(intervalStr);
    const date = new Date(timestamp);

    // Zero out seconds and milliseconds
    date.setSeconds(0, 0);

    if (unit === "minutes") {
        // For minute-based intervals, snap minutes to the nearest lower multiple
        const minutes = date.getMinutes();
        const bucketMinutes = Math.floor(minutes / value) * value;
        date.setMinutes(bucketMinutes);
    } else if (unit === "hours") {
        // For hour-based intervals, snap the hour to the nearest lower multiple
        const hours = date.getHours();
        const bucketHour = Math.floor(hours / value) * value;
        date.setHours(bucketHour, 0, 0, 0);
    }
    return date.getTime();
}

export type Candles = {
    open: number,
    high: number,
    low: number,
    close: number,
    time: number,
    volume: number
}

// Compute candlesticks from trades
export function computeCandles(trades:Trade[] | undefined, intervalStr:string):Candles[] | undefined {
    const interval: {[key: number]: Candles} = {};
    if (!trades) {
        return undefined;
    }
    for (const trade of trades) {
        // Convert the trade's date string to a timestamp
        const tradeTime = new Date(trade.tradeTime).getTime();
        const intervalStart = getBucketStart(tradeTime, intervalStr);

        if (!interval[intervalStart]) {
            // Start a new candle for this time bucket
            interval[intervalStart] = {
                time: intervalStart,
                open: trade.price,
                high: trade.price,
                low: trade.price,
                close: trade.price,
                volume: trade.price*trade.quantity || 0,
            };
        } else {
            // Update the existing candle
            const candle = interval[intervalStart];
            candle.high = Math.max(candle.high, trade.price);
            candle.low = Math.min(candle.low, trade.price);
            candle.close = trade.price;
            candle.volume += trade.price*trade.quantity || 0;
        }
    }

    // Return the candles as a sorted array by time
    return Object.values(interval).sort((a, b) => a.time - b.time);
}
